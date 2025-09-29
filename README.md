# OneClick

一个智能的IntelliJ IDEA插件，支持JavaBean和业务类的一键代码生成。

## 🚀 功能特性

### 智能类型检测
- **自动检测类类型**：根据包名、注解、字段和方法特征智能判断是JavaBean还是业务类
- **JavaBean检测**：识别entity、model、bean、pojo、dto、vo等包名的类
- **业务类检测**：识别service、controller、manager、handler等包名的类

### JavaBean代码生成
- ✅ **Getter/Setter方法**：支持标准setter和fluent setter（链式调用）
- ✅ **ToString方法**：支持JSON、简单、Apache Commons三种风格
- ✅ **Equals/HashCode方法**：基于字段生成
- ✅ **分割注释**：在业务方法和JavaBean方法之间添加分割线
- ✅ **智能插入位置**：JavaBean方法插入到业务方法之后

### 业务类代码生成
- ✅ **日志字段**：支持SLF4J、Log4j、JUL三种日志框架
- ✅ **SerialVersionUID**：为实现Serializable的类自动生成
- ✅ **自定义日志字段名**：默认为LOGGER，可自定义

### 高级功能
- ✅ **可配置设置面板**：所有功能都可以通过设置面板控制
- ✅ **重复生成检测**：避免重复生成相同的代码
- ✅ **代码格式化**：生成的代码自动格式化
- ✅ **方法折叠**：一键折叠所有JavaBean方法

## 🛠️ 使用方法

### 快捷键
- **Windows/Linux**: `Ctrl+Alt+G` - 生成代码
- **macOS**: `Cmd+Option+G` - 生成代码
- **折叠方法**: `Ctrl+Alt+F` (Windows/Linux) / `Cmd+Option+F` (macOS)

### 菜单操作
1. 右键点击Java类文件
2. 选择 "JavaBean Tools" → "Generate JavaBean Methods"
3. 或者使用 "Generate" 菜单

### 设置配置
1. 打开 IntelliJ IDEA 设置
   - **Windows/Linux**: `File` → `Settings`
   - **macOS**: `IntelliJ IDEA` → `Preferences`
2. 导航到 `Tools` → `OneClick`
3. 配置各种生成选项

> 💡 **设置入口位置**: 在IDE主菜单中找到设置选项，然后在左侧导航树中展开 `Tools` 节点，即可看到 `OneClick Code Generator` 选项。

## ⚙️ 设置选项

### JavaBean生成设置
- **Generate separator comment**: 生成分割注释
- **Generate getter/setter methods**: 生成getter/setter方法
- **Generate toString method**: 生成toString方法
- **Generate equals method**: 生成equals方法
- **Generate hashCode method**: 生成hashCode方法

### 业务类设置
- **Generate logger field**: 为业务类生成日志字段
- **Logger field name**: 日志字段名称（默认：LOGGER）
- **Logger type**: 日志框架类型（slf4j/log4j/jul）

### 通用设置
- **Auto detect class type**: 自动检测类类型
- **Use field comments**: 在生成的方法中使用字段注释
- **Generate serialVersionUID**: 为Serializable类生成serialVersionUID

### 代码风格设置
- **Generate fluent setters**: 生成链式setter方法
- **ToString style**: toString方法风格（json/simple/apache）

## 📝 示例

### JavaBean示例

```java
// 原始类
public class User {
    private String name;
    private int age;
    private boolean active;
}

// 生成后
public class User {
    private String name;
    private int age;
    private boolean active;

    // ================================ JavaBean Methods ================================

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

### 业务类示例

```java
// 原始类
@Service
public class UserService {
    
    public void createUser(String username) {
        // 业务逻辑
    }
}

// 生成后
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    
    public void createUser(String username) {
        // 业务逻辑
    }
}
```

## 🔧 开发环境

- IntelliJ IDEA 2023.1+
- Java 11+
- Gradle 8.0+

## 📄 许可证

MIT License

## 🤝 贡献

欢迎提交Issue和Pull Request！

## 📚 文档

详细文档请查看 [docs](./docs/) 目录：
- [用户指南](./docs/USER_GUIDE.md) - 详细的使用说明和配置指南
- [开发文档](./docs/DEVELOPMENT.md) - 插件开发和扩展指南

## 📞 联系方式

- 作者：glowxq
- 邮箱：glowxq@qq.com
- GitHub：https://github.com/glowxq
