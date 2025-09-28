# OneClick JavaBean Generator 使用指南

## 插件功能概述

这个IDEA插件提供了两个主要功能：

1. **一键生成JavaBean方法**
   - Windows/Linux: `Ctrl+Alt+G`
   - macOS: `Cmd+Option+G`
   - 右键菜单: JavaBean Tools → Generate JavaBean Methods

2. **一键折叠JavaBean方法**
   - Windows/Linux: `Ctrl+Alt+F`
   - macOS: `Cmd+Option+F`
   - 右键菜单: JavaBean Tools → Fold JavaBean Methods

## 详细使用说明

### 1. 生成JavaBean方法

#### 操作步骤：
1. 在Java类中定义字段
2. 将光标放在类定义内的任意位置
3. 使用以下任一方式触发：
   - **快捷键**: `Ctrl+Alt+G` (Windows/Linux) 或 `Cmd+Option+G` (macOS)
   - **右键菜单**: 右键 → JavaBean Tools → Generate JavaBean Methods
   - **主菜单**: Code → Generate → Generate JavaBean Methods
4. 插件将自动生成所需的方法

#### 生成规则：
- **Getter方法**：为所有非静态、非final字段生成getter方法
  - 普通字段：`getFieldName()`
  - boolean字段：`isFieldName()` 或 `getFieldName()`
- **Setter方法**：为所有非静态、非final字段生成setter方法
  - 格式：`setFieldName(FieldType fieldName)`
- **toString方法**：生成JSON格式的toString方法
  - 自动删除已存在的toString方法
  - 重新生成包含所有字段的JSON格式toString

#### 智能特性：
- ✅ 不会重复生成已存在的getter/setter方法
- ✅ 每次都会重新生成toString方法（确保包含最新字段）
- ✅ 自动处理不同数据类型的JSON格式化
- ✅ 跳过静态字段和final字段

### 2. 折叠JavaBean方法

#### 操作步骤：
1. 将光标放在包含JavaBean方法的类中
2. 使用以下任一方式触发：
   - **快捷键**: `Ctrl+Alt+F` (Windows/Linux) 或 `Cmd+Option+F` (macOS)
   - **右键菜单**: 右键 → JavaBean Tools → Fold JavaBean Methods
   - **主菜单**: Code → Fold JavaBean Methods
3. 所有getter/setter/toString方法将被折叠

#### 折叠效果：
- Getter方法显示为：`getter: getFieldName()`
- Setter方法显示为：`setter: setFieldName(FieldType)`
- toString方法显示为：`toString(): String`

## 示例演示

### 原始类：
```java
public class User {
    private String name;
    private int age;
    private boolean active;
    private List<String> hobbies;
}
```

### 按 `Ctrl+Alt+G` 后：
```java
public class User {
    private String name;
    private int age;
    private boolean active;
    private List<String> hobbies;

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

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + name + "\"" + "," +
                "\"age\":" + age + "," +
                "\"active\":" + active + "," +
                "\"hobbies\":" + hobbies +
                "}";
    }
}
```

### 按 `Ctrl+Alt+F` 后（折叠效果）：
```java
public class User {
    private String name;
    private int age;
    private boolean active;
    private List<String> hobbies;

    getter: getName()
    setter: setName(String)
    getter: getAge()
    setter: setAge(int)
    getter: isActive()
    setter: setActive(boolean)
    getter: getHobbies()
    setter: setHobbies(List)
    toString(): String
}
```

## 注意事项

1. **文件类型**：插件只在Java文件中生效
2. **光标位置**：需要将光标放在类定义内
3. **字段类型**：只处理实例字段（非静态、非final）
4. **toString重新生成**：每次生成都会删除旧的toString方法并创建新的

## 快捷键总结

| 功能 | Windows/Linux | macOS | 说明 |
|------|---------------|-------|------|
| 生成JavaBean方法 | `Ctrl+Alt+G` | `Cmd+Option+G` | 生成getter/setter/toString |
| 折叠JavaBean方法 | `Ctrl+Alt+F` | `Cmd+Option+F` | 折叠所有JavaBean方法 |

## 访问方式

### 快捷键
- **Windows/Linux**: `Ctrl+Alt+G` / `Ctrl+Alt+F`
- **macOS**: `Cmd+Option+G` / `Cmd+Option+F`

### 主菜单
- **生成方法**: Code → Generate → Generate JavaBean Methods
- **折叠方法**: Code → Fold JavaBean Methods

### 右键菜单
- 在Java文件中右键 → **JavaBean Tools** → 选择对应功能
  - Generate JavaBean Methods
  - Fold JavaBean Methods

## 故障排除

如果插件不工作，请检查：
1. 是否在Java文件中使用
2. 光标是否在类定义内
3. 类是否包含实例字段
4. IDEA版本是否兼容（需要2023.2+）
