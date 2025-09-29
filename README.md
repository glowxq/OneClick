# OneClick - 智能代码生成器 🚀

OneClick 是一个功能强大的 IntelliJ IDEA 插件，专为 Java 开发者设计，提供全方位的智能代码生成、分析和管理功能。

![Plugin Version](https://img.shields.io/badge/version-1.0.0-blue)
![IntelliJ Platform](https://img.shields.io/badge/platform-IntelliJ%20IDEA-orange)
![Java](https://img.shields.io/badge/java-8%2B-green)
![License](https://img.shields.io/badge/license-MIT-brightgreen)

## 🌟 核心特性

### 🎯 智能一键生成 (Command+Shift+D)
OneClick 的核心功能是智能一键生成，它会根据不同的使用场景自动选择最合适的操作：

#### 📝 选中文本场景
- **字符串常量生成**：选中字符串 → 自动生成常量字段（插入到LOGGER下方）
- **命名风格转换**：选中标识符 → 智能切换驼峰/下划线命名
  - `userService` ↔ `user_service`
  - `userName` ↔ `user_name`

#### 🏗️ 类级别场景
- **JavaBean类**：自动生成 getter/setter/toString/equals/hashCode 方法
- **业务类**：生成 Logger 字段、serialVersionUID、字段排序等
- **智能识别**：根据包名和类特征自动判断类类型

#### 🔧 其他强大功能
- **开发工具集合** (Command+Shift+U)：UUID生成、时间戳、枚举创建等20种实用工具
- **数据库工具** (Command+Shift+Y)：Entity注解、SQL语句、Repository生成等
- **批量生成功能**：支持选中包或多个文件进行批量处理
- **项目视图集成**：右键菜单中的OneClick工具组

### 🔧 代码重构助手
- **10种重构操作**：提取常量、转换Stream API、添加空值检查等
- **智能注释生成**：根据代码上下文自动生成中文注释
- **代码清理助手**：移除未使用导入、空行、调试代码等
- **快速文档生成**：自动生成标准JavaDoc文档

### 📊 代码分析工具
- **详细统计分析**：行数、复杂度、方法长度等指标
- **代码质量检测**：长方法、大类、TODO注释统计
- **可视化报告**：直观的分析结果展示

### ⚙️ 高度可定制
- **快捷键自定义**：支持不同操作系统的快捷键配置
- **多语言支持**：完整的中英双语界面
- **灵活配置**：丰富的设置选项满足不同需求

## 🚀 快速开始

### 安装方式

1. **通过 IntelliJ IDEA 插件市场**：
   ```
   File → Settings → Plugins → 搜索 "OneClick" → Install
   ```

2. **手动安装**：
   - 下载最新的 [Release](https://github.com/glowxq/OneClick/releases)
   - `File → Settings → Plugins → ⚙️ → Install Plugin from Disk...`

### 基本使用

#### 1. 智能一键生成 (Command+Shift+D)

**场景一：选中字符串生成常量**
```java
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public void processUser() {
        // 选中 "USER_NOT_FOUND" → 按 Command+Shift+D
        throw new RuntimeException("USER_NOT_FOUND");
        // 自动生成常量：private static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    }
}
```

**场景二：选中标识符切换命名风格**
```java
// 选中 userService → 按 Command+Shift+D → 变为 user_service
// 选中 user_name → 按 Command+Shift+D → 变为 userName
```

**场景三：JavaBean类自动生成方法**
```java
public class User {
    private Long id;
    private String name;
    private String email;

    // 按 Command+Shift+D 自动生成所有JavaBean方法
}
```

**场景四：业务类生成Logger和工具**
```java
@Service
public class UserService {
    // 按 Command+Shift+D 自动生成Logger、serialVersionUID等
    // 同时对字段进行智能排序
}
```

#### 2. 批量生成 (Command+Shift+B)
- 在项目视图中选中包或多个文件
- 右键 → OneClick → 批量生成
- 或使用快捷键 `Command+Shift+B`

#### 3. 开发工具集合 (Command+Shift+U)
- UUID生成、时间戳插入、枚举文件创建
- 常量生成、Builder模式、JSON模板等

## 📋 功能详览

### 🎨 代码生成功能

| 功能 | 快捷键 (Win/Linux) | 快捷键 (macOS) | 描述 |
|------|-------------------|----------------|------|
| **智能一键生成** | `Ctrl+Shift+D` | `Cmd+Shift+D` | 🎯 **核心功能**：智能识别场景并执行相应操作 |
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

### 🎯 智能一键生成详解

OneClick 的核心功能是智能一键生成 (`Command+Shift+D`)，它会根据当前上下文智能选择操作：

#### 📝 文本选中场景
| 选中内容 | 操作结果 | 示例 |
|---------|---------|------|
| 字符串字面量 | 生成常量字段 | `"USER_NOT_FOUND"` → 生成 `private static final String USER_NOT_FOUND = "USER_NOT_FOUND";` |
| 驼峰命名标识符 | 转为下划线命名 | `userService` → `user_service` |
| 下划线命名标识符 | 转为驼峰命名 | `user_name` → `userName` |

#### 🏗️ 类级别场景
| 类类型 | 操作结果 | 识别规则 |
|--------|---------|---------|
| JavaBean类 | 生成getter/setter/toString/equals/hashCode | 包名包含：model, entity, dto, vo, bean |
| 业务类 | 生成Logger、serialVersionUID、字段排序 | 包名包含：service, controller, manager, handler |

#### 🔧 特殊功能
- **字段排序**：业务类中自动按字母顺序排列实例字段（排除常量和静态字段）
- **常量位置**：新生成的常量自动插入到LOGGER字段下方
- **智能检测**：根据包名、类名、注解等多维度判断类类型

### 🛠️ 开发工具集合 (Command+Shift+U)

#### 代码生成工具
- **🔧 生成UUID** - 生成随机UUID字符串
- **📅 插入时间戳** - 插入当前时间戳
- **🔍 生成序列化ID** - 生成serialVersionUID
- **📝 生成TODO注释** - 生成带时间和作者的TODO注释
- **🎯 生成测试方法** - 生成单元测试方法模板

#### 代码转换工具
- **🔒 生成常量** - 将选中文本转换为常量定义
- **📊 生成枚举** - 创建新的枚举文件（不在当前文件中）
- **🌐 生成JSON模板** - 生成JSON数据模板
- **🔄 转换命名风格** - 转换驼峰/下划线命名
- **📋 生成Builder模式** - 为当前类生成Builder模式

### 🗄️ 数据库工具集合 (Command+Shift+Y)

#### JPA/Hibernate工具
- **🗃️ 生成Entity注解** - 为实体类添加JPA注解
- **📝 生成SQL建表语句** - 根据实体类生成CREATE TABLE语句
- **🔍 生成Repository接口** - 生成Spring Data JPA Repository

#### MyBatis工具
- **📊 生成MyBatis Mapper** - 生成MyBatis Mapper接口和XML
- **🔄 生成数据转换器** - 生成Entity与DTO转换器

#### Spring Boot集成
- **📋 生成CRUD Service** - 生成完整的CRUD Service类
- **🌐 生成REST Controller** - 生成RESTful API控制器
- **🔧 生成数据库配置** - 生成数据源配置类

### 📝 代码模板库 (Command+Shift+T)

#### 设计模式
- **Singleton Pattern** - 单例模式
- **Builder Pattern** - 建造者模式
- **Factory Pattern** - 工厂模式
- **Observer Pattern** - 观察者模式
- **Strategy Pattern** - 策略模式

#### 架构层模板
- **REST Controller** - RESTful控制器
- **Service Layer** - 服务层
- **Repository Layer** - 数据访问层
- **Exception Handler** - 异常处理器

#### 工具类模板
- **Validation Utils** - 验证工具类
- **Date Utils** - 日期工具类
- **String Utils** - 字符串工具类
- **File Utils** - 文件工具类
- **JSON Utils** - JSON工具类
- **Test Class** - 测试类模板

### 🔄 重构操作

1. **Extract Constants** - 提取常量
2. **Convert to Stream API** - 转换为Stream API
3. **Add Null Checks** - 添加空值检查
4. **Extract Method** - 提取方法
5. **Inline Variables** - 内联变量
6. **Convert to Lambda** - 转换为Lambda表达式
7. **Add Logging** - 添加日志记录
8. **Add Documentation** - 添加文档注释
9. **Optimize Imports** - 优化导入
10. **Format Code** - 格式化代码

### 🧹 代码清理功能

- **移除未使用的导入** - 清理无用import语句
- **移除空行** - 清理多余的空白行
- **移除未使用的变量** - 清理未使用的局部变量
- **移除TODO注释** - 清理TODO/FIXME注释
- **格式化代码** - 统一代码格式
- **优化导入顺序** - 按规范排序import
- **移除多余空格** - 清理多余的空白字符
- **统一换行符** - 标准化行结束符
- **移除调试代码** - 清理System.out.println等调试语句

### 📊 代码分析指标

#### 基础统计
- 总行数、代码行数、注释行数、空白行数
- 类数量、方法数量、字段数量、变量数量
- 导入语句数量

#### 复杂度分析
- 圈复杂度计算
- 方法长度统计（最短/最长/平均）
- 类大小统计

#### 质量指标
- 长方法检测（>20行）
- 大类检测（>200行）
- TODO注释统计
- 空catch块检测

## ⚙️ 配置选项

### 基本设置
```
File → Settings → Tools → OneClick
```

- **语言选择**：中文/English
- **类型检测**：自动识别JavaBean vs 业务类
- **生成选项**：选择要生成的方法类型

### 内部类设置
- **处理内部类**：是否递归处理内部类
- **最大深度**：内部类处理的最大层数（1-10）
- **分隔注释**：为内部类生成分隔注释

### 快捷键设置
```
File → Settings → Tools → OneClick → Keymap Settings
```
- 自定义所有功能的快捷键
- 支持不同操作系统
- 快捷键冲突检测

### toString 配置
- **风格选择**：JSON/简单/Apache Commons
- **字段包含**：选择包含的字段类型
- **格式化选项**：缩进、换行等

## 🎯 使用场景

### 1. 智能文本处理
```java
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public void validateUser() {
        // 场景1：选中字符串 "INVALID_USER" → Command+Shift+D
        // 自动生成：private static final String INVALID_USER = "INVALID_USER";
        if (user == null) {
            throw new RuntimeException("INVALID_USER");
        }

        // 场景2：选中 userService → Command+Shift+D → 变为 user_service
        // 场景3：选中 user_name → Command+Shift+D → 变为 userName
    }
}
```

### 2. JavaBean 快速开发
```java
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime createTime;

    // 按 Command+Shift+D 一键生成所有标准方法
    // 自动识别为JavaBean类，生成getter/setter/toString/equals/hashCode
}
```

### 3. 业务类智能增强
```java
@Service
public class UserService {
    // 按 Command+Shift+D 自动生成：
    // 1. SLF4J Logger字段
    // 2. serialVersionUID
    // 3. 字段自动排序（只排序实例字段，不影响常量）

    private UserRepository userRepository;
    private EmailService emailService;
    private ValidationService validationService;
}
```

### 4. 项目级批量处理
- **项目视图操作**：右键包或文件 → OneClick → 批量生成
- **多文件处理**：选中多个Java文件进行批量JavaBean方法生成
- **新项目搭建**：快速为整个项目生成基础代码结构
- **代码规范统一**：批量应用代码规范和最佳实践

### 5. 开发效率提升
- **枚举文件创建**：Command+Shift+U → 生成枚举 → 自动创建独立枚举文件
- **数据库代码生成**：Command+Shift+Y → 一键生成Entity、Repository、Service
- **常量管理**：智能常量生成，自动插入到合适位置（LOGGER下方）
- **命名规范**：一键转换不同命名风格，保持代码一致性

## 🔧 开发指南

### 环境要求
- IntelliJ IDEA 2020.3+
- Java 8+
- Gradle 7.0+

### 本地开发
```bash
# 克隆项目
git clone https://github.com/glowxq/OneClick.git

# 进入项目目录
cd OneClick

# 构建项目
./gradlew build

# 运行开发环境
./gradlew runIde
```

### 项目结构
```
OneClick/
├── src/main/java/com/glowxq/plugs/
│   ├── actions/          # Action实现
│   ├── settings/         # 设置面板
│   ├── utils/           # 工具类
│   └── icons/           # 图标资源
├── src/main/resources/
│   ├── META-INF/        # 插件配置
│   ├── messages/        # 国际化文件
│   └── icons/           # 图标文件
└── docs/                # 文档
```

## 🤝 贡献指南

我们欢迎各种形式的贡献！

### 如何贡献
1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

### 贡献类型
- 🐛 Bug 修复
- ✨ 新功能开发
- 📝 文档改进
- 🎨 UI/UX 优化
- ⚡ 性能优化
- 🧪 测试用例

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 🐛 问题反馈

遇到问题？我们来帮您解决！

### 反馈渠道
1. 📋 [GitHub Issues](https://github.com/glowxq/OneClick/issues)
2. 📧 邮箱：glowxq@qq.com
3. 💬 [讨论区](https://github.com/glowxq/OneClick/discussions)

## 📞 联系方式

- **作者**：glowxq
- **邮箱**：glowxq@qq.com
- **GitHub**：[https://github.com/glowxq](https://github.com/glowxq)

## 🙏 致谢

感谢所有为 OneClick 项目做出贡献的开发者和用户！

### 特别感谢
- IntelliJ Platform SDK 团队
- 所有提供反馈和建议的用户
- 开源社区的支持

## 📈 版本历史

### v1.0.0 (2024-09-29)
- 🎉 首次发布
- ✨ 完整的JavaBean方法生成功能
- 🔧 代码模板生成器（15种模板）
- 🔄 重构助手（10种操作）
- 🧹 代码清理助手
- 📊 代码分析工具
- 📝 快速文档生成
- ⚙️ 完整的设置面板
- 🌍 中英双语支持
- ⌨️ 自定义快捷键
- 📦 批量处理功能

---

⭐ **如果这个项目对您有帮助，请给我们一个 Star！您的支持是我们持续改进的动力！** ⭐
