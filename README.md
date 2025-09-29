# OneClick - 智能代码生成器 🚀

OneClick 是一个功能强大的 IntelliJ IDEA 插件，专为 Java 开发者设计，提供全方位的智能代码生成、分析和管理功能。

![Plugin Version](https://img.shields.io/badge/version-1.0.0-blue)
![IntelliJ Platform](https://img.shields.io/badge/platform-IntelliJ%20IDEA-orange)
![Java](https://img.shields.io/badge/java-8%2B-green)
![License](https://img.shields.io/badge/license-MIT-brightgreen)

## 🌟 核心特性

### 🎯 智能代码生成
- **JavaBean 方法生成**：getter/setter/toString/equals/hashCode
- **代码模板生成器**：15种常用设计模式和架构模板
- **批量生成功能**：支持选中包或多个文件进行批量处理
- **内部类支持**：递归处理嵌套内部类，可配置处理深度

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

#### 1. JavaBean 方法生成
```java
public class User {
    private Long id;
    private String name;
    private String email;
    
    // 右键 → JavaBean Tools → Generate JavaBean Methods
    // 或使用快捷键 Ctrl+Alt+G (Windows/Linux) / Cmd+Alt+G (macOS)
}
```

#### 2. 批量生成
- 在项目视图中选中包或多个文件
- 右键 → Batch Generate JavaBean Methods
- 或使用快捷键 `Ctrl+Alt+B`

#### 3. 代码模板生成
- 使用快捷键 `Ctrl+Alt+T`
- 选择需要的模板类型（Singleton、Builder、REST Controller等）

## 📋 功能详览

### 🎨 代码生成功能

| 功能 | 快捷键 (Win/Linux) | 快捷键 (macOS) | 描述 |
|------|-------------------|----------------|------|
| JavaBean 方法生成 | `Ctrl+Alt+G` | `Cmd+Alt+G` | 生成getter/setter/toString等 |
| 批量生成 | `Ctrl+Alt+B` | `Cmd+Alt+B` | 批量处理多个文件 |
| 代码模板 | `Ctrl+Alt+T` | `Cmd+Alt+T` | 15种设计模式模板 |
| 重构助手 | `Ctrl+Alt+R` | `Cmd+Alt+R` | 10种重构操作 |
| 智能注释 | `Ctrl+Alt+C` | `Cmd+Alt+C` | 自动生成注释 |
| 代码清理 | `Ctrl+Alt+L` | `Cmd+Alt+L` | 清理冗余代码 |
| 代码分析 | `Ctrl+Alt+A` | `Cmd+Alt+A` | 统计分析代码 |
| 快速文档 | `Ctrl+Alt+D` | `Cmd+Alt+D` | 生成JavaDoc |
| 折叠方法 | `Ctrl+Alt+F` | `Cmd+Alt+F` | 折叠JavaBean方法 |

### 🏗️ 代码模板库

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

### 1. JavaBean 开发
```java
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime createTime;
    
    // 使用 OneClick 一键生成所有标准方法
}
```

### 2. 业务类开发
```java
@Service
public class UserService {
    // 自动生成 SLF4J Logger
    // 生成常用业务方法模板
}
```

### 3. 批量处理
- 选中整个包进行批量JavaBean方法生成
- 新项目快速搭建基础代码结构
- 重构现有项目的代码规范

### 4. 代码质量提升
- 定期使用代码分析功能检查代码质量
- 使用代码清理功能保持代码整洁
- 自动生成标准文档注释

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
