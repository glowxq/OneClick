# @Serial 注解配置功能

## 问题背景

在生成 DTO/VO/BO 类时，插件默认会为 `serialVersionUID` 字段添加 `@Serial` 注解。但是：

- `@Serial` 注解是 JDK 14 引入的新特性
- 市面上大部分项目仍在使用 JDK 8
- 在 JDK 8 环境下使用 `@Serial` 注解会导致编译错误

## 解决方案

新增配置选项，允许用户自定义是否生成 `@Serial` 注解：

### 1. 默认行为
- **默认关闭** `@Serial` 注解生成，兼容 JDK 8
- 生成的代码示例：
```java
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    // ...
}
```

### 2. 启用 @Serial 注解
在设置面板中勾选"生成 @Serial 注解"选项后，生成的代码将包含 `@Serial` 注解：
```java
import java.io.Serial;

public class UserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // ...
}
```

## 配置方式

### 打开设置面板
1. macOS: `Preferences` → `Tools` → `OneClick Settings`
2. Windows/Linux: `Settings` → `Tools` → `OneClick Settings`

### 配置选项位置
在 **DTO/VO/BO 生成设置** 部分，找到：
- ☐ 生成 @Serial 注解（需要 JDK 14+，默认关闭兼容 JDK 8）
- ☐ Generate @Serial Annotation (Requires JDK 14+, disabled by default for JDK 8 compatibility)

## 技术实现

### 修改的文件

1. **OneClickSettings.java**
   - 添加配置项：`generateSerialAnnotation`（默认 `false`）
   - 添加 getter/setter 方法

2. **OneClickSettingsComponent.java**
   - 添加 UI 复选框组件
   - 添加国际化文本绑定

3. **OneClickSettingsConfigurable.java**
   - 添加配置同步逻辑

4. **GenerateJavaBeanMethodsAction.java**
   - 根据配置决定是否导入 `java.io.Serial`
   - 根据配置决定是否生成 `@Serial` 注解

5. **国际化资源文件**
   - `OneClickBundle.properties`（中文）
   - `OneClickBundle_en.properties`（英文）

### 代码逻辑

```java
// 获取设置
OneClickSettings settings = OneClickSettings.getInstance();

// 根据设置决定是否添加@Serial注解的导入
if (settings.isGenerateSerialAnnotation()) {
    imports.add("java.io.Serial");
}

// 根据设置决定是否添加@Serial注解
if (settings.isGenerateSerialAnnotation()) {
    sb.append("    @Serial\n");
}
sb.append("    private static final long serialVersionUID = 1L;\n\n");
```

## 使用建议

### JDK 8 项目（推荐）
- ✅ 保持默认设置（关闭 @Serial 注解）
- ✅ 确保生成的代码可以在 JDK 8 环境下编译

### JDK 14+ 项目
- ✅ 可以启用 @Serial 注解
- ✅ 符合现代 Java 编码规范
- ✅ 提供更好的序列化字段标识

## 兼容性说明

| JDK 版本 | @Serial 注解 | 推荐设置 |
|---------|-------------|---------|
| JDK 8   | ❌ 不支持    | 关闭    |
| JDK 11  | ❌ 不支持    | 关闭    |
| JDK 14+ | ✅ 支持      | 可选    |
| JDK 17+ | ✅ 支持      | 可选    |

## 测试验证

### 测试场景 1：默认配置（关闭 @Serial）
1. 打开设置，确认"生成 @Serial 注解"未勾选
2. 选择一个实体类，按 `Cmd+Shift+D`（macOS）或 `Ctrl+Shift+D`（Windows/Linux）
3. 选择生成 DTO/VO/BO
4. 验证生成的代码：
   - ✅ 不包含 `import java.io.Serial;`
   - ✅ `serialVersionUID` 字段前没有 `@Serial` 注解
   - ✅ 代码可以在 JDK 8 环境下编译

### 测试场景 2：启用 @Serial 注解
1. 打开设置，勾选"生成 @Serial 注解"
2. 选择一个实体类，生成 DTO/VO/BO
3. 验证生成的代码：
   - ✅ 包含 `import java.io.Serial;`
   - ✅ `serialVersionUID` 字段前有 `@Serial` 注解
   - ✅ 代码需要 JDK 14+ 环境编译

## 常见问题

### Q: 为什么默认关闭 @Serial 注解？
A: 为了最大化兼容性。大部分项目仍在使用 JDK 8，默认关闭可以避免编译错误。

### Q: 如何知道我的项目使用的 JDK 版本？
A: 
- 在 IDEA 中：`File` → `Project Structure` → `Project` → `Project SDK`
- 命令行：`java -version`

### Q: 已经生成的代码如何修改？
A: 
1. 如果需要移除 @Serial 注解：手动删除 `@Serial` 注解和 `import java.io.Serial;`
2. 如果需要添加 @Serial 注解：在设置中启用后重新生成

### Q: 这个设置会影响已有代码吗？
A: 不会。这个设置只影响新生成的 DTO/VO/BO 类，不会修改已有代码。

## 更新日志

### v1.2.0
- ✨ 新增：@Serial 注解可配置功能
- 🔧 修复：默认关闭 @Serial 注解，兼容 JDK 8
- 📝 文档：添加配置说明和使用建议

