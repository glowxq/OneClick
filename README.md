# OneClick - 智能代码生成器 🚀

一个快捷键 `Shift+Alt+D` (Windows/Linux) / `Cmd+Shift+D` (Mac)，智能识别场景，自动生成代码。

## ⭐ 核心亮点

### JSON格式toString方法（符合阿里规范）

**问题：** 阿里规范禁止使用JSON工具类转String打印日志，但JSON格式确实方便调试。

**解决方案：** 自动生成JSON格式的toString方法，无需JSON工具类，避免性能问题和异常风险。

```java
// 生成的toString方法
@Override
public String toString() {
    return "{" +
            "\"id\":" + id + ", " +
            "\"name\":\"" + name + "\", " +
            "\"active\":" + active +
            "}";
}

// 使用
User user = new User();
user.setId(1L);
user.setName("John");

// 输出：{"id":1, "name":"John"}
LOGGER.info("User: {}", user.toString());
```

**优势：**
- ✅ 符合阿里规范，不使用JSON工具类
- ✅ 日志打印直接得到JSON格式，方便调试
- ✅ 编译时生成，运行时无额外开销
- ✅ 类型安全，编译期检查

## 🎯 核心功能

### 智能一键生成 (Shift+Alt+D / Cmd+Shift+D)

一个快捷键，五大场景，智能识别，自动执行。

#### 1. 变量名命名风格转换
选中变量名后按快捷键，循环切换4种命名风格：
- 小驼峰 → 大驼峰 → 下划线小写 → 下划线大写 → 小驼峰

```java
// 选中 userName → 按 Shift+Alt+D (Windows/Linux) 或 Cmd+Shift+D (Mac)
userName → UserName → user_name → USER_NAME → userName
```

#### 2. 生成常量字段
选中字符串字面量后按快捷键，自动生成常量字段。

```java
// 选中 "USER_NOT_FOUND" → 按 Shift+Alt+D (Windows/Linux) 或 Cmd+Shift+D (Mac)
// 自动生成：private static final String USER_NOT_FOUND = "USER_NOT_FOUND";
```

#### 3. 生成DTO/VO/BO类
选中类名后按快捷键，选择类型后自动生成对应的数据传输对象类。

```java
// 选中类名 "User" → 按 Shift+Alt+D (Windows/Linux) 或 Cmd+Shift+D (Mac) → 选择 "DTO"
// 自动生成 com/example/entity/dto/UserDTO.java
// 包含：getter/setter、toEntity()、fromEntity()、JSON格式toString()
```

#### 4. 生成JavaBean方法（⭐ JSON格式toString）
在包含私有字段的Java类中按快捷键，自动生成 getter/setter/toString 方法。

```java
public class User {
    private Long id;
    private String username;
    private boolean active;
    
    // 按 Shift+Alt+D (Windows/Linux) 或 Cmd+Shift+D (Mac) 自动生成：
    // - getter/setter方法
    // - JSON格式的toString方法
}
```

**生成的toString方法：**
```java
@Override
public String toString() {
    return "{" +
            "\"id\":" + id + ", " +
            "\"username\":\"" + username + "\", " +
            "\"active\":" + active +
            "}";
}
```

#### 5. 生成枚举类parse方法
在包含 code 字段的枚举类中按快捷键，自动生成 parse 方法。

```java
public enum UserStatus {
    ACTIVE(1, "激活"),
    INACTIVE(0, "未激活");
    
    private final Integer code;
    
    // 按 Shift+Alt+D (Windows/Linux) 或 Cmd+Shift+D (Mac) 自动生成parse方法
    public static UserStatus parse(Integer code) {
        // ...
    }
}
```

## 🚀 快速开始

### 安装
```
File → Settings → Plugins → 搜索 "OneClick" → Install
```

### 使用
1. 在Java类中按 `Shift+Alt+D` (Windows/Linux) 或 `Cmd+Shift+D` (Mac)
2. 选中变量名后按快捷键：循环切换命名风格
3. 选中字符串后按快捷键：生成常量字段
4. 选中类名后按快捷键：生成DTO/VO/BO类
5. 在枚举类中按快捷键：生成parse方法

## ⚙️ 配置

### 基本设置
```
File → Settings → Tools → OneClick Settings
```

- **JavaBean设置**：生成getter/setter/toString等
- **枚举类设置**：自定义parse方法名和code字段名
- **DTO/VO/BO生成设置**：配置BeanUtils类和转换方式
- **快捷键设置**：自定义快捷键（默认：Shift+Alt+D (Windows/Linux) 或 Cmd+Shift+D (Mac)）

### 快捷键自定义
1. 打开 `File → Settings → Tools → OneClick Settings → Keymap Settings`
2. 输入自定义快捷键（如：`Ctrl+Shift+G`）
3. 输入后自动验证格式，格式正确显示 ✅
4. 点击"Apply"保存

**支持的格式：**
- `Ctrl+Shift+D` (Windows/Linux)
- `Cmd+Shift+D` (macOS)
- `Alt+Shift+D`

## 📋 功能列表

| 场景 | 操作 | 结果 |
|------|------|------|
| 变量名转换 | 选中变量名 → Shift+Alt+D (Windows/Linux) 或 Cmd+Shift+D (Mac) | 循环切换命名风格 |
| 常量生成 | 选中字符串 → Shift+Alt+D (Windows/Linux) 或 Cmd+Shift+D (Mac) | 生成常量字段 |
| DTO/VO/BO | 选中类名 → Shift+Alt+D (Windows/Linux) 或 Cmd+Shift+D (Mac) | 生成数据传输对象类 |
| JavaBean方法 | 在JavaBean类中 → Shift+Alt+D (Windows/Linux) 或 Cmd+Shift+D (Mac) | 生成getter/setter/toString（JSON格式） |
| 枚举parse | 在枚举类中 → Shift+Alt+D (Windows/Linux) 或 Cmd+Shift+D (Mac) | 生成parse方法 |

## 🎯 使用场景

### 日志打印（符合阿里规范）
```java
// 生成JSON格式toString，无需JSON工具类
User user = new User();
LOGGER.info("User: {}", user.toString());
// 输出：{"id":1, "name":"John"}
```

### DTO/VO/BO快速生成
```java
// 选中类名 → Shift+Alt+D (Windows/Linux) 或 Cmd+Shift+D (Mac) → 选择类型
// 自动生成完整的DTO类，包含JSON格式toString
```

### 命名风格统一
```java
// 数据库字段转Java字段
user_name → userName (选中后按快捷键)

// Java字段转数据库字段
userName → user_name (选中后按快捷键)
```

## 🔧 开发

### 环境要求
- IntelliJ IDEA 2020.3+
- Java 8+
- Gradle 7.0+

### 本地开发
```bash
git clone https://github.com/glowxq/OneClick.git
cd OneClick
./gradlew build
./gradlew runIde
```

## 📄 许可证

Apache License 2.0

## 📞 联系方式

- **作者**：glowxq
- **邮箱**：glowxq@qq.com
- **GitHub**：https://github.com/glowxq

---

⭐ **如果这个项目对您有帮助，请给我们一个 Star！** ⭐
