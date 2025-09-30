# 故障排除指南

## 🔧 已修复的问题

### 1. 右键菜单不显示
**问题**: 右键菜单中没有 "JavaBean Tools" 选项

**原因**: Action类的 `update()` 方法中使用了 `setVisible(false)`，导致菜单项被隐藏

**解决方案**: 
- ✅ 移除了 `setVisible()` 调用
- ✅ 保留 `setEnabled()` 来控制菜单项的启用状态
- ✅ 添加了 `popup="true"` 属性到菜单组

### 2. macOS快捷键无效
**问题**: `Cmd+Option+G` 和 `Cmd+Option+F` 快捷键不工作

**原因**: 
- 错误使用了 `cmd` 关键字（应该是 `meta`）
- 快捷键配置格式错误

**解决方案**:
- ✅ 使用正确的快捷键格式：`meta alt G`
- ✅ `meta` = Cmd键，`alt` = Option键
- ✅ 支持多个macOS keymap版本

## 🚀 安装新版本

### 步骤1: 卸载旧版本
1. 打开 IntelliJ IDEA
2. File → Settings → Plugins
3. 找到 "OneClick JavaBean Generator"
4. 点击卸载
5. 重启 IDEA

### 步骤2: 安装新版本
1. 下载最新的插件文件：`build/distributions/OneClick-1.2-SNAPSHOT.zip`
2. File → Settings → Plugins
3. 点击齿轮图标 → Install Plugin from Disk
4. 选择下载的zip文件
5. 重启 IDEA

## 🔍 验证安装

### 检查右键菜单
1. 打开任意Java文件
2. 右键点击编辑器
3. 应该能看到 **"JavaBean Tools"** 菜单项
4. 展开后应该有两个选项：
   - Generate JavaBean Methods
   - Fold JavaBean Methods

### 检查快捷键
1. 创建一个包含字段的Java类：
```java
public class TestClass {
    private String name;
    private int age;
}
```

2. 将光标放在类定义内
3. 按下快捷键：
   - **macOS**: `Cmd+Option+G`
   - **Windows/Linux**: `Ctrl+Alt+G`
4. 应该自动生成getter/setter/toString方法

### 检查主菜单
1. Code → Generate → 应该能看到 "Generate JavaBean Methods"
2. Code → 应该能看到 "Fold JavaBean Methods"

## ❗ 常见问题

### Q1: 快捷键冲突
**症状**: 按快捷键没有反应或触发了其他功能

**解决方案**:
1. 打开 IDEA 设置：`Cmd+,` (macOS) 或 `Ctrl+Alt+S` (Windows/Linux)
2. 搜索 "Keymap"
3. 搜索 "JavaBean" 查看快捷键设置
4. 如有冲突，重新分配快捷键

### Q2: 菜单项显示为灰色
**症状**: 右键菜单中能看到选项，但是灰色不可点击

**原因**: 不在Java文件中，或者没有选中类

**解决方案**:
1. 确保在 `.java` 文件中使用
2. 将光标放在类定义内部
3. 确保类包含实例字段

### Q3: 生成的方法格式不正确
**症状**: toString方法不是JSON格式，或者方法格式异常

**解决方案**:
1. 确保使用最新版本的插件
2. 检查字段类型是否支持
3. 重新安装插件

### Q4: 折叠功能不工作
**症状**: 按折叠快捷键没有反应

**解决方案**:
1. 确保类中有getter/setter/toString方法
2. 尝试先展开所有代码折叠，再使用插件折叠
3. 检查IDEA的代码折叠设置

## 🔧 高级故障排除

### 查看插件日志
1. Help → Show Log in Finder/Explorer
2. 查找包含 "OneClick" 或 "JavaBean" 的错误信息
3. 如果有错误，请提供日志信息

### 重置IDEA设置
如果问题持续存在：
1. 备份重要设置
2. File → Manage IDE Settings → Restore Default Settings
3. 重新安装插件

### 检查IDEA版本兼容性
确保您的IDEA版本在支持范围内：
- ✅ 支持：2023.2 - 2025.3
- ❌ 不支持：2023.1及更早版本

## 📞 获取帮助

如果问题仍未解决，请提供以下信息：
1. **IDEA版本**: Help → About 中的完整版本信息
2. **操作系统**: macOS/Windows/Linux 及版本
3. **具体操作**: 您尝试做什么
4. **期望结果**: 您期望发生什么
5. **实际结果**: 实际发生了什么
6. **错误信息**: 任何错误提示或日志

## ✅ 成功案例

插件应该在以下情况下正常工作：

### 示例Java类
```java
public class User {
    private String username;
    private int age;
    private boolean active;
    private List<String> roles;
}
```

### 使用插件后
1. **右键菜单**: JavaBean Tools → Generate JavaBean Methods
2. **快捷键**: `Cmd+Option+G` (macOS)
3. **结果**: 自动生成所有getter/setter方法和JSON格式的toString方法

### 折叠效果
使用 `Cmd+Option+F` 后，所有JavaBean方法会被折叠显示为：
- `getter: getUsername()`
- `setter: setUsername(String)`
- `toString(): String`
