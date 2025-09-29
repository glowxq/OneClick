# OneClick 插件使用指南

## 🎯 智能一键功能

### 1. 智能一键生成 (核心功能)
- **快捷键**: `Ctrl+Alt+G` (Windows/Linux) / `Cmd+Alt+G` (macOS)
- **智能特性**: 根据类类型自动选择合适的生成操作
  - **JavaBean类**: 生成 getter、setter、toString、equals、hashCode 方法
  - **业务类**: 生成 Logger 字段、serialVersionUID 等
- **使用方法**: 在 Java 类中按快捷键，插件会智能识别类类型并提供相应选项

## 🛠️ 开发工具集合

### 2. 开发工具 (Ctrl+Alt+U)
提供各种实用的开发辅助功能：

- **🔧 生成UUID**: 生成随机UUID字符串
- **📅 插入时间戳**: 插入当前时间戳
- **🔍 生成序列化ID**: 生成serialVersionUID
- **📝 生成TODO注释**: 生成带时间和作者的TODO注释
- **🎯 生成测试方法**: 生成单元测试方法模板
- **🔒 生成常量**: 将选中文本转换为常量定义
- **📊 生成枚举**: 生成枚举类模板
- **🌐 生成JSON模板**: 生成JSON数据模板
- **🔄 转换命名风格**: 转换驼峰/下划线命名
- **📋 生成Builder模式**: 为当前类生成Builder模式

### 3. 数据库工具 (Ctrl+Alt+Y)
提供数据库相关的代码生成功能：

- **🗃️ 生成Entity注解**: 为实体类添加JPA注解
- **📝 生成SQL建表语句**: 根据实体类生成CREATE TABLE语句
- **🔍 生成Repository接口**: 生成Spring Data JPA Repository
- **📊 生成MyBatis Mapper**: 生成MyBatis Mapper接口和XML
- **🔄 生成数据转换器**: 生成Entity与DTO转换器
- **📋 生成CRUD Service**: 生成完整的CRUD Service类
- **🌐 生成REST Controller**: 生成RESTful API控制器
- **🔧 生成数据库配置**: 生成数据源配置类

## 📋 其他功能

### 4. 批量生成 (Ctrl+Alt+B)
- **功能**: 批量处理多个Java文件或包
- **使用方法**: 在项目视图中选中文件或包，右键选择"批量生成"

### 5. 代码模板 (Ctrl+Alt+T)
- **功能**: 提供15种设计模式和架构模板
- **包含**: 单例模式、工厂模式、观察者模式、Spring配置等

### 6. 重构助手 (Ctrl+Alt+R)
- **功能**: 代码重构和优化建议
- **特性**: 智能分析代码结构，提供改进建议

### 7. 智能注释 (Ctrl+Alt+C)
- **功能**: 生成智能注释和文档
- **特性**: 根据方法签名自动生成JavaDoc

### 8. 代码清理 (Ctrl+Alt+L)
- **功能**: 清理无用代码和导入
- **特性**: 自动移除未使用的导入和变量

### 9. 代码分析 (Ctrl+Alt+A)
- **功能**: 代码质量分析
- **特性**: 检测潜在问题和性能优化点

### 10. 快速文档 (Ctrl+Alt+D)
- **功能**: 快速生成文档
- **特性**: 生成README、API文档等

## ⚙️ 设置配置

### 快捷键设置
1. 打开 `File` → `Settings` → `Tools` → `OneClick Settings`
2. 选择 `Keymap Settings` 子页面
3. 可以查看和修改所有快捷键设置
4. 支持快捷键预设方案：
   - 默认预设 (推荐)
   - VS Code 风格
   - Eclipse 风格
   - 自定义预设

### 功能设置
1. **JavaBean设置**: 配置生成方法的样式和选项
2. **业务类设置**: 配置Logger类型、字段排序等
3. **包规则设置**: 自定义包名检测规则
4. **字段排序**: 支持按名称、长度、类型排序

## 🚀 使用技巧

### 智能快捷键使用
- `Ctrl+Alt+G` 是智能快捷键，会根据当前类的类型自动选择合适的功能
- 在JavaBean类中：优先生成getter/setter等方法
- 在业务类中：优先生成Logger字段等

### 操作系统适配
- **Windows/Linux**: 使用 `Ctrl` 作为主修饰键
- **macOS**: 使用 `Cmd` 作为主修饰键，`Option` 相当于 `Alt`
- 插件会自动检测操作系统并显示相应的快捷键格式

### 批量操作
- 在项目视图中可以选中多个文件或整个包进行批量处理
- 支持进度显示和错误统计
- 可以同时处理多种类型的Java文件

### 设置同步
- 所有设置都会实时保存
- 支持导入/导出设置配置
- 可以在不同项目间共享设置

## 📞 获取帮助

- **插件概览**: `Tools` → `OneClick Settings` → `OneClick Overview`
- **在线帮助**: 右键菜单中的"OneClick Help"
- **快捷键列表**: 设置面板中的完整快捷键对照表

通过这些功能，OneClick插件可以大大提升Java开发效率，让代码生成变得更加智能和便捷！

2. **一键折叠JavaBean方法**
   - Windows/Linux: `Ctrl+Alt+F`
   - macOS: `Cmd+Option+F`
   - 右键菜单: JavaBean Tools → Fold JavaBean Methods

## 详细使用说明

### 1. 生成JavaBean方法

#### 操作步骤：
1. 在Java类中定义字段
2. 将光标放在类定义内的任意位置
3. 使用以下任一方式触发：
   - **快捷键**: `Ctrl+Alt+G` (Windows/Linux) 或 `Cmd+Option+G` (macOS)
   - **右键菜单**: 右键 → JavaBean Tools → Generate JavaBean Methods
   - **主菜单**: Code → Generate → Generate JavaBean Methods
4. 插件将自动生成所需的方法

#### 生成规则：
- **Getter方法**：为所有非静态、非final字段生成getter方法
  - 普通字段：`getFieldName()`
  - boolean字段：`isFieldName()` 或 `getFieldName()`
- **Setter方法**：为所有非静态、非final字段生成setter方法
  - 格式：`setFieldName(FieldType fieldName)`
- **toString方法**：生成JSON格式的toString方法
  - 自动删除已存在的toString方法
  - 重新生成包含所有字段的JSON格式toString

#### 智能特性：
- ✅ 不会重复生成已存在的getter/setter方法
- ✅ 每次都会重新生成toString方法（确保包含最新字段）
- ✅ 自动处理不同数据类型的JSON格式化
- ✅ 跳过静态字段和final字段

### 2. 折叠JavaBean方法

#### 操作步骤：
1. 将光标放在包含JavaBean方法的类中
2. 使用以下任一方式触发：
   - **快捷键**: `Ctrl+Alt+F` (Windows/Linux) 或 `Cmd+Option+F` (macOS)
   - **右键菜单**: 右键 → JavaBean Tools → Fold JavaBean Methods
   - **主菜单**: Code → Fold JavaBean Methods
3. 所有getter/setter/toString方法将被折叠

#### 折叠效果：
- Getter方法显示为：`getter: getFieldName()`
- Setter方法显示为：`setter: setFieldName(FieldType)`
- toString方法显示为：`toString(): String`

## 示例演示

### 原始类：
```java
public class User {
    private String name;
    private int age;
    private boolean active;
    private List<String> hobbies;
}
```

### 按 `Ctrl+Alt+G` 后：
```java
public class User {
    private String name;
    private int age;
    private boolean active;
    private List<String> hobbies;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + name + "\"" + "," +
                "\"age\":" + age + "," +
                "\"active\":" + active + "," +
                "\"hobbies\":" + hobbies +
                "}";
    }
}
```

### 按 `Ctrl+Alt+F` 后（折叠效果）：
```java
public class User {
    private String name;
    private int age;
    private boolean active;
    private List<String> hobbies;

    getter: getName()
    setter: setName(String)
    getter: getAge()
    setter: setAge(int)
    getter: isActive()
    setter: setActive(boolean)
    getter: getHobbies()
    setter: setHobbies(List)
    toString(): String
}
```

## 注意事项

1. **文件类型**：插件只在Java文件中生效
2. **光标位置**：需要将光标放在类定义内
3. **字段类型**：只处理实例字段（非静态、非final）
4. **toString重新生成**：每次生成都会删除旧的toString方法并创建新的

## 快捷键总结

| 功能 | Windows/Linux | macOS | 说明 |
|------|---------------|-------|------|
| 生成JavaBean方法 | `Ctrl+Alt+G` | `Cmd+Option+G` | 生成getter/setter/toString |
| 折叠JavaBean方法 | `Ctrl+Alt+F` | `Cmd+Option+F` | 折叠所有JavaBean方法 |

## 访问方式

### 快捷键
- **Windows/Linux**: `Ctrl+Alt+G` / `Ctrl+Alt+F`
- **macOS**: `Cmd+Option+G` / `Cmd+Option+F`

### 主菜单
- **生成方法**: Code → Generate → Generate JavaBean Methods
- **折叠方法**: Code → Fold JavaBean Methods

### 右键菜单
- 在Java文件中右键 → **JavaBean Tools** → 选择对应功能
  - Generate JavaBean Methods
  - Fold JavaBean Methods

## 故障排除

如果插件不工作，请检查：
1. 是否在Java文件中使用
2. 光标是否在类定义内
3. 类是否包含实例字段
4. IDEA版本是否兼容（需要2023.2+）
