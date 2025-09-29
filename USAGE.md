# OneClick 插件使用指南

## 🎯 智能一键功能 (Command+Shift+D)

OneClick 的核心功能是智能一键生成，它会根据当前的使用场景自动选择最合适的操作。

### 📝 场景一：选中文本智能处理

#### 1.1 字符串常量生成
```java
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public void processUser() {
        // 选中字符串 "USER_NOT_FOUND" → 按 Command+Shift+D
        throw new RuntimeException("USER_NOT_FOUND");

        // 自动生成常量字段（插入到LOGGER下方）：
        // private static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    }
}
```

#### 1.2 命名风格智能转换
```java
// 驼峰转下划线
userService → user_service
emailAddress → email_address
createTime → create_time

// 下划线转驼峰
user_name → userName
email_address → emailAddress
create_time → createTime
```

**使用方法**：
1. 选中要转换的标识符
2. 按 `Command+Shift+D` (macOS) / `Ctrl+Shift+D` (Windows)
3. 自动静默转换，无弹窗干扰

### 🏗️ 场景二：类级别智能生成

#### 2.1 JavaBean类自动识别
```java
// 包名包含：model, entity, dto, vo, bean 等
package com.example.model;

public class User {
    private Long id;
    private String username;
    private String email;
    private Date createTime;

    // 按 Command+Shift+D 自动生成：
    // - getter/setter 方法
    // - toString 方法 (JSON格式)
    // - equals/hashCode 方法
}
```

#### 2.2 业务类智能增强
```java
// 包名包含：service, controller, manager, handler 等
package com.example.service;

@Service
public class UserService {
    // 按 Command+Shift+D 自动执行：
    // 1. 生成 Logger 字段
    // 2. 生成 serialVersionUID
    // 3. 字段智能排序（只排序实例字段，保护常量和静态字段）

    private UserRepository userRepository;
    private EmailService emailService;
    private ValidationService validationService;
}
```

### 🔧 智能特性详解

#### 字段排序规则
- **只排序实例字段**：排除 static、final、常量字段
- **保护重要字段**：LOGGER、serialVersionUID 等保持原位置
- **智能识别常量**：全大写+下划线命名的字段不参与排序
- **排序方式**：支持按名称、长度、类型排序

#### 常量生成位置
- **有LOGGER字段**：常量插入到LOGGER下一行
- **无LOGGER字段**：常量插入到类的最上面
- **重复检测**：避免生成重复的常量字段

#### 类类型识别
| 类类型 | 包名特征 | 生成内容 |
|--------|---------|---------|
| JavaBean | model, entity, dto, vo, bean, pojo | getter/setter/toString/equals/hashCode |
| 业务类 | service, controller, manager, handler, component | Logger/serialVersionUID/字段排序 |

## 🛠️ 开发工具集合 (Command+Shift+U)

### 代码生成工具
- **🔧 生成UUID**: 生成随机UUID字符串
- **📅 插入时间戳**: 插入当前时间戳
- **🔍 生成序列化ID**: 生成serialVersionUID
- **📝 生成TODO注释**: 生成带时间和作者的TODO注释
- **🎯 生成测试方法**: 生成单元测试方法模板

### 代码转换工具
- **🔒 生成常量**: 将选中文本转换为常量定义
- **📊 生成枚举**: 创建新的枚举文件（不在当前文件中插入）
- **🌐 生成JSON模板**: 生成JSON数据模板
- **🔄 转换命名风格**: 转换驼峰/下划线命名
- **📋 生成Builder模式**: 为当前类生成Builder模式

### 特殊功能说明
- **枚举生成**：会在当前文件同目录下创建新的枚举文件，而不是在当前文件中插入代码
- **常量生成**：智能检测LOGGER字段，将常量插入到合适位置
- **Builder模式**：为当前类生成完整的Builder模式实现

## 🗄️ 数据库工具集合 (Command+Shift+Y)

### JPA/Hibernate工具
- **🗃️ 生成Entity注解**: 为实体类添加JPA注解
- **📝 生成SQL建表语句**: 根据实体类生成CREATE TABLE语句
- **🔍 生成Repository接口**: 生成Spring Data JPA Repository

### MyBatis工具
- **📊 生成MyBatis Mapper**: 生成MyBatis Mapper接口和XML
- **🔄 生成数据转换器**: 生成Entity与DTO转换器

### Spring Boot集成
- **📋 生成CRUD Service**: 生成完整的CRUD Service类
- **🌐 生成REST Controller**: 生成RESTful API控制器
- **🔧 生成数据库配置**: 生成数据源配置类

## 📋 其他功能

### 批量生成 (Command+Shift+B)
- **功能**: 批量处理多个Java文件或包
- **项目视图集成**: 右键包或文件 → OneClick → 批量生成
- **多文件支持**: 可同时选中多个文件进行批量处理
- **进度显示**: 显示处理进度和结果统计

### 代码模板 (Command+Shift+T)
- **功能**: 提供15种设计模式和架构模板
- **设计模式**: 单例、工厂、观察者、建造者、策略模式等
- **架构模板**: REST Controller、Service Layer、Repository等
- **工具类**: 验证工具、日期工具、字符串工具等

### 重构助手 (Command+Shift+R)
- **功能**: 10种代码重构操作
- **包含**: 提取常量、转换Stream API、添加空值检查等
- **智能分析**: 根据代码上下文提供重构建议

### 智能注释 (Command+Shift+C)
- **功能**: 生成智能注释和文档
- **JavaDoc**: 根据方法签名自动生成标准JavaDoc
- **中文注释**: 根据代码上下文生成中文注释

### 代码清理 (Command+Shift+L)
- **功能**: 清理无用代码和导入
- **清理项目**: 移除未使用导入、空行、调试代码等
- **格式化**: 统一代码格式和导入顺序

### 代码分析 (Command+Shift+A)
- **功能**: 代码质量分析
- **统计指标**: 行数、复杂度、方法长度等
- **质量检测**: 长方法、大类、TODO注释统计

### 快速文档 (Command+Shift+Q)
- **功能**: 快速生成文档
- **文档类型**: README、API文档、使用说明等
- **自动格式**: 生成标准格式的项目文档

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

### 智能快捷键使用策略
- **`Command+Shift+D`** 是核心智能快捷键，会根据当前上下文自动选择操作
- **选中文本时**：优先进行文本处理（常量生成、命名转换）
- **类级别时**：根据类类型智能生成相应代码
- **无选中时**：分析类特征并执行相应的增强操作

### 快捷键规范 (统一使用Command+Shift)
| 功能 | Windows/Linux | macOS | 说明 |
|------|---------------|-------|------|
| **智能一键生成** | `Ctrl+Shift+D` | `Cmd+Shift+D` | 🎯 核心功能：智能识别场景 |
| 开发工具集合 | `Ctrl+Shift+U` | `Cmd+Shift+U` | 🛠️ 20种开发工具 |
| 数据库工具 | `Ctrl+Shift+Y` | `Cmd+Shift+Y` | 🗄️ 数据库代码生成 |
| 批量生成 | `Ctrl+Shift+B` | `Cmd+Shift+B` | 📦 批量处理文件/包 |
| 代码模板 | `Ctrl+Shift+T` | `Cmd+Shift+T` | 📝 设计模式模板 |
| 重构助手 | `Ctrl+Shift+R` | `Cmd+Shift+R` | 🔄 重构操作 |
| 智能注释 | `Ctrl+Shift+C` | `Cmd+Shift+C` | 💬 自动注释 |
| 代码清理 | `Ctrl+Shift+L` | `Cmd+Shift+L` | 🧹 清理代码 |
| 代码分析 | `Ctrl+Shift+A` | `Cmd+Shift+A` | 📊 质量分析 |
| 快速文档 | `Ctrl+Shift+Q` | `Cmd+Shift+Q` | 📖 文档生成 |
| 折叠方法 | `Ctrl+Shift+F` | `Cmd+Shift+F` | 📁 方法折叠 |

### 项目视图集成
- **OneClick菜单组**：在项目视图右键菜单中提供完整的OneClick工具
- **批量操作**：选中多个文件或包进行批量处理
- **上下文感知**：根据选中的文件类型提供相应功能

### 智能特性利用
- **字段排序**：业务类中自动排序，JavaBean类保持原顺序
- **常量位置**：新常量自动插入到LOGGER下方，保持代码整洁
- **枚举文件**：生成独立枚举文件，避免当前文件过于复杂
- **命名转换**：静默执行，无弹窗干扰，提升操作流畅度

### 设置优化
- **语言设置**：下拉菜单选择，支持中英文切换
- **快捷键预设**：提供多种预设方案，一键应用
- **字段排序配置**：支持按名称、长度、类型排序
- **包规则自定义**：可自定义JavaBean和业务类的包名规则

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
