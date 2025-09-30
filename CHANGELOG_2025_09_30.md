# OneClick 插件更新日志 - 2025-09-30

## 🐛 Bug修复

### 1. 修复 Boolean vs boolean 的 getter 方法命名问题

**问题**：
- 原代码将 `Boolean`（包装类型）和 `boolean`（基本类型）都使用 `isXxx()` 方法
- 不符合JavaBeans规范，应该只有基本类型 `boolean` 使用 `isXxx()`，包装类型 `Boolean` 使用 `getXxx()`

**修复**：
- ✅ `boolean` 类型字段：生成 `isXxx()` 方法
- ✅ `Boolean` 类型字段：生成 `getXxx()` 方法
- ✅ toEntity/fromEntity 方法正确调用对应的 getter/setter

**示例**：
```java
// 修复前 ❌
private Boolean active;
public Boolean isActive() { return active; }  // 错误

// 修复后 ✅
private Boolean active;
public Boolean getActive() { return active; }  // 正确
```

**影响功能**：
- JavaBean方法生成（`Cmd+Shift+D`）
- DTO/VO/BO类生成
- toEntity/fromEntity 方法生成

### 2. 生成JSON和SQL在新编辑器标签页打开

**问题**：
- 原代码生成JSON模板和SQL建表语句时，直接插入到当前光标位置
- 会破坏当前正在编辑的文件

**修复**：
- ✅ JSON模板在新标签页 `template.json` 中打开
- ✅ SQL建表语句在新标签页 `xxx_create.sql` 中打开
- ✅ 不影响当前正在编辑的文件

**影响功能**：
- 生成JSON模板（`Cmd+Shift+U` → "生成JSON模板"）
- 生成SQL建表语句（`Cmd+Shift+Y` → "生成SQL建表语句"）

## ✨ 功能改进

### SQL建表语句符合阿里巴巴规范

**改进内容**：
1. **表名和字段名**：使用反引号包裹 `` `table_name` ``
2. **主键规范**：`BIGINT UNSIGNED NOT NULL AUTO_INCREMENT`
3. **通用字段**：
   - `gmt_create` - 创建时间
   - `gmt_modified` - 更新时间
   - `is_deleted` - 逻辑删除标记
4. **索引规范**：为逻辑删除字段添加索引
5. **字段类型**：
   - Boolean → `TINYINT UNSIGNED`
   - BigDecimal → `DECIMAL(19,4)`（金额类型）
6. **表属性**：`ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci`

**生成示例**：
```sql
-- User表
-- 符合阿里巴巴Java开发手册数据库规范
CREATE TABLE `user` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(255) DEFAULT NULL COMMENT 'name',
    `gmt_create` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0-未删除,1-已删除)',
    PRIMARY KEY (`id`),
    KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='User表';
```

## 📝 文档更新

### 新增文档
1. `docs/BOOLEAN_FIELD_FIX.md` - Boolean字段处理修复详细说明
2. `docs/NEW_EDITOR_TAB_FIX.md` - 新编辑器标签页显示修复说明
3. `docs/FIXES_SUMMARY.md` - 修复总结文档

### 新增测试文件
1. `src/test/java/com/glowxq/plugs/model/TestBooleanFieldBean.java` - Boolean字段测试类

## 🔧 技术细节

### 修改的文件
1. `src/main/java/com/glowxq/plugs/utils/JavaBeanUtils.java`
   - 修复 `getGetterName()` 方法

2. `src/main/java/com/glowxq/plugs/actions/GenerateJavaBeanMethodsAction.java`
   - 修复 `getGetterNameForField()` 方法
   - 修复 DTO/VO/BO 生成中的 getter 方法

3. `src/main/java/com/glowxq/plugs/actions/DevToolsAction.java`
   - 添加 `openInNewEditor()` 方法
   - 修复 `generateJsonTemplate()` 方法

4. `src/main/java/com/glowxq/plugs/actions/DatabaseToolsAction.java`
   - 添加 `openInNewEditor()` 方法
   - 重写 `generateCreateTableSql()` 方法
   - 更新 `mapJavaTypeToSql()` 方法

### 新增依赖
- `com.intellij.testFramework.LightVirtualFile` - 用于创建虚拟文件

## ✅ 验证方法

### 验证 Boolean/boolean 修复
1. 创建包含 `boolean` 和 `Boolean` 字段的类
2. 按 `Cmd+Shift+D` 生成JavaBean方法
3. 检查：`boolean` 使用 `isXxx()`，`Boolean` 使用 `getXxx()`

### 验证 JSON/SQL 新标签页
1. 按 `Cmd+Shift+U` → "生成JSON模板"
   - ✅ JSON在新标签页打开
   - ✅ 当前文件未修改

2. 按 `Cmd+Shift+Y` → "生成SQL建表语句"
   - ✅ SQL在新标签页打开
   - ✅ 当前文件未修改
   - ✅ SQL符合阿里巴巴规范

## 🎯 兼容性

- ✅ 向后兼容：不会破坏现有功能
- ✅ JavaBeans规范：符合标准命名规范
- ✅ 阿里巴巴规范：SQL生成符合开发手册
- ✅ IntelliJ平台：使用标准API

## 📚 参考资料

- [JavaBeans规范](https://docs.oracle.com/javase/8/docs/api/java/beans/package-summary.html)
- [阿里巴巴Java开发手册](https://github.com/alibaba/p3c)
- [IntelliJ Platform SDK](https://plugins.jetbrains.com/docs/intellij/welcome.html)

---

**感谢反馈！** 如有任何问题或建议，请随时提出。

