# macOS 快捷键说明

## 🍎 macOS 专用快捷键

### 为什么使用 Option 键？

在 macOS 系统中：
- **没有 Alt 键**，对应的是 **Option 键** (⌥)
- **Cmd 键** (⌘) 对应 Windows/Linux 的 Ctrl 键
- IntelliJ IDEA 在内部将 Option 键映射为 `alt`

### 快捷键对照表

| 功能 | Windows/Linux | macOS 显示 | macOS 实际按键 |
|------|---------------|-------------|----------------|
| 生成JavaBean方法 | `Ctrl+Alt+G` | `Cmd+Option+G` | ⌘ + ⌥ + G |
| 折叠JavaBean方法 | `Ctrl+Alt+F` | `Cmd+Option+F` | ⌘ + ⌥ + F |

### 键盘符号说明

- **⌘** = Command 键 (Cmd)
- **⌥** = Option 键 (相当于 Windows 的 Alt)
- **⌃** = Control 键 (很少使用)
- **⇧** = Shift 键

## 🔧 技术实现

在 IntelliJ IDEA 插件配置中：

```xml
<!-- macOS快捷键配置 -->
<keyboard-shortcut keymap="Mac OS X" first-keystroke="meta alt G"/>
<keyboard-shortcut keymap="Mac OS X 10.5+" first-keystroke="meta alt G"/>
```

其中：
- `meta` = Cmd 键 (⌘)
- `alt` = Option 键 (⌥)

## 📱 在 IDEA 中的显示

当您在 macOS 版本的 IntelliJ IDEA 中查看快捷键时，会显示为：
- `⌘⌥G` - 生成JavaBean方法
- `⌘⌥F` - 折叠JavaBean方法

## 🎯 使用建议

1. **记忆方法**: 
   - Cmd 替代 Ctrl
   - Option 替代 Alt
   - 其他键保持不变

2. **查看快捷键**: 
   - 在 IDEA 中按 `⌘,` 打开设置
   - 搜索 "JavaBean" 查看具体快捷键

3. **自定义快捷键**:
   - 如果不习惯默认快捷键，可以在 IDEA 设置中自定义
   - Preferences → Keymap → 搜索 "JavaBean"

## ✅ 验证方法

安装插件后，您可以通过以下方式验证快捷键是否正常工作：

1. 打开一个 Java 文件
2. 创建一个包含字段的类
3. 按下 `⌘⌥G` 应该能生成方法
4. 按下 `⌘⌥F` 应该能折叠方法

如果快捷键不工作，请检查：
- 是否有其他插件占用了相同快捷键
- IDEA 版本是否兼容 (需要 2023.2+)
- 是否在 Java 文件中使用
