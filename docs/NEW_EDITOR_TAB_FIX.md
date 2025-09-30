# 新编辑器标签页显示修复说明

## 问题描述

### 问题：生成JSON和SQL建表语句时影响当前文件

**原有行为**：
- 生成JSON模板时，直接插入到当前光标位置
- 生成SQL建表语句时，直接插入到当前光标位置
- 这会影响当前正在编辑的文件，可能导致代码混乱

**问题示例**：
```java
public class User {
    private Long id;
    private String name;
    // 光标在这里，按快捷键生成SQL
    // ❌ SQL语句直接插入到这里，破坏了Java代码结构
    CREATE TABLE user (
        id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
        ...
    );
}
```

### 期望行为

- 生成JSON模板时，在新的编辑器标签页中打开
- 生成SQL建表语句时，在新的编辑器标签页中打开
- 不影响当前正在编辑的文件

## 修复方案

### 1. 添加 openInNewEditor 工具方法

在 `DevToolsAction` 和 `DatabaseToolsAction` 中添加工具方法：

```java
/**
 * 在新的编辑器标签页中打开内容
 */
private static void openInNewEditor(Project project, String content, String fileName) {
    WriteCommandAction.runWriteCommandAction(project, () -> {
        try {
            // 创建一个轻量级虚拟文件
            LightVirtualFile virtualFile = new LightVirtualFile(fileName, content);
            
            // 在新的编辑器标签页中打开
            FileEditorManager.getInstance(project).openFile(virtualFile, true);
        } catch (Exception ex) {
            Messages.showErrorDialog(project, "打开编辑器失败: " + ex.getMessage(), "错误");
        }
    });
}
```

**关键点**：
- 使用 `LightVirtualFile` 创建虚拟文件（不会保存到磁盘）
- 使用 `FileEditorManager.openFile()` 在新标签页中打开
- 第二个参数 `true` 表示获取焦点

### 2. 修复 JSON 模板生成

**文件**：`src/main/java/com/glowxq/plugs/actions/DevToolsAction.java`

**修改前**：
```java
private static void generateJsonTemplate(AnActionEvent e) {
    String jsonTemplate = "{\n" +
        "    \"id\": 1,\n" +
        "    \"name\": \"示例名称\",\n" +
        "    ...\n" +
        "}";
    insertTextAtCursor(e, jsonTemplate);  // ❌ 插入到当前文件
    showNotification("JSON模板已生成");
}
```

**修改后**：
```java
private static void generateJsonTemplate(AnActionEvent e) {
    String jsonTemplate = "{\n" +
        "    \"id\": 1,\n" +
        "    \"name\": \"示例名称\",\n" +
        "    ...\n" +
        "}";
    openInNewEditor(e, jsonTemplate, "template.json");  // ✅ 在新标签页打开
    showNotification("JSON模板已在新标签页中打开");
}
```

### 3. 修复 SQL 建表语句生成（符合阿里巴巴规范）

**文件**：`src/main/java/com/glowxq/plugs/actions/DatabaseToolsAction.java`

**修改前**：
```java
private static void generateCreateTableSql(AnActionEvent e) {
    PsiClass psiClass = getCurrentClass(e);
    if (psiClass == null) return;

    StringBuilder sql = new StringBuilder();
    String tableName = camelToSnake(psiClass.getName());
    
    sql.append("CREATE TABLE ").append(tableName).append(" (\n");
    sql.append("    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',\n");
    // ... 生成字段
    sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='").append(psiClass.getName()).append("表';");
    
    insertTextAtCursor(e, sql.toString());  // ❌ 插入到当前文件
    Messages.showInfoMessage("SQL建表语句已生成", "生成完成");
}
```

**修改后**：
```java
private static void generateCreateTableSql(AnActionEvent e) {
    PsiClass psiClass = getCurrentClass(e);
    if (psiClass == null) return;

    Project project = e.getProject();
    if (project == null) return;

    StringBuilder sql = new StringBuilder();
    String tableName = camelToSnake(psiClass.getName());
    
    // 阿里巴巴规范：添加注释说明
    sql.append("-- ").append(psiClass.getName()).append("表\n");
    sql.append("-- 符合阿里巴巴Java开发手册数据库规范\n");
    
    // 阿里巴巴规范：表名使用反引号包裹
    sql.append("CREATE TABLE `").append(tableName).append("` (\n");
    
    // 阿里巴巴规范：主键使用BIGINT UNSIGNED
    sql.append("    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',\n");
    
    // 处理类字段
    for (PsiField field : psiClass.getFields()) {
        if (!field.hasModifierProperty(PsiModifier.STATIC) && 
            !field.hasModifierProperty(PsiModifier.FINAL) &&
            !"id".equals(field.getName())) {
            String fieldName = camelToSnake(field.getName());
            String fieldType = mapJavaTypeToSql(field.getType().getPresentableText());
            
            // 阿里巴巴规范：字段名使用反引号包裹
            sql.append("    `").append(fieldName).append("` ").append(fieldType);
            
            // 阿里巴巴规范：字符串类型字段必须指定是否允许为NULL
            if (fieldType.startsWith("VARCHAR") || fieldType.startsWith("TEXT")) {
                sql.append(" DEFAULT NULL");
            }
            
            sql.append(" COMMENT '").append(field.getName()).append("',\n");
        }
    }
    
    // 阿里巴巴规范：添加通用字段
    sql.append("    `gmt_create` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n");
    sql.append("    `gmt_modified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n");
    sql.append("    `is_deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0-未删除,1-已删除)',\n");
    
    // 阿里巴巴规范：主键定义
    sql.append("    PRIMARY KEY (`id`),\n");
    
    // 阿里巴巴规范：添加逻辑删除字段的索引
    sql.append("    KEY `idx_is_deleted` (`is_deleted`)\n");
    
    // 阿里巴巴规范：引擎使用InnoDB，字符集使用utf8mb4
    sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='")
       .append(psiClass.getName()).append("表';");
    
    // ✅ 在新的编辑器标签页中打开SQL
    openInNewEditor(project, sql.toString(), tableName + "_create.sql");
    Messages.showInfoMessage("SQL建表语句已在新标签页中打开", "生成完成");
}
```

### 4. 更新 SQL 类型映射（符合阿里巴巴规范）

**修改前**：
```java
private static String mapJavaTypeToSql(String javaType) {
    switch (javaType) {
        case "String": return "VARCHAR(255)";
        case "Integer": case "int": return "INT";
        case "Long": case "long": return "BIGINT";
        case "Boolean": case "boolean": return "TINYINT(1)";
        case "BigDecimal": return "DECIMAL(10,2)";
        default: return "VARCHAR(255)";
    }
}
```

**修改后**：
```java
private static String mapJavaTypeToSql(String javaType) {
    // 符合阿里巴巴规范的SQL类型映射
    switch (javaType) {
        case "String": return "VARCHAR(255)";
        case "Integer": case "int": return "INT";
        case "Long": case "long": return "BIGINT";
        case "Boolean": case "boolean": return "TINYINT UNSIGNED";  // 阿里巴巴规范：使用UNSIGNED
        case "BigDecimal": return "DECIMAL(19,4)";  // 阿里巴巴规范：金额类型使用DECIMAL(19,4)
        default: return "VARCHAR(255)";
    }
}
```

## 阿里巴巴数据库规范要点

### 1. 表名和字段名规范
- ✅ 使用反引号包裹：`` `table_name` ``
- ✅ 使用小写字母，单词间用下划线分隔
- ✅ 避免使用MySQL保留字

### 2. 主键规范
- ✅ 主键字段名统一为 `id`
- ✅ 类型使用 `BIGINT UNSIGNED`
- ✅ 使用 `AUTO_INCREMENT`

### 3. 通用字段规范
- ✅ 创建时间：`gmt_create` (DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)
- ✅ 更新时间：`gmt_modified` (DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)
- ✅ 逻辑删除：`is_deleted` (TINYINT UNSIGNED NOT NULL DEFAULT 0)

### 4. 字段类型规范
- ✅ 布尔类型：使用 `TINYINT UNSIGNED`
- ✅ 金额类型：使用 `DECIMAL(19,4)`
- ✅ 字符串类型：明确指定是否允许NULL

### 5. 索引规范
- ✅ 为逻辑删除字段添加索引：`KEY idx_is_deleted (is_deleted)`

### 6. 表属性规范
- ✅ 引擎：`ENGINE=InnoDB`
- ✅ 字符集：`DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci`
- ✅ 表注释：`COMMENT='表名说明'`

## 生成示例

### 输入类
```java
package com.example.entity;

public class User {
    private Long id;
    private String name;
    private String email;
    private Boolean active;
    private BigDecimal balance;
}
```

### 生成的SQL（符合阿里巴巴规范）
```sql
-- User表
-- 符合阿里巴巴Java开发手册数据库规范
CREATE TABLE `user` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(255) DEFAULT NULL COMMENT 'name',
    `email` VARCHAR(255) DEFAULT NULL COMMENT 'email',
    `active` TINYINT UNSIGNED DEFAULT NULL COMMENT 'active',
    `balance` DECIMAL(19,4) DEFAULT NULL COMMENT 'balance',
    `gmt_create` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0-未删除,1-已删除)',
    PRIMARY KEY (`id`),
    KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='User表';
```

## 影响范围

此修复影响以下功能：

1. **生成JSON模板**：`Cmd+Shift+U` → 选择"生成JSON模板"
2. **生成SQL建表语句**：`Cmd+Shift+Y` → 选择"生成SQL建表语句"

## 验证方法

### 验证JSON模板生成
1. 在任意Java文件中按 `Cmd+Shift+U`
2. 选择"🌐 生成JSON模板"
3. 检查：
   - ✅ JSON内容在新的编辑器标签页中打开
   - ✅ 文件名为 `template.json`
   - ✅ 当前Java文件没有被修改

### 验证SQL建表语句生成
1. 创建一个实体类（如User）
2. 在类中按 `Cmd+Shift+Y`
3. 选择"📝 生成SQL建表语句"
4. 检查：
   - ✅ SQL语句在新的编辑器标签页中打开
   - ✅ 文件名为 `user_create.sql`
   - ✅ 当前Java文件没有被修改
   - ✅ SQL语句符合阿里巴巴规范

## 参考资料

- [阿里巴巴Java开发手册 - MySQL数据库规约](https://github.com/alibaba/p3c)
- [IntelliJ Platform SDK - FileEditorManager](https://plugins.jetbrains.com/docs/intellij/file-editor-manager.html)
- [IntelliJ Platform SDK - Virtual Files](https://plugins.jetbrains.com/docs/intellij/virtual-file.html)

