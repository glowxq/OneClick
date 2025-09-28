# OneClick JavaBean Generator

一个强大的IntelliJ IDEA插件，用于快速生成和管理JavaBean的getter/setter/toString方法。

## 功能特性

### 🚀 一键生成JavaBean方法
- **Windows/Linux快捷键**: `Ctrl+Alt+G`
- **macOS快捷键**: `Cmd+Option+G`
- **右键菜单**: JavaBean Tools → Generate JavaBean Methods
- 自动生成所有实例字段的getter和setter方法
- 智能生成JSON格式的toString方法
- 自动跳过静态字段和final字段
- 如果方法已存在则跳过生成（getter/setter）
- 自动删除已存在的toString方法并重新生成

### 📁 一键折叠JavaBean方法
- **Windows/Linux快捷键**: `Ctrl+Alt+F`
- **macOS快捷键**: `Cmd+Option+F`
- **右键菜单**: JavaBean Tools → Fold JavaBean Methods
- 折叠所有getter方法
- 折叠所有setter方法
- 折叠所有toString方法
- 显示简洁的方法签名作为折叠占位符

## 安装方法

### 方法一：从源码构建
1. 克隆此仓库
2. 在项目根目录运行：`./gradlew buildPlugin`
3. 在IDEA中安装生成的插件文件：`build/distributions/OneClick-1.0-SNAPSHOT.zip`

**详细安装指南**: 请参考 [INSTALLATION_GUIDE.md](INSTALLATION_GUIDE.md)

### 方法二：开发模式运行
1. 在项目根目录运行：`./gradlew runIde`
2. 这将启动一个带有插件的IDEA实例

## 使用方法

### 生成JavaBean方法
1. 在Java类中定义字段
2. 将光标放在类定义内的任意位置
3. 使用以下任一方式触发：
   - **快捷键**: `Ctrl+Alt+G` (Windows/Linux) 或 `Cmd+Option+G` (macOS)
   - **右键菜单**: 右键 → JavaBean Tools → Generate JavaBean Methods
   - **主菜单**: Code → Generate → Generate JavaBean Methods
4. 插件将自动生成所有缺失的getter/setter方法和JSON格式的toString方法

### 折叠JavaBean方法
1. 将光标放在包含JavaBean方法的类中
2. 使用以下任一方式触发：
   - **快捷键**: `Ctrl+Alt+F` (Windows/Linux) 或 `Cmd+Option+F` (macOS)
   - **右键菜单**: 右键 → JavaBean Tools → Fold JavaBean Methods
   - **主菜单**: Code → Fold JavaBean Methods
3. 所有getter/setter/toString方法将被折叠

## 示例

### 输入类：
```java
public class User {
    private String name;
    private int age;
    private boolean active;
}
```

### 生成后的类：
```java
public class User {
    private String name;
    private int age;
    private boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + name + "\"" + "," +
                "\"age\":" + age + "," +
                "\"active\":" + active +
                "}";
    }
}
```

## 技术特性

- ✅ 支持所有Java基本类型和对象类型
- ✅ 智能处理boolean类型的getter方法（is前缀）
- ✅ 生成符合JavaBean规范的方法
- ✅ JSON格式的toString方法，便于调试和日志记录
- ✅ 智能跳过已存在的getter/setter方法
- ✅ 自动删除并重新生成toString方法
- ✅ 支持代码折叠以提高代码可读性

## 开发信息

- **作者**: glowxq
- **版本**: 1.0-SNAPSHOT
- **兼容性**: IntelliJ IDEA 2023.2 - 2025.3
- **语言**: Java 17+

## 许可证

此项目采用MIT许可证。详情请参阅LICENSE文件。
