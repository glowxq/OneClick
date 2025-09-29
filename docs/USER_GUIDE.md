# OneClick Code Generator 使用指南

## 📖 目录
- [快速开始](#快速开始)
- [设置配置](#设置配置)
- [功能详解](#功能详解)
- [常见问题](#常见问题)
- [最佳实践](#最佳实践)

## 🚀 快速开始

### 安装插件
1. 打开 IntelliJ IDEA
2. 进入 `File` → `Settings` → `Plugins`
3. 搜索 "OneClick Code Generator" 或从本地安装

### 第一次使用
1. 创建一个Java类，添加一些字段
2. 将光标放在类内部
3. 按 `Ctrl+Alt+G` (Windows/Linux) 或 `Cmd+Option+G` (macOS)
4. 查看生成的代码

## ⚙️ 设置配置

### 打开设置面板
1. **Windows/Linux**: `File` → `Settings` → `Tools` → `OneClick Code Generator`
2. **macOS**: `IntelliJ IDEA` → `Preferences` → `Tools` → `OneClick Code Generator`

### 设置选项详解

#### JavaBean生成设置
- **Generate separator comment**: 是否在业务方法和JavaBean方法之间添加分割注释
- **Generate getter/setter methods**: 是否生成getter和setter方法
- **Generate toString method**: 是否生成toString方法
- **Generate equals method**: 是否生成equals方法（预留功能）
- **Generate hashCode method**: 是否生成hashCode方法（预留功能）

#### 业务类设置
- **Generate logger field**: 是否为业务类生成日志字段
- **Logger field name**: 日志字段的名称，默认为"LOGGER"
- **Logger type**: 选择日志框架类型
  - `slf4j`: 使用SLF4J框架（推荐）
  - `log4j`: 使用Log4j框架
  - `jul`: 使用Java Util Logging

#### 通用设置
- **Auto detect class type**: 是否自动检测类类型
- **Use field comments**: 是否在生成的方法中使用字段注释（预留功能）
- **Generate serialVersionUID**: 是否为实现Serializable的类生成serialVersionUID

#### 代码风格设置
- **Generate fluent setters**: 是否生成fluent风格的setter方法（支持链式调用）
- **ToString style**: 选择toString方法的生成风格
  - `json`: JSON格式（默认）
  - `simple`: 简单格式
  - `apache`: Apache Commons ToStringBuilder风格

## 🔧 功能详解

### 智能类型检测

插件会根据以下特征自动检测类类型：

#### JavaBean类特征
- 包名包含：`entity`, `model`, `bean`, `pojo`, `dto`, `vo`
- 实现了`Serializable`接口
- 主要包含私有字段和少量业务方法
- 有`@Entity`, `@Table`等JPA注解

#### 业务类特征
- 包名包含：`service`, `controller`, `manager`, `handler`, `component`
- 有Spring注解：`@Service`, `@Controller`, `@Component`等
- 包含较多业务方法
- 字段较少，主要是依赖注入

### 代码生成规则

#### JavaBean类生成
1. **Getter方法**: 为所有非静态字段生成getter方法
2. **Setter方法**: 为所有非final字段生成setter方法
3. **ToString方法**: 根据设置选择不同风格
4. **分割注释**: 在业务方法和JavaBean方法之间添加分割线

#### 业务类生成
1. **日志字段**: 在类的第一行添加日志字段
2. **SerialVersionUID**: 为实现Serializable的类生成

### 插入位置规则
- **日志字段**: 插入到类的第一行（所有字段之前）
- **JavaBean方法**: 插入到业务方法之后，类结束之前
- **分割注释**: 在业务方法和JavaBean方法之间

## ❓ 常见问题

### Q: 为什么没有生成代码？
A: 请检查：
1. 光标是否在Java类内部
2. 类是否有字段（JavaBean生成需要字段）
3. 设置中是否启用了相应的生成选项

### Q: 生成的代码位置不对？
A: 插件会智能识别业务方法和JavaBean方法，将新生成的方法放在正确位置。如果位置不对，可能是类结构比较复杂，建议手动调整。

### Q: 如何避免重复生成？
A: 插件有重复检测机制：
- 相同的getter/setter方法不会重复生成
- toString方法会被替换为新的
- 日志字段不会重复添加

### Q: 支持哪些日志框架？
A: 目前支持：
- SLF4J (推荐)
- Log4j
- Java Util Logging (JUL)

### Q: 如何自定义日志字段名？
A: 在设置面板中修改"Logger field name"选项，默认为"LOGGER"。

## 💡 最佳实践

### 1. 包结构建议
```
com.example.project
├── model/entity     # JavaBean类
├── dto/vo          # 数据传输对象
├── service         # 业务服务类
├── controller      # 控制器类
└── manager         # 管理器类
```

### 2. 使用建议
- 为JavaBean类启用所有生成选项
- 为业务类启用日志字段生成
- 根据项目需要选择合适的toString风格
- 使用fluent setter提高代码可读性

### 3. 代码风格建议
- JSON toString适合调试和日志输出
- Simple toString适合简单的字符串表示
- Apache toString适合复杂对象的详细输出

### 4. 性能建议
- 大量字段的类建议关闭不必要的生成选项
- 频繁修改的类可以关闭自动检测，手动选择生成内容

## 🔄 更新日志

### v1.0.0
- 基础JavaBean方法生成
- 智能类型检测
- 可配置设置面板
- 多种toString风格
- 业务类日志字段生成
- Fluent setter支持
