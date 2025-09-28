# OneClick JavaBean Generator - 功能特性

## 🎯 核心功能

### 1. 一键生成JavaBean方法
- ✅ 自动生成getter方法
- ✅ 自动生成setter方法  
- ✅ 自动生成JSON格式的toString方法
- ✅ 智能跳过已存在的getter/setter方法
- ✅ 自动删除并重新生成toString方法

### 2. 一键折叠JavaBean方法
- ✅ 折叠所有getter方法
- ✅ 折叠所有setter方法
- ✅ 折叠所有toString方法
- ✅ 显示简洁的方法签名占位符

## 🖱️ 多种访问方式

### 快捷键支持
| 平台 | 生成方法 | 折叠方法 |
|------|----------|----------|
| **Windows/Linux** | `Ctrl+Alt+G` | `Ctrl+Alt+F` |
| **macOS** | `Cmd+Option+G` | `Cmd+Option+F` |

### 右键菜单
在Java文件中右键点击 → **JavaBean Tools** → 选择功能：
- Generate JavaBean Methods
- Fold JavaBean Methods

### 主菜单
- **生成方法**: Code → Generate → Generate JavaBean Methods
- **折叠方法**: Code → Fold JavaBean Methods

## 🧠 智能特性

### 字段处理
- ✅ 只处理实例字段（非静态、非final）
- ✅ 支持所有Java基本类型和对象类型
- ✅ 正确处理boolean类型的getter方法（is前缀）

### 方法生成规则
- **Getter方法**:
  - 普通字段: `getFieldName()`
  - boolean字段: `isFieldName()` 或 `getFieldName()`
- **Setter方法**: `setFieldName(FieldType fieldName)`
- **toString方法**: JSON格式，支持字符串引号和其他类型

### 重复处理
- ✅ 不会重复生成已存在的getter/setter方法
- ✅ 每次都会重新生成toString方法（确保包含最新字段）

## 📱 跨平台兼容性

### 支持的操作系统
- ✅ Windows
- ✅ Linux  
- ✅ macOS

### 支持的IDEA版本
- ✅ IntelliJ IDEA 2023.2+
- ✅ IntelliJ IDEA Community Edition
- ✅ IntelliJ IDEA Ultimate Edition

## 🎨 用户体验

### 视觉反馈
- ✅ 操作完成后显示成功消息
- ✅ 错误情况下显示友好的错误提示
- ✅ 只在Java文件中显示菜单选项

### 折叠显示
- Getter方法: `getter: getFieldName()`
- Setter方法: `setter: setFieldName(FieldType)`
- toString方法: `toString(): String`

## 🔧 技术实现

### 代码生成
- 使用IntelliJ Platform API进行代码生成
- 支持代码格式化和自动导入
- 遵循JavaBean规范

### 性能优化
- 批量操作减少UI刷新
- 智能检测避免重复处理
- 内存友好的实现方式

## 📋 使用场景

### 适用情况
- ✅ 创建新的JavaBean类
- ✅ 为现有类添加新字段后更新方法
- ✅ 重构代码时需要重新生成toString方法
- ✅ 代码审查时需要折叠冗长的getter/setter方法

### 不适用情况
- ❌ 非Java文件
- ❌ 接口文件（没有实例字段）
- ❌ 抽象类（如果没有实例字段）

## 🚀 快速开始

1. **安装插件**
   ```bash
   ./gradlew buildPlugin
   ```

2. **在IDEA中加载插件**
   - 构建完成后在`build/distributions/`目录找到插件文件
   - 在IDEA中安装插件

3. **开始使用**
   - 创建Java类并添加字段
   - 使用快捷键或右键菜单生成方法
   - 使用折叠功能整理代码视图

## 💡 最佳实践

1. **字段命名**: 使用标准的驼峰命名法
2. **类型选择**: 优先使用包装类型而非基本类型
3. **代码组织**: 先定义字段，再生成方法
4. **版本控制**: 生成方法后及时提交代码

## 🔮 未来计划

- [ ] 支持自定义toString格式模板
- [ ] 支持生成equals和hashCode方法
- [ ] 支持注解驱动的代码生成
- [ ] 支持批量处理多个类文件
