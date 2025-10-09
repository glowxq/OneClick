# OneClick - 智能代码生成器 🚀

OneClick 是一个功能强大的 IntelliJ IDEA 插件，专为 Java 开发者设计，提供全方位的智能代码生成、分析和管理功能。

![Plugin Version](https://img.shields.io/badge/version-1.0.0-blue)
![IntelliJ Platform](https://img.shields.io/badge/platform-IntelliJ%20IDEA-orange)
![Java](https://img.shields.io/badge/java-8%2B-green)
![License](https://img.shields.io/badge/license-Apache%202.0-brightgreen)

Plugin Home: https://plugins.jetbrains.com/plugin/28606-oneclick

## 🙋解决核心问题
- **日志打印时使用JSON工具序列号对象**
  - 问题： 为了调试方便，碰到对象希望能将日志数据转为json格式记录方便排查，但是阿里规范明确指出禁止使用JSON格式工具转成String后打印，一方面有性能影响，因为可能抛出异常，直接影响业务流程，商城以前就曾有过这样的线上Bug案例，但是json格式的日志调试确实方便。
    [图片]
  - 解决方案 ：安装插件使用Command + Shift + D一键生成JSON格式的toString方法
    [图片]
- **DTO/VO/BO类生成**
  - 问题：随着公司业务发展，代码分层规范也更严格起来，例如入参使用DTO，响应使用VO等，每次都要手动创建类非常麻烦，部分同学直接copy实体类并进行筛改，copy的时候又没有整理代码导致很多冗余代码出现，不同同学对DTO/VO类转换没有统一标准，或不熟悉标准导致CR被打回。
  - 解决方案：选中类名后使用Command + Shift + D一键生成 DTO/VO/BO类，并生成DTO/Entity的转换方法
- **复杂对象方法冗乱**
  - 问题：一个对象经过一段时间的字段增加，函数增加导致对象很多，有时候新增了字段没有同步修改get、set、toString，甚至出现get、set方法和业务方法混在一起，导致可读性非常差。特别是对于承接第三方请求response的类中很多内部类这种情况尤为突出。
  - 解决方案：在对应java bean中使用Command + Shift + D一键生成类（包括内部类）的get、set、toString等方法（补全并覆盖却少字段），并且按照 业务方法 → getter/setter → toString 的顺序排序
- **代码洁癖&魔法值懒汉**
  - 问题：业务复杂后注入的字段越来越多，但是大家都习惯在底部注入，导致注入字段参差不齐没有规律，如果你是一个代码洁癖的人会感觉很不爽（代码怎么能没有规律呢？）还有一个更难受的事情，编码的时候很顺畅，魔法值随意写后面又懒得抽离层常量导致魔法值满天飞。
  - 解决方案：在对应业务类中使用Command + Shift + D一键排序注入字段（长度排序，模块排序都可以），选中字符串可以一键提取常量，效果如下图
    排序前
 


## 🌟 核心特性

### 🎯 智能一键生成 (Command+Shift+D) - 核心功能
OneClick 的核心功能是智能一键生成，它会根据不同的使用场景自动选择最合适的操作：

#### 📝 场景1：选中变量名 - 命名风格循环切换
选中变量名后按 `Cmd+Shift+D`，自动循环切换4种命名风格：
- **小驼峰** → **大驼峰** → **下划线小写** → **下划线大写** → **小驼峰**
- 示例：`userName` → `UserName` → `user_name` → `USER_NAME` → `userName`
- 特点：静默执行，无弹窗干扰，连续按快捷键即可循环切换

#### 📝 场景2：选中字符串 - 常量生成
选中字符串字面量后按 `Cmd+Shift+D`，自动生成常量字段：
- 示例：选中 `"USER_NOT_FOUND"` → 生成 `private static final String USER_NOT_FOUND = "USER_NOT_FOUND";`
- 智能插入位置：有LOGGER字段时插入到LOGGER下方，否则插入到类顶部
- 自动去重：避免生成重复的常量字段

#### 📝 场景3：选中类名 - 生成DTO/VO/BO
选中类名后按 `Cmd+Shift+D`，弹出选择对话框生成数据传输对象：
- **DTO** (Data Transfer Object) - 数据传输对象
- **VO** (Value Object) - 值对象
- **BO** (Business Object) - 业务对象
- 特点：
  - 自动创建dto/vo/bo子目录
  - 添加@Serial注解到serialVersionUID
  - 生成JSON格式的toString方法
  - 支持BeanUtils或原生getter/setter进行属性复制
  - 方法排序：toEntity → fromEntity → getter/setter → toString

#### 🏗️ 场景4：JavaBean类 - 自动生成标准方法
在JavaBean类中按 `Cmd+Shift+D`，自动生成所有标准方法：
- 识别规则：包名包含 `entity, model, bean, pojo, dto, vo, domain, data, bo, record`
- 生成内容：getter/setter/toString/equals/hashCode
- 智能特性：
  - boolean字段生成isXxx()方法
  - toString生成JSON格式
  - 自动去重，不重复生成已存在的方法

#### 🏗️ 场景5：业务类 - 智能增强
在业务类中按 `Cmd+Shift+D`，自动执行智能增强：
- 识别规则：包名包含 `service, controller, mapper, dao, handle, manager, handler, component, config, util, utils, debug, demo`
- 生成内容：
  - SLF4J Logger字段
  - serialVersionUID字段
  - 字段智能排序（可选，默认禁用）
- 字段排序特性：
  - 只排序实例字段，保护常量和静态字段
  - 支持按名称、长度、类型排序
  - 支持权限修饰符优先级排序

#### 📦 场景6：选中包 - 批量生成
在项目视图中选中包后按 `Cmd+Shift+D`，批量处理包内所有Java文件：
- 递归处理子包中的文件
- 显示确认对话框，避免误操作
- 提供详细的成功/失败统计
- 自动跳过接口和枚举类

#### 🔧 其他强大功能
- **开发工具集合** (Command+Shift+U)：UUID生成、时间戳、枚举创建等20种实用工具
- **数据库工具** (Command+Shift+Y)：Entity注解、SQL语句、Repository生成等
- **项目视图集成**：右键菜单中的OneClick工具组

### 🔧 代码重构助手
- **10种重构操作**：提取常量、转换Stream API、添加空值检查等
- **智能注释生成**：根据代码上下文自动生成中文注释
- **代码清理助手**：移除未使用导入、空行、调试代码等
- **快速文档生成**：自动生成标准JavaDoc文档

### 📊 代码分析工具
- **详细统计分析**：行数、复杂度、方法长度等指标
- **代码质量检测**：长方法、大类、TODO注释统计
- **可视化报告**：直观的分析结果展示

### ⚙️ 高度可定制
- **快捷键自定义**：支持不同操作系统的快捷键配置
- **多语言支持**：完整的中英双语界面
- **灵活配置**：丰富的设置选项满足不同需求

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

#### 1. 智能一键生成 (Command+Shift+D)

**场景一：变量名命名风格循环切换**
```java
// 选中 userName → 按 Cmd+Shift+D
userName → UserName → user_name → USER_NAME → userName

// 选中 emailAddress → 按 Cmd+Shift+D
emailAddress → EmailAddress → email_address → EMAIL_ADDRESS → emailAddress

// 特点：静默执行，无弹窗，连续按快捷键循环切换
```

**场景二：选中字符串生成常量**
```java
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public void processUser() {
        // 选中 "USER_NOT_FOUND" → 按 Cmd+Shift+D
        throw new RuntimeException("USER_NOT_FOUND");
        // 自动生成常量（插入到LOGGER下方）：
        // private static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    }
}
```

**场景三：选中类名生成DTO/VO/BO**
```java
package com.example.entity;

public class User {
    private Long id;
    private String name;
    private boolean active;

    // 选中类名 "User" → 按 Cmd+Shift+D → 选择 "DTO"
    // 自动在 com/example/entity/dto/ 目录下生成 UserDTO.java
}

// 生成的 UserDTO.java：
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
    private boolean active;

    // toEntity() - 转换为实体类
    public User toEntity() {
        User entity = new User();
        BeanUtils.copyProperties(this, entity);
        return entity;
    }

    // fromEntity() - 从实体类转换
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
    public boolean isActive() { return active; }  // boolean字段使用isXxx()
    public void setActive(boolean active) { this.active = active; }

    // JSON格式的toString
    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id + ", " +
                "\"name\":\"" + name + "\", " +
                "\"active\":" + active +
                "}";
    }
}
```

**场景四：JavaBean类自动生成方法**
```java
package com.example.model;

public class User {
    private Long id;
    private String name;
    private String email;
    private boolean active;

    // 按 Cmd+Shift+D 自动生成所有JavaBean方法
    // 包括：getter/setter/toString/equals/hashCode
}
```

**场景五：业务类智能增强**
```java
package com.example.service;

@Service
public class UserService {
    // 按 Cmd+Shift+D 自动生成：
    // 1. private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    // 2. private static final long serialVersionUID = 1L;
    // 3. 字段智能排序（可选，默认禁用）

    private UserRepository userRepository;
    private EmailService emailService;
}
```

**场景六：选中包批量生成**
```
项目视图中：
src/main/java/com/example/model/
├── User.java
├── Order.java
└── Product.java

选中 model 包 → 按 Cmd+Shift+D
→ 批量为所有类生成JavaBean方法
→ 显示成功/失败统计
```

#### 2. 批量生成 (Command+Shift+B)
- 在项目视图中选中包或多个文件
- 右键 → OneClick → 批量生成
- 或使用快捷键 `Command+Shift+B`

#### 3. 开发工具集合 (Command+Shift+U)
- UUID生成、时间戳插入、枚举文件创建
- 常量生成、Builder模式、JSON模板等

## 📋 功能详览

### 🎨 代码生成功能

| 功能 | 快捷键 (Win/Linux) | 快捷键 (macOS) | 描述 |
|------|-------------------|----------------|------|
| **智能一键生成** | `Ctrl+Shift+D` | `Cmd+Shift+D` | 🎯 **核心功能**：智能识别场景并执行相应操作 |
| 开发工具集合 | `Ctrl+Shift+U` | `Cmd+Shift+U` | 🛠️ 20种开发工具：UUID、时间戳、枚举等 |
| 数据库工具 | `Ctrl+Shift+Y` | `Cmd+Shift+Y` | 🗄️ 数据库代码生成：Entity、SQL、Repository |
| 批量生成 | `Ctrl+Shift+B` | `Cmd+Shift+B` | 📦 批量处理多个文件或包 |
| 代码模板 | `Ctrl+Shift+T` | `Cmd+Shift+T` | 📝 15种设计模式和架构模板 |
| 重构助手 | `Ctrl+Shift+R` | `Cmd+Shift+R` | 🔄 10种重构操作 |
| 智能注释 | `Ctrl+Shift+C` | `Cmd+Shift+C` | 💬 自动生成注释和JavaDoc |
| 代码清理 | `Ctrl+Shift+L` | `Cmd+Shift+L` | 🧹 清理冗余代码和导入 |
| 代码分析 | `Ctrl+Shift+A` | `Cmd+Shift+A` | 📊 代码质量分析和统计 |
| 快速文档 | `Ctrl+Shift+Q` | `Cmd+Shift+Q` | 📖 生成README和API文档 |
| 折叠方法 | `Ctrl+Shift+F` | `Cmd+Shift+F` | 📁 折叠JavaBean方法 |

### 🎯 智能一键生成详解

OneClick 的核心功能是智能一键生成 (`Cmd+Shift+D`)，它会根据当前上下文智能选择操作。**一个快捷键，六大场景，智能识别，自动执行！**

#### 📝 六大智能场景

| 场景 | 识别条件 | 操作结果 | 示例 |
|------|---------|---------|------|
| **1. 变量名切换** | 选中变量名 | 命名风格循环切换 | `userName` → `UserName` → `user_name` → `USER_NAME` |
| **2. 常量生成** | 选中字符串字面量 | 生成常量字段 | `"USER_NOT_FOUND"` → `private static final String USER_NOT_FOUND = ...` |
| **3. DTO/VO/BO** | 选中类名 | 生成数据传输对象 | 选择类型 → 生成对应的DTO/VO/BO类 |
| **4. JavaBean** | JavaBean类 | 生成标准方法 | getter/setter/toString/equals/hashCode |
| **5. 业务类** | 业务类 | 智能增强 | Logger/serialVersionUID/字段排序 |
| **6. 批量生成** | 选中包 | 批量处理 | 批量为包内所有类生成代码 |

#### 🔧 智能特性

**命名风格切换**：
- 循环切换：小驼峰 → 大驼峰 → 下划线小写 → 下划线大写 → 小驼峰
- 静默执行，无弹窗干扰
- 连续按快捷键即可循环切换

**常量生成**：
- 有LOGGER字段：插入到LOGGER下方
- 无LOGGER字段：插入到类顶部
- 自动去重，避免重复生成

**DTO/VO/BO生成**：
- 自动创建dto/vo/bo子目录
- 添加@Serial注解到serialVersionUID
- 生成JSON格式的toString方法
- 支持BeanUtils或原生getter/setter
- boolean字段生成isXxx()方法
- 方法排序：toEntity → fromEntity → getter/setter → toString

**字段排序**：
- 只排序实例字段，保护常量和静态字段
- 支持按名称、长度、类型排序
- 默认禁用，需在设置中启用

**类类型识别**：
- JavaBean：包名包含 `entity, model, bean, pojo, dto, vo, domain, data, bo, record`
- 业务类：包名包含 `service, controller, mapper, dao, handle, manager, handler, component, config, util, utils, debug, demo`

### 🛠️ 开发工具集合 (Command+Shift+U)

#### 代码生成工具
- **🔧 生成UUID** - 生成随机UUID字符串
- **📅 插入时间戳** - 插入当前时间戳
- **🔍 生成序列化ID** - 生成serialVersionUID
- **📝 生成TODO注释** - 生成带时间和作者的TODO注释
- **🎯 生成测试方法** - 生成单元测试方法模板

#### 代码转换工具
- **🔒 生成常量** - 将选中文本转换为常量定义
- **📊 生成枚举** - 创建新的枚举文件（不在当前文件中）
- **🌐 生成JSON模板** - 生成JSON数据模板
- **🔄 转换命名风格** - 转换驼峰/下划线命名
- **📋 生成Builder模式** - 为当前类生成Builder模式

### 🗄️ 数据库工具集合 (Command+Shift+Y)

#### JPA/Hibernate工具
- **🗃️ 生成Entity注解** - 为实体类添加JPA注解
- **📝 生成SQL建表语句** - 根据实体类生成CREATE TABLE语句
- **🔍 生成Repository接口** - 生成Spring Data JPA Repository

#### MyBatis工具
- **📊 生成MyBatis Mapper** - 生成MyBatis Mapper接口和XML
- **🔄 生成数据转换器** - 生成Entity与DTO转换器

#### Spring Boot集成
- **📋 生成CRUD Service** - 生成完整的CRUD Service类
- **🌐 生成REST Controller** - 生成RESTful API控制器
- **🔧 生成数据库配置** - 生成数据源配置类

### 📝 代码模板库 (Command+Shift+T)

#### 设计模式
- **Singleton Pattern** - 单例模式
- **Builder Pattern** - 建造者模式
- **Factory Pattern** - 工厂模式
- **Observer Pattern** - 观察者模式
- **Strategy Pattern** - 策略模式

#### 架构层模板
- **REST Controller** - RESTful控制器
- **Service Layer** - 服务层
- **Repository Layer** - 数据访问层
- **Exception Handler** - 异常处理器

#### 工具类模板
- **Validation Utils** - 验证工具类
- **Date Utils** - 日期工具类
- **String Utils** - 字符串工具类
- **File Utils** - 文件工具类
- **JSON Utils** - JSON工具类
- **Test Class** - 测试类模板

### 🔄 重构操作

1. **Extract Constants** - 提取常量
2. **Convert to Stream API** - 转换为Stream API
3. **Add Null Checks** - 添加空值检查
4. **Extract Method** - 提取方法
5. **Inline Variables** - 内联变量
6. **Convert to Lambda** - 转换为Lambda表达式
7. **Add Logging** - 添加日志记录
8. **Add Documentation** - 添加文档注释
9. **Optimize Imports** - 优化导入
10. **Format Code** - 格式化代码

### 🧹 代码清理功能

- **移除未使用的导入** - 清理无用import语句
- **移除空行** - 清理多余的空白行
- **移除未使用的变量** - 清理未使用的局部变量
- **移除TODO注释** - 清理TODO/FIXME注释
- **格式化代码** - 统一代码格式
- **优化导入顺序** - 按规范排序import
- **移除多余空格** - 清理多余的空白字符
- **统一换行符** - 标准化行结束符
- **移除调试代码** - 清理System.out.println等调试语句

### 📊 代码分析指标

#### 基础统计
- 总行数、代码行数、注释行数、空白行数
- 类数量、方法数量、字段数量、变量数量
- 导入语句数量

#### 复杂度分析
- 圈复杂度计算
- 方法长度统计（最短/最长/平均）
- 类大小统计

#### 质量指标
- 长方法检测（>20行）
- 大类检测（>200行）
- TODO注释统计
- 空catch块检测

## ⚙️ 配置选项

### 基本设置
```
File → Settings → Tools → OneClick
```

- **语言选择**：中文/English
- **类型检测**：自动识别JavaBean vs 业务类
- **生成选项**：选择要生成的方法类型

### JavaBean设置
- **包名模式**：默认 `entity,model,bean,pojo,dto,vo,domain,data,bo,record`
- **生成方法**：getter/setter/toString/equals/hashCode
- **toString风格**：JSON/简单/Apache Commons

### 业务类设置
- **包名模式**：默认 `service,controller,mapper,dao,handle,manager,handler,component,config,util,utils,debug,demo`
- **Logger类型**：SLF4J/Log4j/JUL
- **字段排序**：启用/禁用，排序方式（名称/长度/类型）
- **权限修饰符排序**：public → protected → package → private

### DTO/VO/BO生成设置
- **使用BeanUtils**：启用/禁用（默认启用）
- **BeanUtils类**：默认 `org.springframework.beans.BeanUtils`，可自定义
- **生成目录**：自动创建dto/vo/bo子目录
- **方法排序**：toEntity → fromEntity → getter/setter → toString

### 内部类设置
- **处理内部类**：是否递归处理内部类
- **最大深度**：内部类处理的最大层数（1-10）
- **分隔注释**：为内部类生成分隔注释

### 快捷键设置
```
File → Settings → Tools → OneClick → Keymap Settings
```
- 自定义所有功能的快捷键
- 支持不同操作系统
- 快捷键冲突检测

## 🎯 使用场景

### 1. 命名风格循环切换 🔄
```java
// 选中变量名 → 按 Cmd+Shift+D → 循环切换命名风格
userName → UserName → user_name → USER_NAME → userName

// 实际应用场景：
// 1. 数据库字段转Java字段：user_name → userName
// 2. Java字段转数据库字段：userName → user_name
// 3. 生成常量名：userName → USER_NAME
// 4. 生成类名：userName → UserName
```

### 2. 字符串常量管理 📝
```java
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public void validateUser() {
        // 选中字符串 "INVALID_USER" → Cmd+Shift+D
        // 自动生成常量（插入到LOGGER下方）：
        // private static final String INVALID_USER = "INVALID_USER";
        if (user == null) {
            throw new RuntimeException("INVALID_USER");
        }
    }
}
```

### 3. DTO/VO/BO 快速生成 🏗️
```java
// 源实体类
package com.example.entity;

public class User {
    private Long id;
    private String name;
    private boolean active;
}

// 选中类名 "User" → Cmd+Shift+D → 选择 "DTO"
// 自动生成 com/example/entity/dto/UserDTO.java
// 包含：
// - @Serial注解的serialVersionUID
// - 所有字段的getter/setter（boolean使用isXxx()）
// - toEntity()和fromEntity()转换方法（支持BeanUtils）
// - JSON格式的toString()方法
```

### 4. JavaBean 快速开发 ☕
```java
package com.example.model;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private boolean active;
    private LocalDateTime createTime;

    // 按 Cmd+Shift+D 一键生成所有标准方法
    // 自动识别为JavaBean类，生成：
    // - getter/setter（boolean使用isXxx()）
    // - toString（JSON格式）
    // - equals/hashCode
}
```

### 5. 业务类智能增强 💼
```java
package com.example.service;

@Service
public class UserService {
    // 按 Cmd+Shift+D 自动生成：
    // 1. private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    // 2. private static final long serialVersionUID = 1L;
    // 3. 字段智能排序（可选，默认禁用）

    private UserRepository userRepository;
    private EmailService emailService;
    private ValidationService validationService;
}
```

### 6. 项目级批量处理 📦
```
项目结构：
src/main/java/com/example/
├── model/
│   ├── User.java
│   ├── Order.java
│   └── Product.java
└── service/
    ├── UserService.java
    └── OrderService.java

操作：
1. 选中 model 包 → Cmd+Shift+D → 批量生成JavaBean方法
2. 选中 service 包 → Cmd+Shift+D → 批量生成Logger和serialVersionUID
3. 显示详细统计：成功 5 个类，失败 0 个类
```

### 7. 开发效率提升 🚀
- **枚举文件创建**：Cmd+Shift+U → 生成枚举 → 自动创建独立枚举文件
- **数据库代码生成**：Cmd+Shift+Y → 一键生成Entity、Repository、Service
- **常量管理**：智能常量生成，自动插入到合适位置（LOGGER下方）
- **命名规范**：一键循环切换4种命名风格，保持代码一致性
- **DTO/VO/BO生成**：自动创建子目录，生成完整的数据传输对象

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

### 项目结构
```
OneClick/
├── src/main/java/com/glowxq/plugs/
│   ├── actions/          # Action实现
│   ├── settings/         # 设置面板
│   ├── utils/           # 工具类
│   └── icons/           # 图标资源
├── src/main/resources/
│   ├── META-INF/        # 插件配置
│   ├── messages/        # 国际化文件
│   └── icons/           # 图标文件
└── docs/                # 文档
```

## 🤝 贡献指南

我们欢迎各种形式的贡献！

### 如何贡献
1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

### 贡献类型
- 🐛 Bug 修复
- ✨ 新功能开发
- 📝 文档改进
- 🎨 UI/UX 优化
- ⚡ 性能优化
- 🧪 测试用例

## 📄 许可证

本项目采用 Apache License 2.0 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 🐛 问题反馈

遇到问题？我们来帮您解决！

### 反馈渠道
1. 📋 [GitHub Issues](https://github.com/glowxq/OneClick/issues)
2. 📧 邮箱：glowxq@qq.com
3. 💬 [讨论区](https://github.com/glowxq/OneClick/discussions)

## 📞 联系方式

- **作者**：glowxq
- **邮箱**：glowxq@qq.com
- **GitHub**：[https://github.com/glowxq](https://github.com/glowxq)

## 🙏 致谢

感谢所有为 OneClick 项目做出贡献的开发者和用户！

### 特别感谢
- IntelliJ Platform SDK 团队
- 所有提供反馈和建议的用户
- 开源社区的支持

## 📈 版本历史

### v1.0.0 (2024-09-29)
- 🎉 首次发布
- ✨ 完整的JavaBean方法生成功能
- 🔧 代码模板生成器（15种模板）
- 🔄 重构助手（10种操作）
- 🧹 代码清理助手
- 📊 代码分析工具
- 📝 快速文档生成
- ⚙️ 完整的设置面板
- 🌍 中英双语支持
- ⌨️ 自定义快捷键
- 📦 批量处理功能

---

⭐ **如果这个项目对您有帮助，请给我们一个 Star！您的支持是我们持续改进的动力！** ⭐
