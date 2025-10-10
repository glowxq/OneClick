# 字段顺序生成测试指南

## ✅ 已修复的问题

### 问题描述
您要求：生成缺失的字段生成get set方法的时候要按照字段的顺序生成

### 解决方案

#### 1. **修复了字段获取顺序**
<augment_code_snippet path="src/main/java/com/glowxq/plugs/utils/JavaBeanUtils.java" mode="EXCERPT">
```java
public static List<PsiField> getInstanceFields(PsiClass psiClass) {
    // 获取所有字段并按照在源代码中的位置排序
    List<PsiField> allFields = Arrays.asList(psiClass.getFields());
    
    return allFields.stream()
            .filter(field -> !field.hasModifierProperty(PsiModifier.STATIC))
            .filter(field -> !field.hasModifierProperty(PsiModifier.FINAL))
            .sorted((field1, field2) -> {
                // 按照字段在源代码中的文本偏移量排序
                int offset1 = field1.getTextOffset();
                int offset2 = field2.getTextOffset();
                return Integer.compare(offset1, offset2);
            })
            .collect(Collectors.toList());
}
```
</augment_code_snippet>

#### 2. **优化了方法生成顺序**
现在按照以下顺序生成方法：
1. **按字段声明顺序，每个字段的getter和setter紧挨着生成**
2. **最后生成toString方法**

这样生成的代码更符合常见的JavaBean编码习惯，每个字段的getter和setter方法紧挨着，便于阅读和维护。

### 🎯 关键改进

| 改进点 | 之前 | 现在 |
|--------|------|------|
| **字段顺序** | 随机或按内部顺序 | ✅ 按源代码声明顺序 |
| **方法配对** | getter/setter分离 | ✅ getter和setter紧挨着 |
| **增量生成** | 可能顺序混乱 | ✅ 新方法按正确顺序插入 |
| **代码可读性** | 较差 | ✅ 结构清晰，易于维护 |

## 🧪 测试步骤

### 步骤1: 安装新版本
1. 卸载旧版本插件
2. 安装新构建的插件：`build/distributions/OneClick-1.3-SNAPSHOT.zip`
3. 重启IDEA

### 步骤2: 使用字段顺序测试类
我创建了专门的测试类：`src/test/java/com/glowxq/plugs/FieldOrderTestBean.java`

```java
public class FieldOrderTestBean {
    // 字段按照特定顺序声明
    private String firstName;      // 第1个字段
    private String lastName;       // 第2个字段
    private int age;              // 第3个字段
    private Date birthDate;       // 第4个字段
    private boolean active;       // 第5个字段
    private boolean isVip;        // 第6个字段
    private Double salary;        // 第7个字段
    private List<String> hobbies; // 第8个字段
}
```

### 步骤3: 测试字段顺序生成
1. **打开测试文件**：`FieldOrderTestBean.java`
2. **将光标放在类定义内**
3. **使用快捷键**：`Cmd+Option+G` (macOS)
4. **预期结果**：

#### 生成的方法顺序（getter和setter紧挨着）：
```java
// 第1个字段：firstName
public String getFirstName() { return firstName; }
public void setFirstName(String firstName) { this.firstName = firstName; }

// 第2个字段：lastName
public String getLastName() { return lastName; }
public void setLastName(String lastName) { this.lastName = lastName; }

// 第3个字段：age
public int getAge() { return age; }
public void setAge(int age) { this.age = age; }

// 第4个字段：birthDate
public Date getBirthDate() { return birthDate; }
public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }

// 第5个字段：active
public boolean isActive() { return active; }
public void setActive(boolean active) { this.active = active; }

// 第6个字段：isVip
public boolean isVip() { return isVip; }
public void setVip(boolean isVip) { this.isVip = isVip; }  // 注意setter名称

// 第7个字段：salary
public Double getSalary() { return salary; }
public void setSalary(Double salary) { this.salary = salary; }

// 第8个字段：hobbies
public List<String> getHobbies() { return hobbies; }
public void setHobbies(List<String> hobbies) { this.hobbies = hobbies; }
```

#### 最后生成toString方法：
```java
@Override
public String toString() {
    return "{" +
            "\"firstName\":\"" + firstName + "\"" + "," +
            "\"lastName\":\"" + lastName + "\"" + "," +
            "\"age\":" + age + "," +
            "\"birthDate\":" + birthDate + "," +
            "\"active\":" + active + "," +
            "\"isVip\":" + isVip + "," +
            "\"salary\":" + salary + "," +
            "\"hobbies\":" + hobbies +
            "}";
}
```

### 步骤4: 验证方法顺序
生成完成后，检查：
1. ✅ **Getter方法按字段声明顺序排列**
2. ✅ **Setter方法按字段声明顺序排列**
3. ✅ **所有getter方法在setter方法之前**
4. ✅ **toString方法在最后**

## 🔍 技术实现细节

### 字段排序机制
- 使用 `field.getTextOffset()` 获取字段在源代码中的位置
- 按照文本偏移量排序，确保与源代码声明顺序一致
- 过滤掉static和final字段

### 方法生成策略
1. **单次遍历字段列表**：
   - 为每个字段依次生成getter和setter方法
   - getter和setter紧挨着生成
2. **保持字段顺序**：严格按照字段在源代码中的声明顺序
3. **代码组织**：字段1的getter+setter → 字段2的getter+setter → ... → toString

## 📋 测试清单

请验证以下功能：

### ✅ 字段顺序测试
- [ ] 字段按声明顺序获取
- [ ] Getter方法按字段顺序生成
- [ ] Setter方法按字段顺序生成
- [ ] toString方法中字段按顺序排列

### ✅ 方法配对测试
- [ ] 每个字段的getter和setter紧挨着
- [ ] getter在前，setter在后
- [ ] toString方法在最后

### ✅ 增量生成测试
- [ ] 部分字段已有方法时，新生成的方法仍按顺序
- [ ] 不会重复生成已存在的方法
- [ ] 新方法插入到正确位置

### ✅ 特殊字段测试
- [ ] boolean字段的getter/setter命名正确
- [ ] isVip字段生成isVip()/setVip()
- [ ] 复杂类型（List、Date等）处理正确

## 🎯 预期效果

使用新版本插件后，生成的代码应该具有以下特点：

1. **有序性**：方法按照字段声明顺序排列
2. **配对性**：每个字段的getter和setter紧挨着
3. **一致性**：每次生成的顺序都相同
4. **可读性**：代码结构清晰，易于阅读和维护

## 📞 反馈请求

测试完成后，请告诉我：

1. **字段顺序是否正确？**
2. **方法分组是否符合预期？**
3. **增量生成时顺序是否保持？**
4. **是否还有其他顺序相关的问题？**

如果顺序仍有问题，请提供：
- 字段声明的顺序
- 实际生成的方法顺序
- 期望的方法顺序

这样我可以进一步优化排序逻辑。
