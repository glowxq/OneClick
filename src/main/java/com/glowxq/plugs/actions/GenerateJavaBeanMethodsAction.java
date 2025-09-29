package com.glowxq.plugs.actions;

import com.glowxq.plugs.settings.OneClickSettings;
import com.glowxq.plugs.utils.ClassTypeDetector;
import com.glowxq.plugs.utils.JavaBeanUtils;
import com.glowxq.plugs.utils.LoggerGenerator;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.openapi.ui.Messages;

import java.util.List;

/**
 * 生成JavaBean方法的Action
 * @author glowxq
 */
public class GenerateJavaBeanMethodsAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        try {
            Project project = e.getProject();
            Editor editor = e.getData(CommonDataKeys.EDITOR);
            PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

            if (project == null || editor == null || psiFile == null) {
                Messages.showErrorDialog("无法获取项目信息", "错误");
                return;
            }

            // 检查是否为Java文件（使用文件名检查，更加健壮）
            if (!psiFile.getName().endsWith(".java")) {
                Messages.showErrorDialog(project, "请在Java文件中使用此功能", "错误");
                return;
            }

            // 额外检查：确保是PsiJavaFile类型
            if (!(psiFile instanceof PsiJavaFile)) {
                Messages.showErrorDialog(project, "当前文件不是有效的Java文件", "错误");
                return;
            }

            // 检查是否有选中的文本
            String selectedText = editor.getSelectionModel().getSelectedText();
            if (selectedText != null && !selectedText.trim().isEmpty()) {
                // 有选中文本，执行智能操作
                handleSelectedText(project, editor, psiFile, selectedText);
                return;
            }

            // 没有选中文本，执行原有的类级别操作
            // 获取当前光标位置的类
            PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
            PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);

            if (psiClass == null) {
                Messages.showErrorDialog(project, "请将光标放在类定义内", "错误");
                return;
            }

            // 执行生成操作
            String[] result = new String[1]; // 用于存储结果消息
            WriteCommandAction.runWriteCommandAction(project, () -> {
                result[0] = generateCodeForClass(project, psiClass);
            });

            Messages.showInfoMessage(project, result[0], "成功");
        } catch (Exception ex) {
            Messages.showErrorDialog("智能一键生成时发生错误: " + ex.getMessage(), "错误");
            ex.printStackTrace();
        }
    }

    /**
     * 处理选中的文本
     */
    private void handleSelectedText(Project project, Editor editor, PsiFile psiFile, String selectedText) {
        try {
            // 获取选中文本的位置信息
            int startOffset = editor.getSelectionModel().getSelectionStart();
            int endOffset = editor.getSelectionModel().getSelectionEnd();
            PsiElement elementAtStart = psiFile.findElementAt(startOffset);

            if (elementAtStart == null) {
                Messages.showErrorDialog(project, "无法确定选中内容的上下文", "错误");
                return;
            }

            // 检查选中的是什么类型的元素
            PsiElement parent = elementAtStart.getParent();

            // 检查是否选中了类名、字段名或方法名
            if (isIdentifierSelection(elementAtStart, parent)) {
                // 切换命名风格
                toggleNamingStyle(project, editor, selectedText, startOffset, endOffset);
                return;
            }

            // 检查是否选中了字符串字面量
            if (isStringLiteral(selectedText)) {
                // 生成常量字段并替换
                generateConstantField(project, editor, psiFile, selectedText, startOffset, endOffset);
                return;
            }

            Messages.showInfoMessage(project, "智能识别：选中的内容类型暂不支持", "提示");

        } catch (Exception ex) {
            Messages.showErrorDialog("处理选中文本时发生错误: " + ex.getMessage(), "错误");
            ex.printStackTrace();
        }
    }

    /**
     * 检查是否是标识符选择（类名、字段名、方法名）
     */
    private boolean isIdentifierSelection(PsiElement element, PsiElement parent) {
        // 检查是否是标识符
        if (element.getNode().getElementType().toString().equals("IDENTIFIER")) {
            return true;
        }

        // 检查父元素类型
        return parent instanceof PsiClass ||
               parent instanceof PsiField ||
               parent instanceof PsiMethod ||
               parent instanceof PsiVariable;
    }

    /**
     * 检查是否是字符串字面量
     */
    private boolean isStringLiteral(String text) {
        // 简单检查：包含字母且不是纯数字
        return text.matches(".*[a-zA-Z].*") && !text.matches("\\d+");
    }

    /**
     * 切换命名风格
     */
    private void toggleNamingStyle(Project project, Editor editor, String selectedText, int startOffset, int endOffset) {
        WriteCommandAction.runWriteCommandAction(project, () -> {
            String convertedText;

            if (selectedText.contains("_")) {
                // 下划线转驼峰
                convertedText = underscoreToCamelCase(selectedText);
            } else if (selectedText.matches(".*[a-z][A-Z].*")) {
                // 驼峰转下划线
                convertedText = camelCaseToUnderscore(selectedText);
            } else {
                // 默认转为驼峰
                convertedText = underscoreToCamelCase(selectedText);
            }

            // 替换选中的文本
            editor.getDocument().replaceString(startOffset, endOffset, convertedText);
        });

        // 不再显示弹窗提示，静默执行
    }

    /**
     * 下划线转驼峰
     */
    private String underscoreToCamelCase(String text) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;

        for (char c : text.toCharArray()) {
            if (c == '_') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }

        return result.toString();
    }

    /**
     * 驼峰转下划线
     */
    private String camelCaseToUnderscore(String text) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                result.append('_');
            }
            result.append(Character.toLowerCase(c));
        }

        return result.toString();
    }

    /**
     * 生成常量字段（不替换选中文本）
     */
    private void generateConstantField(Project project, Editor editor, PsiFile psiFile, String selectedText, int startOffset, int endOffset) {
        // 获取当前类
        PsiElement element = psiFile.findElementAt(startOffset);
        PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);

        if (psiClass == null) {
            Messages.showErrorDialog(project, "无法找到当前类", "错误");
            return;
        }

        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                // 生成常量名（转为大写下划线格式）
                String constantName = generateConstantName(selectedText);

                // 检查常量是否已存在
                PsiField existingField = psiClass.findFieldByName(constantName, false);
                if (existingField != null) {
                    Messages.showInfoMessage(project, "常量 " + constantName + " 已存在", "提示");
                    return;
                }

                // 创建常量字段
                PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
                String fieldText = String.format("private static final String %s = \"%s\";", constantName, selectedText);
                PsiField constantField = factory.createFieldFromText(fieldText, psiClass);

                // 找到插入位置（如果有LOGGER，放在LOGGER下一行；否则放在类的最上面）
                PsiElement insertionPoint = findConstantInsertionPoint(psiClass);

                if (insertionPoint != null) {
                    psiClass.addAfter(constantField, insertionPoint);
                } else {
                    psiClass.add(constantField);
                }

                Messages.showInfoMessage(project, "已生成常量字段: " + constantName, "成功");

            } catch (Exception ex) {
                Messages.showErrorDialog("生成常量字段时发生错误: " + ex.getMessage(), "错误");
            }
        });
    }

    /**
     * 生成常量名
     */
    private String generateConstantName(String text) {
        // 移除特殊字符，转为大写下划线格式
        String cleaned = text.replaceAll("[^a-zA-Z0-9\\s]", "")
                             .replaceAll("\\s+", "_")
                             .toUpperCase();

        // 确保以字母开头
        if (!cleaned.isEmpty() && Character.isDigit(cleaned.charAt(0))) {
            cleaned = "CONST_" + cleaned;
        }

        return cleaned.isEmpty() ? "CONSTANT" : cleaned;
    }

    /**
     * 找到常量字段的插入位置（如果有LOGGER，放在LOGGER下一行；否则放在类的最上面）
     */
    private PsiElement findConstantInsertionPoint(PsiClass psiClass) {
        // 首先查找LOGGER字段
        PsiField loggerField = null;
        for (PsiField field : psiClass.getFields()) {
            if (field.hasModifierProperty(PsiModifier.STATIC) &&
                field.hasModifierProperty(PsiModifier.FINAL) &&
                "LOGGER".equals(field.getName())) {
                loggerField = field;
                break;
            }
        }

        // 如果找到LOGGER字段，在其后插入
        if (loggerField != null) {
            return loggerField;
        }

        // 否则在类的开始位置插入
        PsiElement lBrace = psiClass.getLBrace();
        if (lBrace != null) {
            return lBrace;
        }
        return null;
    }

    /**
     * 为类生成代码（根据类型自动判断）
     * @return 生成结果消息
     */
    private String generateCodeForClass(Project project, PsiClass psiClass) {
        OneClickSettings settings = OneClickSettings.getInstance();

        // 检测类类型
        ClassTypeDetector.ClassType classType = ClassTypeDetector.ClassType.UNKNOWN;
        if (settings.isAutoDetectClassType()) {
            classType = ClassTypeDetector.detectClassType(psiClass);
        }

        // 首先执行字段重新排列（对所有类型都执行，内部会判断是否为业务类）
        JavaBeanUtils.rearrangeFieldsPhysically(psiClass);

        StringBuilder resultMessage = new StringBuilder();

        // 根据类型生成相应的代码
        if (classType == ClassTypeDetector.ClassType.JAVA_BEAN || !settings.isAutoDetectClassType()) {
            // 生成JavaBean方法
            String javaBeanResult = generateJavaBeanMethods(project, psiClass);
            resultMessage.append(javaBeanResult);
        }

        if (classType == ClassTypeDetector.ClassType.BUSINESS_CLASS || !settings.isAutoDetectClassType()) {
            // 生成业务类代码（如日志字段）
            String businessResult = generateBusinessClassCode(project, psiClass);
            if (businessResult != null && !businessResult.isEmpty()) {
                if (resultMessage.length() > 0) {
                    resultMessage.append("\n");
                }
                resultMessage.append(businessResult);
            }
        }

        if (resultMessage.length() == 0) {
            return "未生成任何代码";
        }

        return resultMessage.toString();
    }

    /**
     * 生成JavaBean方法
     * @return 生成结果消息
     */
    private String generateJavaBeanMethods(Project project, PsiClass psiClass) {
        OneClickSettings settings = OneClickSettings.getInstance();
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);

        List<PsiField> fields = JavaBeanUtils.getInstanceFields(psiClass);

        if (fields.isEmpty()) {
            return "没有找到需要生成getter/setter的字段";
        }

        // 1. 收集并移除所有现有的getter/setter/toString方法
        List<PsiMethod> existingJavaBeanMethods = JavaBeanUtils.getAllJavaBeanMethods(psiClass, fields);
        int existingGetterCount = 0;
        int existingSetterCount = 0;
        int existingToStringCount = 0;

        for (PsiMethod method : existingJavaBeanMethods) {
            if (JavaBeanUtils.isGetterMethod(method)) {
                existingGetterCount++;
            } else if (JavaBeanUtils.isSetterMethod(method)) {
                existingSetterCount++;
            } else if (JavaBeanUtils.isToStringMethod(method)) {
                existingToStringCount++;
            }
            method.delete();
        }

        // 2. 移除现有的分割注释（如果存在）
        if (settings.isGenerateSeparatorComment()) {
            JavaBeanUtils.removeSeparatorComment(psiClass);
        }

        // 3. 找到插入JavaBean方法的最佳位置（在业务方法之后）
        PsiElement insertionPoint = JavaBeanUtils.findInsertionPoint(psiClass, fields);

        // 4. 检查是否需要添加分割注释（只有当存在业务方法时才添加）
        List<PsiMethod> businessMethods = JavaBeanUtils.getBusinessMethods(psiClass, fields);
        PsiElement lastInserted = insertionPoint;

        if (settings.isGenerateSeparatorComment() && !businessMethods.isEmpty()) {
            // 添加格式化的分割注释
            PsiElement commentElement = JavaBeanUtils.createFormattedSeparatorComment(factory, psiClass);
            lastInserted = JavaBeanUtils.insertCommentAfter(psiClass, commentElement, lastInserted);
        }

        int newGetterCount = 0;
        int newSetterCount = 0;

        // 5. 按字段顺序重新生成所有getter和setter方法（在正确位置插入）
        if (settings.isGenerateGetterSetter()) {
            for (PsiField field : fields) {
                String fieldName = field.getName();

                // 生成getter方法
                String getterCode = JavaBeanUtils.generateGetterCode(field);
                PsiMethod getterMethod = factory.createMethodFromText(getterCode, psiClass);
                lastInserted = JavaBeanUtils.insertAfter(psiClass, getterMethod, lastInserted);
                newGetterCount++;
                System.out.println("Generated getter for field: " + fieldName);

                // 生成setter方法
                String setterCode;
                if (settings.isGenerateFluentSetters()) {
                    setterCode = JavaBeanUtils.generateFluentSetterCode(field, psiClass);
                } else {
                    setterCode = JavaBeanUtils.generateSetterCode(field);
                }
                PsiMethod setterMethod = factory.createMethodFromText(setterCode, psiClass);
                lastInserted = JavaBeanUtils.insertAfter(psiClass, setterMethod, lastInserted);
                newSetterCount++;
                System.out.println("Generated setter for field: " + fieldName);
            }
        }

        // 6. 生成新的toString方法（放在最后）
        int newToStringCount = 0;
        if (settings.isGenerateToString() && !fields.isEmpty()) {
            String toStringCode;
            switch (settings.getToStringStyle()) {
                case "simple":
                    toStringCode = JavaBeanUtils.generateSimpleToStringCode(psiClass);
                    break;
                case "apache":
                    toStringCode = JavaBeanUtils.generateApacheToStringCode(psiClass);
                    break;
                default: // json
                    toStringCode = JavaBeanUtils.generateToStringCode(psiClass);
                    break;
            }
            PsiMethod toStringMethod = factory.createMethodFromText(toStringCode, psiClass);
            JavaBeanUtils.insertAfter(psiClass, toStringMethod, lastInserted);
            newToStringCount = 1;
        }

        // 显示详细的生成结果
        StringBuilder message = new StringBuilder("JavaBean方法生成完成！\n");

        if (settings.isGenerateGetterSetter()) {
            message.append(String.format("- 移除了 %d 个旧的getter方法，重新生成了 %d 个\n",
                existingGetterCount, newGetterCount));
            message.append(String.format("- 移除了 %d 个旧的setter方法，重新生成了 %d 个\n",
                existingSetterCount, newSetterCount));
        }

        if (settings.isGenerateToString()) {
            message.append(String.format("- %s toString方法（%s风格）\n",
                newToStringCount > 0 ? "重新生成了" : "未生成", settings.getToStringStyle()));
        }

        if (settings.isGenerateSeparatorComment()) {
            String separatorInfo = businessMethods.isEmpty() ?
                "- 未添加分割注释（无业务方法）" :
                "- 添加了分割注释便于区分业务逻辑";
            message.append(separatorInfo).append("\n");
        }

        message.append("- 所有JavaBean方法已正确插入到业务方法之后");

        // 处理内部类
        int innerClassCount = 0;
        if (settings.isProcessInnerClasses()) {
            PsiClass[] innerClasses = psiClass.getInnerClasses();
            for (PsiClass innerClass : innerClasses) {
                if (innerClass != null && !innerClass.isInterface() && !innerClass.isEnum()) {
                    innerClassCount++;
                }
            }
            if (innerClassCount > 0) {
                JavaBeanUtils.processInnerClasses(psiClass, settings);
                message.append("\n- 处理了 ").append(innerClassCount).append(" 个内部类");
            }
        }

        System.out.println(message.toString());
        return message.toString();
    }

    /**
     * 生成业务类代码
     * @return 生成结果消息
     */
    private String generateBusinessClassCode(Project project, PsiClass psiClass) {
        OneClickSettings settings = OneClickSettings.getInstance();
        StringBuilder message = new StringBuilder();

        // 生成日志字段
        if (settings.isGenerateLogger()) {
            LoggerGenerator.LoggerType loggerType = LoggerGenerator.getLoggerType(settings.getLoggerType());
            PsiElement loggerField = LoggerGenerator.insertLoggerField(psiClass, settings.getLoggerFieldName(), loggerType);

            if (loggerField != null) {
                message.append(String.format("- 添加了%s日志字段：%s\n",
                    settings.getLoggerType().toUpperCase(), settings.getLoggerFieldName()));
            } else {
                message.append("- 日志字段已存在，未重复添加\n");
            }
        }

        // 生成serialVersionUID（如果类实现了Serializable）
        if (settings.isGenerateSerialVersionUID() && isSerializable(psiClass)) {
            if (!hasSerialVersionUID(psiClass)) {
                PsiField serialVersionUID = generateSerialVersionUID(project, psiClass);
                if (serialVersionUID != null) {
                    message.append("- 添加了serialVersionUID字段\n");
                }
            }
        }

        return message.toString();
    }

    /**
     * 检查类是否实现了Serializable接口
     */
    private boolean isSerializable(PsiClass psiClass) {
        PsiClassType[] interfaces = psiClass.getImplementsListTypes();
        for (PsiClassType interfaceType : interfaces) {
            if ("java.io.Serializable".equals(interfaceType.getCanonicalText())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查类是否已经有serialVersionUID字段
     */
    private boolean hasSerialVersionUID(PsiClass psiClass) {
        PsiField[] fields = psiClass.getFields();
        for (PsiField field : fields) {
            if ("serialVersionUID".equals(field.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 生成serialVersionUID字段
     */
    private PsiField generateSerialVersionUID(Project project, PsiClass psiClass) {
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
        String serialVersionUIDCode = "private static final long serialVersionUID = 1L;";
        PsiField serialVersionUID = factory.createFieldFromText(serialVersionUIDCode, psiClass);

        // 插入到类的开始位置
        PsiElement lBrace = psiClass.getLBrace();
        if (lBrace != null) {
            return (PsiField) psiClass.addAfter(serialVersionUID, lBrace);
        }
        return null;
    }

    @Override
    public void update(AnActionEvent e) {
        // 简化逻辑：始终启用，让actionPerformed方法处理具体检查
        e.getPresentation().setEnabled(true);
        e.getPresentation().setVisible(true);
    }
}
