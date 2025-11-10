# OneClick - Smart Code Generator 🚀

[中文](README.md) | English

One shortcut key `Shift+Alt+D` (Windows/Linux) / `Cmd+Shift+D` (Mac) intelligently identifies scenarios and automatically generates code.

## ⭐ Core Highlights

### JSON Format toString Method (Alibaba Code Standard Compliant)

**Problem:** Alibaba code standards prohibit using JSON utility classes to convert to String for logging, but JSON format is indeed convenient for debugging.

**Solution:** Automatically generate JSON format toString methods without JSON utility classes, avoiding performance issues and exception risks.

```java
// Generated toString method
@Override
public String toString() {
    return "{" +
            "\"id\":" + id + ", " +
            "\"name\":\"" + name + "\", " +
            "\"active\":" + active +
            "}";
}

// Usage
User user = new User();
user.setId(1L);
user.setName("John");

// Output: {"id":1, "name":"John"}
LOGGER.info("User: {}", user.toString());
```

**Advantages:**
- ✅ Compliant with Alibaba code standards, no JSON utility classes
- ✅ Direct JSON format output for logging, convenient for debugging
- ✅ Generated at compile time, no runtime overhead
- ✅ Type-safe, compile-time checking

## 🎯 Core Features

### Smart One-Click Generate

Shortcut: `Shift+Alt+D` (Windows/Linux) / `Cmd+Shift+D` (Mac)

One shortcut key, five scenarios, intelligent recognition, automatic execution.

#### 1. Variable Naming Style Conversion
Select a variable name and press the shortcut to cycle through 4 naming styles: camelCase → PascalCase → snake_case → UPPER_SNAKE_CASE

```java
// Select userName → Press shortcut
userName → UserName → user_name → USER_NAME → userName
```

#### 2. Generate Constant Field
Select a string literal and press the shortcut to automatically generate a constant field.

```java
// Select "USER_NOT_FOUND" → Press shortcut
// Auto-generates: private static final String USER_NOT_FOUND = "USER_NOT_FOUND";
```

#### 3. Generate DTO/VO/BO Classes
Select a class name and press the shortcut, then choose the type to automatically generate the corresponding data transfer object class.

```java
// Select class name "User" → Press shortcut → Choose "DTO"
// Auto-generates com/example/entity/dto/UserDTO.java
// Includes: getter/setter, toEntity(), fromEntity(), JSON format toString()
```

#### 4. Generate JavaBean Methods (⭐ JSON Format toString)
Press the shortcut in a Java class with private fields to automatically generate getter/setter/toString methods.

```java
public class User {
    private Long id;
    private String username;
    private boolean active;
    
    // Press shortcut to auto-generate: getter/setter methods, JSON format toString method
}
```

#### 5. Generate Enum parse Method
Press the shortcut in an enum class with a code field to automatically generate a parse method.

```java
public enum UserStatus {
    ACTIVE(1, "Active"),
    INACTIVE(0, "Inactive");
    
    private final Integer code;
    
    // Press shortcut to auto-generate parse method
    public static UserStatus parse(Integer code) {
        // ...
    }
}
```

## 🚀 Quick Start

### Installation
```
File → Settings → Plugins → Search "OneClick" → Install
```

### Usage
1. Press the shortcut in a Java class
2. Select a variable name and press shortcut: cycle through naming styles
3. Select a string and press shortcut: generate constant field
4. Select a class name and press shortcut: generate DTO/VO/BO class
5. Press shortcut in an enum class: generate parse method

## ⚙️ Configuration

### Basic Settings
```
File → Settings → Tools → OneClick Settings
```

- **JavaBean Settings**: Generate getter/setter/toString, etc.
- **Enum Settings**: Customize parse method name and code field name
- **DTO/VO/BO Generation Settings**: Configure BeanUtils class and conversion method
- **Shortcut Settings**: Customize shortcut (default: `Shift+Alt+D` / `Cmd+Shift+D`)

### Shortcut Customization
1. Open `File → Settings → Tools → OneClick Settings → Keymap Settings`
2. Enter custom shortcut (e.g., `Ctrl+Shift+G`)
3. Auto-validates format after input, shows ✅ when correct
4. Click "Apply" to save

**Supported formats:** `Ctrl+Shift+D` (Windows/Linux) / `Cmd+Shift+D` (Mac) / `Alt+Shift+D`

## 📋 Feature List

| Scenario | Operation | Result |
|----------|-----------|--------|
| Variable Name Conversion | Select variable name → Press shortcut | Cycle through naming styles |
| Constant Generation | Select string → Press shortcut | Generate constant field |
| DTO/VO/BO | Select class name → Press shortcut | Generate data transfer object class |
| JavaBean Methods | In JavaBean class → Press shortcut | Generate getter/setter/toString (JSON format) |
| Enum parse | In enum class → Press shortcut | Generate parse method |

## 🎯 Usage Scenarios

### Logging (Alibaba Code Standard Compliant)
```java
// Generate JSON format toString, no JSON utility classes needed
User user = new User();
LOGGER.info("User: {}", user.toString());
// Output: {"id":1, "name":"John"}
```

### DTO/VO/BO Quick Generation
```java
// Select class name → Press shortcut → Choose type
// Auto-generate complete DTO class with JSON format toString
```

### Naming Style Unification
```java
// Database field to Java field: user_name → userName (select and press shortcut)
// Java field to database field: userName → user_name (select and press shortcut)
```

## 🔧 Development

### Requirements
- IntelliJ IDEA 2020.3+
- Java 8+
- Gradle 7.0+

### Local Development
```bash
git clone https://github.com/glowxq/OneClick.git
cd OneClick
./gradlew build
./gradlew runIde
```

## 📄 License

Apache License 2.0

## 📞 Contact

- **Author**: glowxq
- **Email**: glowxq@qq.com
- **GitHub**: https://github.com/glowxq

---

⭐ **If this project helps you, please give us a Star!** ⭐

