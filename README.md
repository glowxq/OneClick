# OneClick - 智能代码生成器 🚀

OneClick 是一个专为 Java 开发者设计的 IntelliJ IDEA 插件，通过一个快捷键 `Shift+Alt+D` (Mac: `Cmd+Shift+D`) 智能识别场景并自动生成代码。

![Plugin Version](https://img.shields.io/badge/version-1.0.0-blue)
![IntelliJ Platform](https://img.shields.io/badge/platform-IntelliJ%20IDEA-orange)
![Java](https://img.shields.io/badge/java-8%2B-green)

## ✨ 核心功能

### 🎯 智能一键生成 (Shift+Alt+D)

一个快捷键，五大场景，智能识别，自动执行！

#### 场景1：变量名命名风格转换
选中变量名后按快捷键，循环切换4种命名风格：
- **小驼峰** → **大驼峰** → **下划线小写** → **下划线大写** → **小驼峰**

**示例：**
```java
// 选中 userName → 按 Shift+Alt+D
userName → UserName → user_name → USER_NAME → userName

// 选中 emailAddress → 按 Shift+Alt+D
emailAddress → EmailAddress → email_address → EMAIL_ADDRESS → emailAddress
```

#### 场景2：生成常量字段
选中字符串字面量后按快捷键，自动生成常量字段。

**示例：**
```java
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    
    public void validateUser() {
        // 选中 "USER_NOT_FOUND" → 按 Shift+Alt+D
        // 自动生成常量（插入到LOGGER下方）：
        // private static final String USER_NOT_FOUND = "USER_NOT_FOUND";
        if (user == null) {
            throw new RuntimeException("USER_NOT_FOUND");
        }
    }
}
```

#### 场景3：生成DTO/VO/BO类
选中类名后按快捷键，选择类型后自动生成对应的数据传输对象类。

**示例：**
```java
// 源实体类
package com.example.entity;

public class User {
    private Long id;
    private String name;
    private String email;
    private boolean active;
}

// 选中类名 "User" → 按 Shift+Alt+D → 选择 "DTO"
// 自动生成 com/example/entity/dto/UserDTO.java
```

**生成的UserDTO.java：**
```java
package com.example.entity.dto;

import java.io.Serializable;
import java.io.Serial;
import com.example.entity.User;
import org.springframework.beans.BeanUtils;

public class UserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String email;
    private boolean active;

    // 转换为实体类
    public User toEntity() {
        User entity = new User();
        BeanUtils.copyProperties(this, entity);
        return entity;
    }

    // 从实体类转换
    public static UserDTO fromEntity(User entity) {
        if (entity == null) return null;
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    // getter/setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isActive() { return active; }  // boolean字段使用isXxx()
    public void setActive(boolean active) { this.active = active; }

    // ⭐ JSON格式的toString方法（亮点功能）
    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id + ", " +
                "\"name\":\"" + name + "\", " +
                "\"email\":\"" + email + "\", " +
                "\"active\":" + active +
                "}";
    }
}
```

#### 场景4：生成JavaBean方法（⭐ JSON格式toString亮点）

在包含私有字段的Java类中按快捷键，自动生成 getter/setter/toString 方法。

**⭐ 亮点：toString方法生成JSON格式，方便日志打印和调试！**

**示例：**
```java
package com.example.model;

public class User {
    private Long id;
    private String username;
    private String email;
    private boolean active;
    private LocalDateTime createTime;

    // 按 Shift+Alt+D 自动生成所有标准方法
}
```

**生成的方法：**
```java
// getter/setter方法
public Long getId() { return id; }
public void setId(Long id) { this.id = id; }
// ... 其他getter/setter

// ⭐ JSON格式的toString方法（亮点功能）
@Override
public String toString() {
    return "{" +
            "\"id\":" + id + ", " +
            "\"username\":\"" + username + "\", " +
            "\"email\":\"" + email + "\", " +
            "\"active\":" + active + ", " +
            "\"createTime\":\"" + createTime + "\"" +
            "}";
}
```

**使用场景：**
```java
// 日志打印时，直接使用toString()即可得到JSON格式
User user = new User();
user.setId(1L);
user.setUsername("john");
user.setEmail("john@example.com");
user.setActive(true);

// 输出：{"id":1, "username":"john", "email":"john@example.com", "active":true}
LOGGER.info("User info: {}", user.toString());

// 符合阿里规范：不使用JSON工具类，避免性能问题和异常风险
// 同时获得JSON格式的便利性
```

**为什么JSON格式toString是亮点？**
- ✅ **符合阿里规范**：不使用JSON工具类（如Fastjson、Jackson），避免性能问题和异常风险
- ✅ **方便调试**：日志打印时直接得到JSON格式，便于查看和分析
- ✅ **性能优化**：编译时生成，运行时无额外开销
- ✅ **类型安全**：编译期检查，避免运行时异常

#### 场景5：生成枚举类parse方法
在包含 code 字段的枚举类中按快捷键，自动生成 parse 方法。

**示例：**
```java
public enum UserStatus {
    ACTIVE(1, "激活"),
    INACTIVE(0, "未激活"),
    DELETED(-1, "已删除");

    private final Integer code;
    private final String desc;

    UserStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // 按 Shift+Alt+D 自动生成parse方法
    /**
     * 根据code解析对应的枚举值
     * 
     * @param code 枚举的code值
     * @return 对应的枚举值，如果未找到则返回null
     */
    public static UserStatus parse(Integer code) {
        if (code == null) {
            return null;
        }
        for (UserStatus value : values()) {
            if (value.code != null && value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
```

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

1. **在Java类中按 `Shift+Alt+D`** (Mac: `Cmd+Shift+D`)
2. **选中变量名后按快捷键**：循环切换命名风格
3. **选中字符串后按快捷键**：生成常量字段
4. **选中类名后按快捷键**：生成DTO/VO/BO类
5. **在枚举类中按快捷键**：生成parse方法

## ⚙️ 配置选项

### 基本设置
```
File → Settings → Tools → OneClick Settings
```

- **JavaBean设置**：生成getter/setter/toString等
- **枚举类设置**：自定义parse方法名和code字段名
- **DTO/VO/BO生成设置**：配置BeanUtils类和转换方式
- **快捷键设置**：自定义快捷键（默认：Shift+Alt+D）

### 快捷键自定义

1. 打开 `File → Settings → Tools → OneClick Settings → Keymap Settings`
2. 在"快捷键设置"中输入自定义快捷键（如：`Ctrl+Shift+G`）
3. 输入后自动验证格式，格式正确显示 ✅
4. 点击"Apply"保存设置

**支持的格式：**
- `Ctrl+Shift+D` (Windows/Linux)
- `Cmd+Shift+D` (macOS)
- `Alt+Shift+D`
- 必须包含修饰键（Ctrl/Cmd/Alt/Shift）和主键

## 📋 功能详览

| 场景 | 操作 | 结果 |
|------|------|------|
| **变量名转换** | 选中变量名 → Shift+Alt+D | 循环切换命名风格 |
| **常量生成** | 选中字符串 → Shift+Alt+D | 生成常量字段 |
| **DTO/VO/BO** | 选中类名 → Shift+Alt+D | 生成数据传输对象类 |
| **JavaBean方法** | 在JavaBean类中 → Shift+Alt+D | 生成getter/setter/toString（JSON格式） |
| **枚举parse** | 在枚举类中 → Shift+Alt+D | 生成parse方法 |

## 🎯 使用场景

### 1. 日志打印场景（⭐ 亮点）

**问题：** 阿里规范禁止使用JSON工具类转String打印日志，但JSON格式确实方便调试。

**解决方案：** 使用JSON格式的toString方法

```java
// 生成JSON格式toString
public class User {
    private Long id;
    private String name;
    
    @Override
    public String toString() {
        return "{\"id\":" + id + ", \"name\":\"" + name + "\"}";
    }
}

// 使用
User user = new User();
user.setId(1L);
user.setName("John");

// 输出：{"id":1, "name":"John"}
LOGGER.info("User: {}", user.toString());
```

### 2. DTO/VO/BO快速生成

```java
// 选中类名 → Shift+Alt+D → 选择类型
// 自动生成完整的DTO类，包含：
// - 所有字段的getter/setter
// - toEntity()和fromEntity()转换方法
// - JSON格式的toString方法
```

### 3. 命名风格统一

```java
// 数据库字段转Java字段
user_name → userName (选中后按快捷键)

// Java字段转数据库字段
userName → user_name (选中后按快捷键)

// 生成常量名
userName → USER_NAME (选中后按快捷键)
```

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

## 🤝 贡献指南

我们欢迎各种形式的贡献！

### 如何贡献
1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 Apache License 2.0 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 🐛 问题反馈

遇到问题？我们来帮您解决！

### 反馈渠道
1. 📋 [GitHub Issues](https://github.com/glowxq/OneClick/issues)
2. 📧 邮箱：glowxq@qq.com

## 📞 联系方式

- **作者**：glowxq
- **邮箱**：glowxq@qq.com
- **GitHub**：[https://github.com/glowxq](https://github.com/glowxq)

---

⭐ **如果这个项目对您有帮助，请给我们一个 Star！您的支持是我们持续改进的动力！** ⭐
