# @Serial 注解配置 - 快速使用指南

## 🎯 一分钟快速上手

### 问题
生成 DTO/VO/BO 时出现 `@Serial` 注解，但你的项目使用 JDK 8，导致编译错误？

### 解决方案
**默认已经解决！** 插件现在默认不生成 `@Serial` 注解，完美兼容 JDK 8。

---

## 📋 使用场景

### 场景 1：JDK 8 项目（推荐配置）

**无需任何配置！** 插件默认设置已经兼容 JDK 8。

生成的代码示例：
```java
package com.example.entity.dto;

import java.io.Serializable;
import com.example.entity.User;

/**
 * User DTO 类
 * 自动生成的数据传输对象
 * 
 * @author OneClick Plugin
 * @date 2025/10/10
 */
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String email;
    
    // getter/setter methods...
}
```

✅ **特点：**
- 不包含 `import java.io.Serial;`
- `serialVersionUID` 前没有 `@Serial` 注解
- 可以在 JDK 8 环境下正常编译

---

### 场景 2：JDK 14+ 项目（可选配置）

如果你的项目使用 JDK 14 或更高版本，可以启用 `@Serial` 注解以符合现代 Java 规范。

#### 配置步骤：

1. **打开设置面板**
   - macOS: `IntelliJ IDEA` → `Preferences` → `Tools` → `OneClick Settings`
   - Windows/Linux: `File` → `Settings` → `Tools` → `OneClick Settings`

2. **找到配置项**
   - 滚动到 **DTO/VO/BO 生成设置** 部分
   - 勾选 ☑️ **生成 @Serial 注解（需要 JDK 14+，默认关闭兼容 JDK 8）**

3. **保存设置**
   - 点击 `Apply` 或 `OK`

#### 生成的代码示例：
```java
package com.example.entity.dto;

import java.io.Serializable;
import java.io.Serial;
import com.example.entity.User;

/**
 * User DTO 类
 * 自动生成的数据传输对象
 * 
 * @author OneClick Plugin
 * @date 2025/10/10
 */
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String email;
    
    // getter/setter methods...
}
```

✅ **特点：**
- 包含 `import java.io.Serial;`
- `serialVersionUID` 前有 `@Serial` 注解
- 符合 JDK 14+ 的最佳实践

---

## 🔧 如何生成 DTO/VO/BO

### 方法 1：使用快捷键（推荐）

1. 在实体类中，选中类名（如 `User`）
2. 按快捷键：
   - macOS: `Cmd + Shift + D`
   - Windows/Linux: `Ctrl + Shift + D`
3. 在弹出的菜单中选择 `DTO`、`VO` 或 `BO`
4. 插件会自动在 `dto`/`vo`/`bo` 子目录下生成对应的类

### 方法 2：使用右键菜单

1. 在实体类中右键
2. 选择 `OneClick` → `Generate DTO/VO/BO`
3. 选择要生成的类型

---

## ❓ 常见问题

### Q1: 我的项目是 JDK 8，需要修改配置吗？
**A:** 不需要！插件默认配置已经兼容 JDK 8，直接使用即可。

### Q2: 如何知道我的项目使用的 JDK 版本？
**A:** 
- 在 IntelliJ IDEA 中：`File` → `Project Structure` → `Project` → `Project SDK`
- 或者在终端运行：`java -version`

### Q3: 已经生成的代码包含 @Serial 注解，如何移除？
**A:** 
1. 手动删除 `@Serial` 注解
2. 删除 `import java.io.Serial;` 导入语句
3. 或者在设置中关闭该选项后重新生成

### Q4: 启用 @Serial 注解后，在 JDK 8 环境下编译失败怎么办？
**A:** 
1. 打开设置面板
2. 取消勾选"生成 @Serial 注解"
3. 重新生成 DTO/VO/BO 类

### Q5: 这个设置会影响已有的代码吗？
**A:** 不会。这个设置只影响新生成的 DTO/VO/BO 类，不会修改已有代码。

---

## 📊 JDK 版本对照表

| JDK 版本 | @Serial 支持 | 推荐配置 | 说明 |
|---------|-------------|---------|------|
| JDK 8   | ❌ 不支持    | 关闭    | 默认配置，无需修改 |
| JDK 11  | ❌ 不支持    | 关闭    | 保持默认配置 |
| JDK 14  | ✅ 支持      | 可选    | 可以启用以符合规范 |
| JDK 17  | ✅ 支持      | 可选    | 可以启用以符合规范 |
| JDK 21  | ✅ 支持      | 可选    | 可以启用以符合规范 |

---

## 💡 最佳实践

### JDK 8 项目
```java
// ✅ 推荐：不使用 @Serial 注解
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    // ...
}
```

### JDK 14+ 项目
```java
// ✅ 推荐：使用 @Serial 注解
import java.io.Serial;

public class UserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // ...
}
```

---

## 🎓 了解更多

### @Serial 注解的作用
- 标识序列化相关的字段和方法
- 帮助编译器检查序列化字段的正确性
- 提高代码可读性和维护性

### 为什么默认关闭？
- JDK 8 仍然是市场主流版本
- 默认关闭确保最大兼容性
- 避免用户在不知情的情况下遇到编译错误

---

## 📞 获取帮助

如果遇到问题，可以：
1. 查看插件设置面板的提示信息
2. 阅读完整文档：`SERIAL_ANNOTATION_FEATURE.md`
3. 查看实现细节：`IMPLEMENTATION_SUMMARY.md`

---

## ✨ 总结

- ✅ **JDK 8 用户**：无需任何配置，开箱即用
- ✅ **JDK 14+ 用户**：可选启用 @Serial 注解
- ✅ **灵活配置**：根据项目需求自由选择
- ✅ **向后兼容**：不影响已有代码

**享受愉快的编码体验！** 🚀

