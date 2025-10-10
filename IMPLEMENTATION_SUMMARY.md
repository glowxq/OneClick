# @Serial 注解配置功能实现总结

## 需求描述

在生成 DTO/VO/BO 类时，`@Serial` 注解在 JDK 1.4 才引入（实际是 JDK 14），但市面上大部分项目还在使用 JDK 1.8。因此需要：
1. 默认不生成 `@Serial` 注解
2. 在设置面板中提供可配置选项

## 实现方案

### 1. 配置层修改

#### OneClickSettings.java
添加新的配置项：
```java
// DTO/VO/BO生成设置
public boolean generateSerialAnnotation = false; // 生成@Serial注解（JDK 14+），默认false兼容JDK 8
```

添加 getter/setter 方法：
```java
public boolean isGenerateSerialAnnotation() {
    return myState.generateSerialAnnotation;
}

public void setGenerateSerialAnnotation(boolean generateSerialAnnotation) {
    myState.generateSerialAnnotation = generateSerialAnnotation;
}
```

### 2. UI 层修改

#### OneClickSettingsComponent.java
1. 添加 UI 组件：
```java
private final JBCheckBox generateSerialAnnotation = new JBCheckBox();
```

2. 添加国际化文本绑定：
```java
generateSerialAnnotation.setText(I18nUtils.message("settings.dto.generate.serial.annotation"));
```

3. 添加到 DTO 生成面板：
```java
private JPanel createDtoGenerationPanel() {
    return FormBuilder.createFormBuilder()
            .addComponent(useBeanUtilsForConversion)
            .addLabeledComponent(new JBLabel(I18nUtils.message("settings.dto.beanutils.class")), beanUtilsClass)
            .addComponent(generateSerialAnnotation)  // 新增
            .getPanel();
}
```

4. 添加 getter/setter 方法：
```java
public boolean isGenerateSerialAnnotation() {
    return generateSerialAnnotation.isSelected();
}

public void setGenerateSerialAnnotation(boolean selected) {
    generateSerialAnnotation.setSelected(selected);
}
```

#### OneClickSettingsConfigurable.java
1. 在 `isModified()` 中添加检查：
```java
mySettingsComponent.isGenerateSerialAnnotation() != settings.isGenerateSerialAnnotation();
```

2. 在 `apply()` 中保存设置：
```java
settings.setGenerateSerialAnnotation(mySettingsComponent.isGenerateSerialAnnotation());
```

3. 在 `reset()` 中加载设置：
```java
mySettingsComponent.setGenerateSerialAnnotation(settings.isGenerateSerialAnnotation());
```

### 3. 代码生成逻辑修改

#### GenerateJavaBeanMethodsAction.java

1. 根据配置决定是否导入 `@Serial` 注解：
```java
// 获取设置
OneClickSettings settings = OneClickSettings.getInstance();

// 收集需要导入的类型
Set<String> imports = new LinkedHashSet<>();
imports.add("java.io.Serializable");

// 根据设置决定是否添加@Serial注解的导入
if (settings.isGenerateSerialAnnotation()) {
    imports.add("java.io.Serial");
}
```

2. 根据配置决定是否生成 `@Serial` 注解：
```java
// serialVersionUID - 根据设置决定是否添加@Serial注解
if (settings.isGenerateSerialAnnotation()) {
    sb.append("    @Serial\n");
}
sb.append("    private static final long serialVersionUID = 1L;\n\n");
```

### 4. 国际化资源文件

#### OneClickBundle.properties（中文）
```properties
settings.dto.generate.serial.annotation=生成 @Serial 注解（需要 JDK 14+，默认关闭兼容 JDK 8）
```

#### OneClickBundle_en.properties（英文）
```properties
settings.dto.generate.serial.annotation=Generate @Serial Annotation (Requires JDK 14+, disabled by default for JDK 8 compatibility)
```

## 修改文件清单

1. ✅ `src/main/java/com/glowxq/plugs/settings/OneClickSettings.java`
2. ✅ `src/main/java/com/glowxq/plugs/settings/OneClickSettingsComponent.java`
3. ✅ `src/main/java/com/glowxq/plugs/settings/OneClickSettingsConfigurable.java`
4. ✅ `src/main/java/com/glowxq/plugs/actions/GenerateJavaBeanMethodsAction.java`
5. ✅ `src/main/resources/messages/OneClickBundle.properties`
6. ✅ `src/main/resources/messages/OneClickBundle_en.properties`

## 测试验证

### 编译测试
```bash
./gradlew build -x test
```
结果：✅ BUILD SUCCESSFUL

### 功能测试

#### 测试用例 1：默认配置（关闭 @Serial）
**前置条件：**
- 设置中"生成 @Serial 注解"未勾选（默认）

**操作步骤：**
1. 创建一个实体类 `User.java`
2. 使用快捷键生成 DTO
3. 检查生成的 `UserDTO.java`

**预期结果：**
```java
package com.example.entity.dto;

import java.io.Serializable;
import com.example.entity.User;

public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    // ...
}
```
- ✅ 不包含 `import java.io.Serial;`
- ✅ `serialVersionUID` 前没有 `@Serial` 注解

#### 测试用例 2：启用 @Serial 注解
**前置条件：**
- 在设置中勾选"生成 @Serial 注解"

**操作步骤：**
1. 创建一个实体类 `Product.java`
2. 使用快捷键生成 DTO
3. 检查生成的 `ProductDTO.java`

**预期结果：**
```java
package com.example.entity.dto;

import java.io.Serializable;
import java.io.Serial;
import com.example.entity.Product;

public class ProductDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // ...
}
```
- ✅ 包含 `import java.io.Serial;`
- ✅ `serialVersionUID` 前有 `@Serial` 注解

## 兼容性说明

### JDK 版本兼容性
| JDK 版本 | @Serial 支持 | 默认配置 | 推荐配置 |
|---------|-------------|---------|---------|
| JDK 8   | ❌          | 关闭    | 关闭    |
| JDK 11  | ❌          | 关闭    | 关闭    |
| JDK 14  | ✅          | 关闭    | 可选    |
| JDK 17  | ✅          | 关闭    | 可选    |
| JDK 21  | ✅          | 关闭    | 可选    |

### 向后兼容性
- ✅ 不影响现有配置
- ✅ 不影响已生成的代码
- ✅ 默认行为兼容 JDK 8

## 设计亮点

1. **默认兼容性优先**：默认关闭 @Serial 注解，确保在 JDK 8 环境下可用
2. **灵活可配置**：用户可以根据项目 JDK 版本自由选择
3. **国际化支持**：中英文双语提示，说明清晰
4. **代码解耦**：配置与生成逻辑分离，易于维护
5. **用户友好**：设置项有详细的提示信息

## 后续优化建议

1. **自动检测 JDK 版本**：
   - 可以根据项目的 JDK 版本自动推荐是否启用 @Serial 注解
   - 在 JDK 14+ 项目中提示用户可以启用此功能

2. **批量更新**：
   - 提供工具批量为已有的 DTO/VO/BO 类添加或移除 @Serial 注解

3. **代码检查**：
   - 添加代码检查规则，在 JDK 8 项目中使用 @Serial 时给出警告

## 总结

本次实现完整解决了 @Serial 注解在不同 JDK 版本下的兼容性问题：
- ✅ 默认关闭，兼容 JDK 8
- ✅ 可配置，支持 JDK 14+
- ✅ 国际化，用户体验好
- ✅ 编译通过，功能完整

用户现在可以根据项目的实际 JDK 版本，灵活选择是否生成 @Serial 注解，既保证了兼容性，又支持了现代 Java 的最佳实践。

