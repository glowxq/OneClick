# 插件安装和测试指南

## 🔧 已修复的问题

### 问题分析
您遇到的问题是：
- ✅ `./gradlew runIde` 运行正常
- ❌ `./gradlew buildPlugin` 构建的插件安装后菜单项灰色禁用

### 根本原因
构建的插件和开发环境的类加载机制不同，导致PSI类型检查失败。

### 解决方案
1. ✅ **改进了类型检查**：使用文件名检查替代instanceof检查
2. ✅ **添加了异常处理**：防止未知错误导致菜单禁用
3. ✅ **增强了健壮性**：多重检查确保插件正常工作
4. ✅ **添加了测试Action**：用于验证插件基本功能

## 📦 安装新版本

### 步骤1: 完全卸载旧版本
1. 打开 IntelliJ IDEA
2. `File → Settings → Plugins`
3. 搜索 "OneClick" 或 "JavaBean"
4. 如果找到旧版本，点击 **卸载**
5. **重启 IDEA**

### 步骤2: 安装新版本
1. 下载插件文件：`build/distributions/OneClick-1.2-SNAPSHOT.zip`
2. 打开 IDEA：`File → Settings → Plugins`
3. 点击齿轮图标 ⚙️ → `Install Plugin from Disk...`
4. 选择 `OneClick-1.2-SNAPSHOT.zip` 文件
5. 点击 **OK**
6. **重启 IDEA**

### 步骤3: 验证安装
重启后检查以下内容：

#### 3.1 检查插件是否已安装
- `File → Settings → Plugins`
- 在 "Installed" 标签页搜索 "OneClick"
- 应该显示 "OneClick JavaBean Generator" 插件

#### 3.2 检查右键菜单
1. 创建或打开任意 `.java` 文件
2. 在编辑器中右键点击
3. 应该看到 **"JavaBean Tools"** 菜单项
4. 展开后应该有三个选项：
   - ✅ **Test Plugin** (测试用)
   - ✅ **Generate JavaBean Methods**
   - ✅ **Fold JavaBean Methods**

## 🧪 测试步骤

### 测试1: 基本功能测试
1. 在任意Java文件中右键
2. 选择 `JavaBean Tools → Test Plugin`
3. 应该弹出 "插件正常工作！" 的消息框
4. **如果这个测试失败，说明插件安装有问题**

### 测试2: 生成JavaBean方法
1. 创建一个测试类：
```java
public class TestUser {
    private String name;
    private int age;
    private boolean active;
}
```

2. 将光标放在类定义内（任意位置）
3. 右键选择 `JavaBean Tools → Generate JavaBean Methods`
4. 应该自动生成getter/setter/toString方法

### 测试3: 快捷键测试
1. 在上面的测试类中
2. 按快捷键：
   - **macOS**: `Cmd+Option+G`
   - **Windows/Linux**: `Ctrl+Alt+G`
3. 应该触发生成方法功能

### 测试4: 折叠功能测试
1. 在包含getter/setter方法的类中
2. 按快捷键：
   - **macOS**: `Cmd+Option+F`
   - **Windows/Linux**: `Ctrl+Alt+F`
3. 所有JavaBean方法应该被折叠

## ❗ 故障排除

### 问题1: 菜单项仍然灰色
**可能原因**:
- 不在Java文件中
- 插件未正确安装
- IDEA缓存问题

**解决方案**:
1. 确保在 `.java` 文件中测试
2. 尝试 "Test Plugin" 选项
3. 重启IDEA
4. 清除IDEA缓存：`File → Invalidate Caches and Restart`

### 问题2: 快捷键不工作
**可能原因**:
- 快捷键冲突
- 插件未正确注册

**解决方案**:
1. 检查快捷键设置：`File → Settings → Keymap`
2. 搜索 "JavaBean" 查看快捷键配置
3. 如有冲突，重新分配快捷键

### 问题3: 生成方法时出错
**可能原因**:
- 光标不在类定义内
- 类语法错误

**解决方案**:
1. 确保光标在类的大括号内
2. 检查类语法是否正确
3. 查看错误消息提示

### 问题4: 插件安装失败
**可能原因**:
- IDEA版本不兼容
- 插件文件损坏

**解决方案**:
1. 检查IDEA版本（需要2023.2+）
2. 重新下载插件文件
3. 尝试从命令行重新构建：`./gradlew clean buildPlugin`

## 🔍 调试信息

### 查看插件日志
如果遇到问题，可以查看日志：
1. `Help → Show Log in Finder` (macOS) 或 `Show Log in Explorer` (Windows)
2. 搜索包含 "OneClick" 或 "JavaBean" 的错误信息
3. 将错误信息提供给开发者

### 检查插件状态
1. `File → Settings → Plugins`
2. 找到 "OneClick JavaBean Generator"
3. 确保状态为 "Enabled"

### 验证IDEA版本
1. `Help → About`
2. 确保版本在 2023.2 - 2025.3 范围内
3. 当前测试版本：IU-252.25557.131 ✅

## 📋 功能清单

安装成功后，您应该能够使用以下功能：

### ✅ 右键菜单
- JavaBean Tools → Test Plugin
- JavaBean Tools → Generate JavaBean Methods  
- JavaBean Tools → Fold JavaBean Methods

### ✅ 快捷键
- 生成方法：`Cmd+Option+G` (macOS) / `Ctrl+Alt+G` (Windows/Linux)
- 折叠方法：`Cmd+Option+F` (macOS) / `Ctrl+Alt+F` (Windows/Linux)

### ✅ 主菜单
- Code → Generate → Generate JavaBean Methods
- Code → Fold JavaBean Methods

### ✅ 生成的内容
- Getter方法（支持boolean类型的is前缀）
- Setter方法
- JSON格式的toString方法
- 智能跳过已存在的方法

## 🎯 成功标志

如果以下测试都通过，说明插件安装成功：

1. ✅ "Test Plugin" 显示成功消息
2. ✅ 能够生成JavaBean方法
3. ✅ 快捷键正常响应
4. ✅ 折叠功能正常工作
5. ✅ 生成的toString方法是JSON格式

如果任何测试失败，请参考故障排除部分或提供具体的错误信息。
