# OneClick Code Generator 测试指南

## 🧪 测试环境准备

### 1. 构建插件
```bash
./gradlew build
```

### 2. 启动测试IDE
```bash
./gradlew runIde
```

## 📋 测试用例

### 1. JavaBean类测试

#### 测试文件位置
- `src/test/java/com/glowxq/plugs/model/UserEntity.java`
- `src/test/java/com/glowxq/plugs/dto/UserDTO.java`

#### 测试步骤
1. 打开测试IDE
2. 打开 `UserEntity.java` 文件
3. 将光标放在类内部
4. 按 `Ctrl+Alt+G` (Windows/Linux) 或 `Cmd+Option+G` (macOS)
5. 验证生成的代码

#### 预期结果
```java
public class UserEntity implements Serializable {
    private Long id;
    private String username;
    private String email;
    private Date createTime;
    private boolean active;

    // ================================ JavaBean Methods ================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // ... 其他getter/setter方法

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id + "," +
                "\"username\":\"" + username + "\"" + "," +
                "\"email\":\"" + email + "\"" + "," +
                "\"createTime\":\"" + createTime + "\"" + "," +
                "\"active\":" + active +
                "}";
    }
}
```

### 2. 业务类测试

#### 测试文件位置
- `src/test/java/com/glowxq/plugs/service/UserService.java`
- `src/test/java/com/glowxq/plugs/controller/UserController.java`

#### 测试步骤
1. 打开 `UserService.java` 文件
2. 将光标放在类内部
3. 按 `Ctrl+Alt+G` (Windows/Linux) 或 `Cmd+Option+G` (macOS)
4. 验证生成的代码

#### 预期结果
```java
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    
    public void createUser(String username) {
        // 业务逻辑
        System.out.println("Creating user: " + username);
    }
    
    // ... 其他业务方法
}
```

### 3. 设置面板测试

#### 测试步骤
1. 打开IDE设置：`File` → `Settings` (Windows/Linux) 或 `IntelliJ IDEA` → `Preferences` (macOS)
2. 导航到 `Tools` → `OneClick Code Generator`
3. 验证设置面板显示正确
4. 修改各种设置选项
5. 点击 `Apply` 和 `OK`
6. 重新测试代码生成功能

#### 验证项目
- [ ] 设置面板正确显示
- [ ] 所有设置选项可以修改
- [ ] 设置保存后生效
- [ ] 重启IDE后设置保持

### 4. 不同toString风格测试

#### 测试步骤
1. 在设置中选择不同的toString风格
2. 对同一个类生成toString方法
3. 验证生成的代码格式

#### JSON风格（默认）
```java
@Override
public String toString() {
    return "{" +
            "\"name\":\"" + name + "\"" + "," +
            "\"age\":" + age +
            "}";
}
```

#### 简单风格
```java
@Override
public String toString() {
    return "User{" +
            "name=" + name +
            ", age=" + age +
            "}";
}
```

#### Apache风格
```java
@Override
public String toString() {
    return new ToStringBuilder(this)
            .append("name", name)
            .append("age", age)
            .toString();
}
```

### 5. Fluent Setter测试

#### 测试步骤
1. 在设置中启用 "Generate fluent setters"
2. 生成setter方法
3. 验证生成的代码支持链式调用

#### 预期结果
```java
public UserEntity setName(String name) {
    this.name = name;
    return this;
}

public UserEntity setAge(int age) {
    this.age = age;
    return this;
}
```

### 6. 类型检测测试

#### 测试不同包名的类
- `model` 包 → 应该识别为JavaBean
- `entity` 包 → 应该识别为JavaBean
- `dto` 包 → 应该识别为JavaBean
- `service` 包 → 应该识别为业务类
- `controller` 包 → 应该识别为业务类

#### 验证方法
1. 在不同包中创建测试类
2. 使用插件生成代码
3. 验证生成的内容符合类型预期

## 🔍 问题排查

### 常见问题

#### 1. 插件无法加载
- 检查构建是否成功
- 查看IDE日志中的错误信息
- 确认plugin.xml配置正确

#### 2. 快捷键不工作
- 检查快捷键是否冲突
- 尝试通过右键菜单触发
- 确认光标在Java类内部

#### 3. 设置面板不显示
- 检查设置注册是否正确
- 重启IDE
- 查看控制台错误信息

#### 4. 生成的代码位置不对
- 检查类结构是否复杂
- 验证业务方法识别逻辑
- 手动调整代码位置

### 调试技巧

#### 1. 查看日志
```bash
# IDE日志位置
~/Library/Logs/JetBrains/IntelliJIdea2023.2/idea.log
```

#### 2. 添加调试输出
```java
System.out.println("Debug: " + message);
Logger.getInstance(ClassName.class).info("Debug info");
```

#### 3. 使用断点调试
- 在关键方法设置断点
- 使用IDE的调试功能
- 检查PSI元素状态

## ✅ 测试检查清单

### 基础功能
- [ ] JavaBean方法生成正常
- [ ] 业务类日志字段生成正常
- [ ] 快捷键工作正常
- [ ] 右键菜单工作正常

### 设置功能
- [ ] 设置面板显示正常
- [ ] 设置保存和加载正常
- [ ] 设置修改后生效
- [ ] 不同设置组合工作正常

### 代码质量
- [ ] 生成的代码格式正确
- [ ] 插入位置正确
- [ ] 不会重复生成
- [ ] 支持不同代码风格

### 兼容性
- [ ] 不同版本的IntelliJ IDEA
- [ ] 不同操作系统
- [ ] 不同Java版本
- [ ] 不同项目结构

### 性能
- [ ] 大文件处理正常
- [ ] 多次生成不卡顿
- [ ] 内存使用合理
- [ ] 响应时间可接受

## 📊 测试报告模板

### 测试环境
- IDE版本：IntelliJ IDEA 2023.2
- 操作系统：macOS/Windows/Linux
- Java版本：JDK 11+
- 插件版本：1.0.0

### 测试结果
| 功能 | 状态 | 备注 |
|------|------|------|
| JavaBean生成 | ✅ | 正常 |
| 业务类生成 | ✅ | 正常 |
| 设置面板 | ✅ | 正常 |
| 快捷键 | ✅ | 正常 |
| 类型检测 | ✅ | 正常 |

### 发现的问题
1. 问题描述
2. 重现步骤
3. 预期结果
4. 实际结果
5. 严重程度

### 建议改进
1. 功能建议
2. 性能优化
3. 用户体验改进
