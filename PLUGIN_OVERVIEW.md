# OneClick 插件功能完整指南 🚀

## 🎯 智能一键生成 (Command+Shift+D) - 核心功能

OneClick 插件的核心是智能一键生成功能，它会根据不同的使用场景自动选择最合适的操作。

### 📝 场景识别与智能处理

#### 场景一：选中文本智能处理
| 选中内容类型 | 自动操作 | 示例 |
|-------------|---------|------|
| 字符串字面量 | 生成常量字段 | `"USER_NOT_FOUND"` → `private static final String USER_NOT_FOUND = "USER_NOT_FOUND";` |
| 驼峰命名标识符 | 转为下划线命名 | `userService` → `user_service` |
| 下划线命名标识符 | 转为驼峰命名 | `user_name` → `userName` |

#### 场景二：类级别智能生成
| 类类型 | 识别规则 | 自动操作 |
|--------|---------|---------|
| JavaBean类 | 包名包含：model, entity, dto, vo, bean | 生成getter/setter/toString/equals/hashCode |
| 业务类 | 包名包含：service, controller, manager, handler | 生成Logger、serialVersionUID、字段排序 |

### 🔧 智能特性详解

#### 常量生成位置智能化
- **有LOGGER字段**：新常量插入到LOGGER下一行
- **无LOGGER字段**：新常量插入到类的最上面
- **重复检测**：避免生成重复的常量字段

#### 字段排序智能化
- **只排序实例字段**：排除static、final、常量字段
- **保护重要字段**：LOGGER、serialVersionUID等保持原位置
- **智能识别常量**：全大写+下划线命名的字段不参与排序

#### 命名转换智能化
- **静默执行**：无弹窗干扰，操作更流畅
- **双向转换**：智能识别当前命名风格并转换
- **保持选中**：转换后保持文本选中状态

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
| 开发工具集合 | `Ctrl+Shift+U` | `Cmd+Shift+U` | 🛠️ 20种开发工具 |
| 数据库工具 | `Ctrl+Shift+Y` | `Cmd+Shift+Y` | 🗄️ 数据库代码生成 |
| 批量生成 | `Ctrl+Shift+B` | `Cmd+Shift+B` | 📦 批量处理 |
| 代码模板 | `Ctrl+Shift+T` | `Cmd+Shift+T` | 📝 设计模式模板 |
| 重构助手 | `Ctrl+Shift+R` | `Cmd+Shift+R` | 🔄 重构操作 |
| 智能注释 | `Ctrl+Shift+C` | `Cmd+Shift+C` | 💬 自动注释 |
| 代码清理 | `Ctrl+Shift+L` | `Cmd+Shift+L` | 🧹 清理代码 |
| 代码分析 | `Ctrl+Shift+A` | `Cmd+Shift+A` | 📊 质量分析 |
| 快速文档 | `Ctrl+Shift+Q` | `Cmd+Shift+Q` | 📖 文档生成 |
| 折叠方法 | `Ctrl+Shift+F` | `Cmd+Shift+F` | 📁 方法折叠 |

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
