# Boolean字段处理修复说明

## 问题描述

### 问题1：Boolean vs boolean 的 getter 方法区别

在Java中，`boolean`（基本类型）和`Boolean`（包装类型）的getter方法命名规范不同：

- **boolean（基本类型）**：使用 `isXxx()` 方法
- **Boolean（包装类型）**：使用 `getXxx()` 方法

**原因**：
- 基本类型`boolean`不能为null，使用`isXxx()`更符合语义
- 包装类型`Boolean`可以为null，使用`getXxx()`与其他包装类型保持一致

**错误示例**：
```java
public class User {
    private Boolean active;  // 包装类型
    
    // ❌ 错误：包装类型不应该使用isXxx()
    public Boolean isActive() {
        return active;
    }
    
    // ✅ 正确：包装类型应该使用getXxx()
    public Boolean getActive() {
        return active;
    }
}
```

### 问题2：toEntity和fromEntity方法中的调用错误

在生成DTO/VO/BO类时，`toEntity()`和`fromEntity()`方法需要正确调用对应的getter/setter方法：

**错误示例**：
```java
public class UserDTO {
    private Boolean active;  // 包装类型
    
    public Boolean isActive() {  // ❌ 错误的getter方法名
        return active;
    }
    
    public User toEntity() {
        User entity = new User();
        entity.setActive(this.isActive());  // ❌ 调用了错误的方法
        return entity;
    }
}
```

**正确示例**：
```java
public class UserDTO {
    private Boolean active;  // 包装类型
    
    public Boolean getActive() {  // ✅ 正确的getter方法名
        return active;
    }
    
    public User toEntity() {
        User entity = new User();
        entity.setActive(this.getActive());  // ✅ 调用正确的方法
        return entity;
    }
}
```

## 修复方案

### 1. 修复 JavaBeanUtils.getGetterName()

**文件**：`src/main/java/com/glowxq/plugs/utils/JavaBeanUtils.java`

**修改前**：
```java
public static String getGetterName(PsiField field) {
    String fieldName = field.getName();
    PsiType fieldType = field.getType();
    
    // ❌ 错误：Boolean包装类型也会使用isXxx()
    if (PsiType.BOOLEAN.equals(fieldType) && fieldName.startsWith("is")) {
        return fieldName;
    } else if (PsiType.BOOLEAN.equals(fieldType)) {
        return "is" + capitalize(fieldName);
    } else {
        return "get" + capitalize(fieldName);
    }
}
```

**修改后**：
```java
public static String getGetterName(PsiField field) {
    String fieldName = field.getName();
    PsiType fieldType = field.getType();
    
    // ✅ 正确：只有基本类型boolean才使用isXxx()
    if (PsiType.BOOLEAN.equals(fieldType)) {
        if (fieldName.startsWith("is")) {
            return fieldName;
        } else {
            return "is" + capitalize(fieldName);
        }
    } else {
        // Boolean包装类型使用getXxx()
        return "get" + capitalize(fieldName);
    }
}
```

**关键点**：
- `PsiType.BOOLEAN` 只匹配基本类型 `boolean`
- `Boolean` 包装类型会走 `else` 分支，使用 `getXxx()`

### 2. 修复 GenerateJavaBeanMethodsAction.getGetterNameForField()

**文件**：`src/main/java/com/glowxq/plugs/actions/GenerateJavaBeanMethodsAction.java`

**修改前**：
```java
private String getGetterNameForField(PsiField field) {
    String fieldName = field.getName();
    String fieldType = field.getType().getCanonicalText();

    // ❌ 错误：Boolean包装类型也会使用isXxx()
    if ("boolean".equals(fieldType) || "java.lang.Boolean".equals(fieldType)) {
        if (fieldName.startsWith("is") && fieldName.length() > 2 && Character.isUpperCase(fieldName.charAt(2))) {
            return fieldName;
        }
        return "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
}
```

**修改后**：
```java
private String getGetterNameForField(PsiField field) {
    String fieldName = field.getName();
    String fieldType = field.getType().getCanonicalText();

    // ✅ 正确：只有基本类型boolean才使用isXxx()
    if ("boolean".equals(fieldType)) {
        if (fieldName.startsWith("is") && fieldName.length() > 2 && Character.isUpperCase(fieldName.charAt(2))) {
            return fieldName;
        }
        return "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    // Boolean包装类型使用getXxx()
    return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
}
```

### 3. 修复 DTO/VO/BO 生成中的 getter 方法

**文件**：`src/main/java/com/glowxq/plugs/actions/GenerateJavaBeanMethodsAction.java`

**修改前**：
```java
// 3. 生成getter和setter方法
for (PsiField field : fields) {
    String fieldType = getSimpleTypeName(field.getType().getCanonicalText());
    String fieldName = field.getName();
    String capitalizedName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

    // ❌ 错误：所有字段都使用getXxx()
    sb.append("    public ").append(fieldType).append(" get").append(capitalizedName).append("() {\n");
    sb.append("        return ").append(fieldName).append(";\n");
    sb.append("    }\n\n");
    
    // Setter
    sb.append("    public void set").append(capitalizedName).append("(").append(fieldType).append(" ").append(fieldName).append(") {\n");
    sb.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
    sb.append("    }\n\n");
}
```

**修改后**：
```java
// 3. 生成getter和setter方法
for (PsiField field : fields) {
    String fieldType = getSimpleTypeName(field.getType().getCanonicalText());
    String fieldName = field.getName();
    String capitalizedName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

    // ✅ 正确：根据字段类型使用正确的getter方法名
    String getterName = getGetterNameForField(field);
    String getterMethodName = getterName.startsWith("is") ? getterName : "get" + capitalizedName;
    sb.append("    public ").append(fieldType).append(" ").append(getterMethodName).append("() {\n");
    sb.append("        return ").append(fieldName).append(";\n");
    sb.append("    }\n\n");

    // Setter
    sb.append("    public void set").append(capitalizedName).append("(").append(fieldType).append(" ").append(fieldName).append(") {\n");
    sb.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
    sb.append("    }\n\n");
}
```

## 测试用例

创建了测试文件：`src/test/java/com/glowxq/plugs/model/TestBooleanFieldBean.java`

```java
public class TestBooleanFieldBean {
    // 基本类型boolean - 应该生成isActive()
    private boolean active;
    
    // 包装类型Boolean - 应该生成getEnabled()
    private Boolean enabled;
    
    // 基本类型boolean，字段名以is开头 - 应该生成isValid()
    private boolean isValid;
    
    // 包装类型Boolean，字段名以is开头 - 应该生成getIsDeleted()
    private Boolean isDeleted;
}
```

**预期生成结果**：
```java
// boolean类型 - 使用isXxx()
public boolean isActive() {
    return active;
}

// Boolean类型 - 使用getXxx()
public Boolean getEnabled() {
    return enabled;
}

// boolean类型，字段名以is开头 - 使用isXxx()
public boolean isValid() {
    return isValid;
}

// Boolean类型，字段名以is开头 - 使用getXxx()
public Boolean getIsDeleted() {
    return isDeleted;
}
```

## 影响范围

此修复影响以下功能：

1. **JavaBean方法生成**：`Cmd+Shift+D` 在JavaBean类中生成getter/setter方法
2. **DTO/VO/BO生成**：选中类名生成DTO/VO/BO时的getter/setter方法
3. **toEntity/fromEntity方法**：确保正确调用对应的getter/setter方法

## 验证方法

1. 创建一个包含`boolean`和`Boolean`字段的类
2. 使用`Cmd+Shift+D`生成JavaBean方法
3. 检查生成的getter方法名：
   - `boolean`字段应该生成`isXxx()`
   - `Boolean`字段应该生成`getXxx()`
4. 选中类名生成DTO，检查：
   - getter方法名是否正确
   - `toEntity()`和`fromEntity()`方法是否正确调用对应的getter/setter

## 参考资料

- [JavaBeans规范](https://docs.oracle.com/javase/8/docs/api/java/beans/package-summary.html)
- [阿里巴巴Java开发手册](https://github.com/alibaba/p3c)
- IntelliJ IDEA的JavaBean生成规则

