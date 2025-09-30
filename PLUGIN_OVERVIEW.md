# OneClick 插件功能完整指南 🚀

## 🎯 智能一键生成 (Command+Shift+D) - 核心功能

OneClick 插件的核心是智能一键生成功能，它会根据不同的使用场景自动选择最合适的操作。一个快捷键，六大场景，智能识别，自动执行！

### 📝 六大智能场景详解

#### 场景1：选中变量名 - 命名风格循环切换 🔄
**操作**：选中变量名 → 按 `Cmd+Shift+D`

**切换顺序**：小驼峰 → 大驼峰 → 下划线小写 → 下划线大写 → 小驼峰

| 当前风格 | 按一次 | 按两次 | 按三次 | 按四次 |
|---------|--------|--------|--------|--------|
| `userName` | `UserName` | `user_name` | `USER_NAME` | `userName` |
| `emailAddress` | `EmailAddress` | `email_address` | `EMAIL_ADDRESS` | `emailAddress` |
| `createTime` | `CreateTime` | `create_time` | `CREATE_TIME` | `createTime` |

**特点**：
- ✅ 静默执行，无弹窗干扰
- ✅ 连续按快捷键循环切换
- ✅ 自动识别当前命名风格
- ✅ 保持文本选中状态

#### 场景2：选中字符串 - 常量生成 📝
**操作**：选中字符串字面量 → 按 `Cmd+Shift+D`

**示例**：
```java
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public void validateUser() {
        // 选中 "USER_NOT_FOUND" → 按 Cmd+Shift+D
        throw new RuntimeException("USER_NOT_FOUND");

        // 自动生成（插入到LOGGER下方）：
        // private static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    }
}
```

**智能特性**：
- ✅ 有LOGGER字段：插入到LOGGER下方
- ✅ 无LOGGER字段：插入到类顶部
- ✅ 自动去重：避免生成重复常量
- ✅ 命名规范：自动转换为大写下划线格式

#### 场景3：选中类名 - 生成DTO/VO/BO 🏗️
**操作**：选中类名 → 按 `Cmd+Shift+D` → 选择类型

**支持类型**：
- **DTO** (Data Transfer Object) - 数据传输对象
- **VO** (Value Object) - 值对象
- **BO** (Business Object) - 业务对象

**生成特性**：
- ✅ 自动创建dto/vo/bo子目录
- ✅ 包声明包含子包名（如 `com.example.entity.dto`）
- ✅ 添加@Serial注解到serialVersionUID
- ✅ 生成JSON格式的toString方法
- ✅ 支持BeanUtils或原生getter/setter
- ✅ boolean字段生成isXxx()方法
- ✅ 方法排序：toEntity → fromEntity → getter/setter → toString

**示例**：
```java
// 源类：com.example.entity.User
public class User {
    private Long id;
    private String name;
    private boolean active;
}

// 选中 "User" → Cmd+Shift+D → 选择 "DTO"
// 生成：com/example/entity/dto/UserDTO.java

package com.example.entity.dto;

import java.io.Serializable;
import java.io.Serial;
import com.example.entity.User;
import org.springframework.beans.BeanUtils;

public class UserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private boolean active;

    public UserDTO() {}

    // toEntity() - 使用BeanUtils
    public User toEntity() {
        User entity = new User();
        BeanUtils.copyProperties(this, entity);
        return entity;
    }

    // fromEntity() - 使用BeanUtils
    public static UserDTO fromEntity(User entity) {
        if (entity == null) return null;
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isActive() { return active; }  // boolean使用isXxx()
    public void setActive(boolean active) { this.active = active; }

    // JSON格式toString
    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id + ", " +
                "\"name\":\"" + name + "\", " +
                "\"active\":" + active +
                "}";
    }
}
```

#### 场景4：JavaBean类 - 自动生成标准方法 ☕
**识别规则**：包名包含 `entity, model, bean, pojo, dto, vo, domain, data, bo, record`

**操作**：在JavaBean类中 → 按 `Cmd+Shift+D`

**生成内容**：
- ✅ getter/setter方法
- ✅ toString方法（JSON格式）
- ✅ equals/hashCode方法

**智能特性**：
- ✅ boolean字段生成isXxx()方法
- ✅ 自动去重，不重复生成已存在的方法
- ✅ toString自动包含所有字段
- ✅ 跳过静态字段和final字段

#### 场景5：业务类 - 智能增强 💼
**识别规则**：包名包含 `service, controller, mapper, dao, handle, manager, handler, component, config, util, utils, debug, demo`

**操作**：在业务类中 → 按 `Cmd+Shift+D`

**生成内容**：
- ✅ SLF4J Logger字段
- ✅ serialVersionUID字段
- ✅ 字段智能排序（可选，默认禁用）

**字段排序特性**：
- 只排序实例字段，保护常量和静态字段
- 支持按名称、长度、类型排序
- 支持权限修饰符优先级排序
- 可在设置中启用/禁用

#### 场景6：选中包 - 批量生成 📦
**操作**：项目视图中选中包 → 按 `Cmd+Shift+D`

**功能**：
- ✅ 递归处理包内所有Java文件
- ✅ 显示确认对话框，避免误操作
- ✅ 提供详细的成功/失败统计
- ✅ 自动跳过接口和枚举类
- ✅ 支持选中多个包同时处理

**示例**：
```
src/main/java/com/example/model/
├── User.java
├── Order.java
└── Product.java

选中 model 包 → Cmd+Shift+D
→ 批量为所有类生成JavaBean方法
→ 显示：成功 3 个类，失败 0 个类
```

### 🔧 智能特性总结

#### 命名风格转换
- **循环切换**：小驼峰 → 大驼峰 → 下划线小写 → 下划线大写 → 小驼峰
- **静默执行**：无弹窗干扰，操作更流畅
- **自动识别**：智能识别当前命名风格
- **保持选中**：转换后保持文本选中状态

#### 常量生成
- **智能插入**：有LOGGER字段时插入到LOGGER下方，否则插入到类顶部
- **自动去重**：避免生成重复的常量字段
- **命名规范**：自动转换为大写下划线格式

#### DTO/VO/BO生成
- **目录结构**：自动创建dto/vo/bo子目录
- **包声明**：自动添加子包名
- **@Serial注解**：添加到serialVersionUID
- **JSON toString**：生成JSON格式的toString方法
- **BeanUtils支持**：可选择使用BeanUtils或原生getter/setter
- **boolean处理**：boolean字段生成isXxx()方法
- **方法排序**：toEntity → fromEntity → getter/setter → toString

#### 字段排序
- **只排序实例字段**：排除static、final、常量字段
- **保护重要字段**：LOGGER、serialVersionUID等保持原位置
- **智能识别常量**：全大写+下划线命名的字段不参与排序
- **可配置**：支持按名称、长度、类型排序，默认禁用

#### 批量处理
- **递归处理**：自动处理子包中的文件
- **确认对话框**：避免误操作
- **详细统计**：显示成功/失败数量
- **智能跳过**：自动跳过接口和枚举类

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
- **枚举生成优化**: 直接创建独立的枚举文件，而不是在当前文件中插入代码
- **文件自动打开**: 创建枚举文件后自动打开新文件
- **包名自动检测**: 新枚举文件自动继承当前文件的包名

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

### 3. ✅ 设置面板结构优化

#### 3.1 层级调整
- **OneClick Overview**: 移动到OneClick Settings下作为子页面
- **字段排序设置**: 移动到业务类设置中，仅对业务类生效
- **默认启用**: 字段排序功能默认开启，提升用户体验

#### 3.2 文字显示优化
- **换行修复**: 修复概览页面文字换行问题
- **HTML格式**: 使用HTML格式提升显示效果
- **智能说明**: 添加智能快捷键的详细说明
- ⚙️ 高级特性说明
- ⌨️ 完整的快捷键表格
- 🚀 使用方法指南
- 📋 代码模板库介绍

### 2. 🎉 欢迎通知
首次安装插件后，会显示欢迎通知，包含：
- 快速开始指南
- 常用快捷键提醒
- 设置面板入口
- 功能概览链接

### 3. 📖 概览页面
新增专门的概览页面 (`File → Settings → Tools → OneClick Overview`)：
- 功能特性详细介绍
- 动态快捷键表格（自动适配操作系统）
- 使用方法说明
- 代码模板分类展示

### 4. 🆘 帮助入口
多个帮助入口方便用户查看：
- 右键菜单中的"OneClick Help"
- Help菜单中的"OneClick Help"
- 设置面板中的概览页面

## 📋 项目视图集成

### OneClick菜单组
- **位置**: 项目视图右键菜单 → OneClick
- **功能**: 提供完整的OneClick工具集合
- **批量操作**: 支持选中多个文件或包进行批量处理

### 菜单项目
- **📦 批量生成**: 批量处理选中的文件或包
- **🛠️ 开发工具**: 在项目视图中也可使用开发工具
- **🗄️ 数据库工具**: 在项目视图中也可使用数据库工具

## ⌨️ 快捷键规范化 (Command+Shift)

### 统一快捷键规范
所有OneClick功能统一使用 `Command+Shift` 开头，提供更一致的用户体验：

| 功能 | Windows/Linux | macOS | 说明 |
|------|---------------|-------|------|
| **智能一键生成** | `Ctrl+Shift+D` | `Cmd+Shift+D` | 🎯 **核心功能**：智能识别场景并执行 |
| 开发工具集合 | `Ctrl+Shift+U` | `Cmd+Shift+U` | 🛠️ 20种开发工具：UUID、时间戳、枚举等 |
| 数据库工具 | `Ctrl+Shift+Y` | `Cmd+Shift+Y` | 🗄️ 数据库代码生成：Entity、SQL、Repository |
| 批量生成 | `Ctrl+Shift+B` | `Cmd+Shift+B` | 📦 批量处理多个文件或包 |
| 代码模板 | `Ctrl+Shift+T` | `Cmd+Shift+T` | 📝 15种设计模式和架构模板 |
| 重构助手 | `Ctrl+Shift+R` | `Cmd+Shift+R` | 🔄 10种重构操作 |
| 智能注释 | `Ctrl+Shift+C` | `Cmd+Shift+C` | 💬 自动生成注释和JavaDoc |
| 代码清理 | `Ctrl+Shift+L` | `Cmd+Shift+L` | 🧹 清理冗余代码和导入 |
| 代码分析 | `Ctrl+Shift+A` | `Cmd+Shift+A` | 📊 代码质量分析和统计 |
| 快速文档 | `Ctrl+Shift+Q` | `Cmd+Shift+Q` | 📖 生成README和API文档 |
| 折叠方法 | `Ctrl+Shift+F` | `Cmd+Shift+F` | 📁 折叠JavaBean方法 |

### 快捷键优势
- **统一规范**: 所有功能使用相同的修饰键组合
- **易于记忆**: Command+Shift+字母的简单模式
- **避免冲突**: 与IDE默认快捷键冲突更少
- **跨平台**: Windows和macOS自动适配

## 用户体验改进

### 🎨 视觉优化
- **HTML格式**: 插件描述使用HTML格式，支持标题、列表、表格
- **图标支持**: 使用emoji图标增强视觉效果
- **分类展示**: 功能按类别清晰分组
- **表格布局**: 快捷键使用表格形式，清晰易读

### 🔄 交互优化
- **启动通知**: 首次安装时显示欢迎信息
- **快速入口**: 通知中提供直接跳转到设置的按钮
- **帮助集成**: 在多个位置提供帮助入口
- **动态适配**: 根据操作系统动态调整显示内容

### 📱 响应式设计
- **滚动支持**: 概览页面支持滚动查看完整内容
- **自适应**: 根据窗口大小自动调整布局
- **字体优化**: 使用合适的字体大小和样式
- **间距控制**: 合理的间距提升阅读体验

## 技术实现

### 🏗️ 组件架构
- **PluginOverviewPanel**: 概览页面UI组件
- **PluginOverviewConfigurable**: 设置页面配置
- **OneClickStartupActivity**: 启动时通知组件
- **ShowOverviewAction**: 帮助Action

### 🌍 国际化支持
- **中英双语**: 所有新增内容都支持中英文
- **动态切换**: 语言切换后立即生效
- **完整覆盖**: 包括通知、设置页面、帮助文本

### 🔧 配置管理
- **通知组**: 注册专门的通知组
- **启动活动**: 项目启动时自动执行
- **版本控制**: 避免重复显示欢迎通知
- **设置集成**: 与现有设置系统无缝集成

## 使用建议

### 🚀 首次使用
1. 安装插件后查看欢迎通知
2. 点击"查看功能介绍"了解完整功能
3. 在设置中配置个人偏好
4. 使用快捷键快速访问功能

### 📖 获取帮助
1. **概览页面**: `File → Settings → Tools → OneClick Overview`
2. **帮助菜单**: `Help → OneClick Help`
3. **右键菜单**: 在Java文件中右键选择"OneClick Help"
4. **设置页面**: 查看详细配置选项

### ⌨️ 快捷键使用策略
1. **核心快捷键**: 记住 `Command+Shift+D` 智能一键生成
2. **工具快捷键**: `Command+Shift+U` 开发工具，`Command+Shift+Y` 数据库工具
3. **批量操作**: `Command+Shift+B` 批量生成，配合项目视图使用
4. **自定义设置**: 在设置中查看和修改快捷键配置

## 🎯 使用场景示例

### 场景1：字符串常量管理
```java
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public void validateUser() {
        // 选中 "INVALID_USER" → Command+Shift+D
        // 自动生成常量并插入到LOGGER下方
        throw new RuntimeException("INVALID_USER");
    }
}
```

### 场景2：命名风格统一
```java
// 选中 userService → Command+Shift+D → user_service
// 选中 user_name → Command+Shift+D → userName
// 静默转换，无弹窗干扰
```

### 场景3：JavaBean快速开发
```java
public class UserDTO {
    private Long id;
    private String username;
    private String email;

    // Command+Shift+D 一键生成所有JavaBean方法
}
```

### 场景4：业务类智能增强
```java
@Service
public class UserService {
    // Command+Shift+D 自动生成Logger、serialVersionUID
    // 同时对实例字段进行智能排序
}
```

### 场景5：枚举文件创建
```java
// Command+Shift+U → 生成枚举 → 输入枚举名
// 自动在同目录创建独立的枚举文件
```

现在OneClick插件提供了真正智能化的代码生成体验，让Java开发更加高效和便捷！🎉
