# OneClick Code Generator 开发文档

## 🏗️ 项目结构

```
OneClick/
├── src/main/java/com/glowxq/plugs/
│   ├── actions/                    # Action类
│   │   ├── GenerateJavaBeanMethodsAction.java
│   │   ├── FoldJavaBeanMethodsAction.java
│   │   └── TestAction.java
│   ├── settings/                   # 设置相关
│   │   ├── OneClickSettings.java
│   │   ├── OneClickSettingsConfigurable.java
│   │   └── OneClickSettingsComponent.java
│   └── utils/                      # 工具类
│       ├── JavaBeanUtils.java
│       ├── ClassTypeDetector.java
│       └── LoggerGenerator.java
├── src/main/resources/
│   └── META-INF/
│       └── plugin.xml              # 插件配置
├── src/test/java/                  # 测试类
├── docs/                           # 文档目录
└── build.gradle                    # 构建配置
```

## 🔧 核心组件

### 1. Action类

#### GenerateJavaBeanMethodsAction
- **功能**: 主要的代码生成Action
- **触发**: 快捷键 Ctrl+Alt+G / Cmd+Option+G
- **流程**:
  1. 获取当前类的PSI元素
  2. 检测类类型（JavaBean vs 业务类）
  3. 根据设置生成相应代码
  4. 插入到正确位置

#### FoldJavaBeanMethodsAction
- **功能**: 折叠JavaBean方法
- **触发**: 快捷键 Ctrl+Alt+F / Cmd+Option+F

### 2. 设置系统

#### OneClickSettings
- **功能**: 设置数据持久化
- **实现**: PersistentStateComponent
- **存储**: IntelliJ的设置系统

#### OneClickSettingsConfigurable
- **功能**: 设置面板配置
- **位置**: Tools → OneClick Code Generator

#### OneClickSettingsComponent
- **功能**: 设置UI组件
- **技术**: IntelliJ UI DSL

### 3. 工具类

#### JavaBeanUtils
- **功能**: JavaBean方法生成
- **方法**:
  - `generateGetterCode()`: 生成getter方法
  - `generateSetterCode()`: 生成setter方法
  - `generateToStringCode()`: 生成toString方法
  - `generateFluentSetterCode()`: 生成fluent setter

#### ClassTypeDetector
- **功能**: 智能类型检测
- **算法**: 基于多特征评分
- **特征**:
  - 包名模式
  - 注解检测
  - 字段/方法比例
  - 继承关系

#### LoggerGenerator
- **功能**: 日志字段生成
- **支持框架**:
  - SLF4J
  - Log4j
  - Java Util Logging

## 🔍 技术实现

### PSI API使用

```java
// 获取类的字段
PsiField[] fields = psiClass.getFields();

// 创建方法
PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
PsiMethod method = factory.createMethodFromText(methodCode, psiClass);

// 插入元素
PsiElement inserted = psiClass.addAfter(method, anchor);

// 格式化代码
CodeStyleManager.getInstance(project).reformat(inserted);
```

### 设置持久化

```java
@State(
    name = "OneClickSettings",
    storages = @Storage("oneclick-settings.xml")
)
public class OneClickSettings implements PersistentStateComponent<OneClickSettings> {
    // 设置字段
    public boolean generateSeparatorComment = true;
    
    @Override
    public OneClickSettings getState() {
        return this;
    }
    
    @Override
    public void loadState(@NotNull OneClickSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
```

### UI组件创建

```java
public JComponent createComponent() {
    return panel {
        group("JavaBean生成设置") {
            row {
                checkBox("生成分割注释", settings::generateSeparatorComment)
            }
            row {
                checkBox("生成getter/setter方法", settings::generateGetterSetter)
            }
        }
    };
}
```

## 🧪 测试策略

### 测试类结构
```
src/test/java/com/glowxq/plugs/
├── model/          # JavaBean测试类
│   └── UserEntity.java
├── dto/            # DTO测试类
│   └── UserDTO.java
├── service/        # 业务类测试类
│   └── UserService.java
└── controller/     # 控制器测试类
    └── UserController.java
```

### 测试场景
1. **JavaBean生成测试**
   - 基本getter/setter生成
   - toString方法生成
   - 分割注释生成
   - Fluent setter生成

2. **业务类生成测试**
   - 日志字段生成
   - 不同日志框架支持
   - 字段插入位置

3. **类型检测测试**
   - 包名检测
   - 注解检测
   - 字段/方法比例检测

4. **设置功能测试**
   - 设置持久化
   - UI组件功能
   - 设置应用效果

## 🚀 构建和部署

### 开发环境
```bash
# 构建项目
./gradlew build

# 运行IDE进行测试
./gradlew runIde

# 构建插件包
./gradlew buildPlugin
```

### 插件配置
```xml
<!-- plugin.xml -->
<idea-plugin>
    <id>com.glowxq.plugs.oneclick</id>
    <name>OneClick Code Generator</name>
    
    <extensions defaultExtensionNs="com.intellij">
        <applicationConfigurable 
            instance="com.glowxq.plugs.settings.OneClickSettingsConfigurable"/>
        <applicationService 
            serviceImplementation="com.glowxq.plugs.settings.OneClickSettings"/>
    </extensions>
    
    <actions>
        <action class="com.glowxq.plugs.actions.GenerateJavaBeanMethodsAction">
            <keyboard-shortcut keymap="$default" first-keystroke="ctrl alt G"/>
        </action>
    </actions>
</idea-plugin>
```

## 🔄 扩展开发

### 添加新的代码生成功能
1. 在`JavaBeanUtils`中添加生成方法
2. 在`OneClickSettings`中添加配置选项
3. 在`OneClickSettingsComponent`中添加UI控件
4. 在`GenerateJavaBeanMethodsAction`中调用新功能

### 添加新的日志框架支持
1. 在`LoggerGenerator.LoggerType`中添加新枚举
2. 实现对应的生成逻辑
3. 更新设置UI

### 添加新的toString风格
1. 在`OneClickSettings.ToStringStyle`中添加新枚举
2. 在`JavaBeanUtils`中实现生成方法
3. 更新设置UI

## 📋 代码规范

### 命名规范
- 类名：PascalCase
- 方法名：camelCase
- 常量：UPPER_SNAKE_CASE
- 包名：小写，用点分隔

### 注释规范
- 所有public方法必须有JavaDoc
- 复杂逻辑需要行内注释
- 类级别注释说明功能和用途

### 异常处理
- 使用try-catch处理可能的异常
- 记录错误日志
- 向用户显示友好的错误信息

## 🐛 调试技巧

### 日志输出
```java
// 使用IntelliJ的日志系统
Logger LOG = Logger.getInstance(GenerateJavaBeanMethodsAction.class);
LOG.info("Generating code for class: " + psiClass.getName());
LOG.warn("No fields found in class: " + psiClass.getName());
LOG.error("Failed to generate code", exception);
```

### PSI调试
- 使用PSI Viewer插件查看PSI结构
- 在代码中打印PSI元素信息
- 使用断点调试PSI操作

### 设置调试
- 检查设置文件：`~/.IntelliJIdea/config/options/oneclick-settings.xml`
- 验证设置加载和保存
- 测试UI组件绑定
