package com.glowxq.plugs.actions;

import com.glowxq.plugs.settings.OneClickSettings;
import com.glowxq.plugs.utils.ClassTypeDetector;
import com.glowxq.plugs.utils.I18nUtils;
import com.glowxq.plugs.utils.JavaBeanUtils;
import com.glowxq.plugs.utils.LoggerGenerator;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

            if (project == null) {
                Messages.showErrorDialog("无法获取项目信息", "错误");
                return;
            }

            // 如果没有PSI文件，尝试从虚拟文件获取
            if (psiFile == null) {
                VirtualFile virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
                if (virtualFile != null) {
                    if (virtualFile.isDirectory()) {
                        // 选中的是目录，使用批量生成逻辑
                        handleDirectorySelection(project, virtualFile);
                        return;
                    } else if (virtualFile.getName().endsWith(".java")) {
                        PsiManager psiManager = PsiManager.getInstance(project);
                        psiFile = psiManager.findFile(virtualFile);
                    }
                }
            }

            if (psiFile == null) {
                Messages.showErrorDialog("无法获取文件信息，请确保在Java文件中执行此操作", "错误");
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

            // 检查是否有选中的文本（只有在编辑器中才检查）
            if (editor != null) {
                String selectedText = editor.getSelectionModel().getSelectedText();
                if (selectedText != null && !selectedText.trim().isEmpty()) {
                    // 检查是否选中了类名
                    if (isClassNameSelected(editor, psiFile, selectedText)) {
                        // 选中了类名，生成DTO/VO类
                        handleClassSelection(project, editor, psiFile, selectedText);
                        return;
                    } else {
                        // 有选中文本，执行智能操作
                        handleSelectedText(project, editor, psiFile, selectedText);
                        return;
                    }
                }
            }

            // 没有选中文本或没有编辑器，执行类级别操作
            PsiClass psiClass = null;

            if (editor != null) {
                // 从编辑器获取当前光标位置的类
                PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
                psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
            } else {
                // 从文件获取第一个类
                PsiJavaFile javaFile = (PsiJavaFile) psiFile;
                PsiClass[] classes = javaFile.getClasses();
                if (classes.length > 0) {
                    psiClass = classes[0];
                }
            }

            if (psiClass == null) {
                Messages.showErrorDialog(project, "未找到可处理的类", "错误");
                return;
            }

            // 执行生成操作
            final PsiClass finalPsiClass = psiClass; // 创建final引用
            String[] result = new String[1]; // 用于存储结果消息
            WriteCommandAction.runWriteCommandAction(project, () -> {
                result[0] = performSmartGeneration(project, finalPsiClass);
            });

            Messages.showInfoMessage(project, result[0], "成功");
        } catch (Exception ex) {
            Messages.showErrorDialog("智能一键生成时发生错误: " + ex.getMessage(), "错误");
            ex.printStackTrace();
        }
    }

    /**
     * 处理目录选择 - 批量生成
     */
    private void handleDirectorySelection(Project project, VirtualFile directory) {
        try {
            // 收集目录下的所有Java文件
            List<VirtualFile> javaFiles = new ArrayList<>();
            collectJavaFilesRecursively(directory, javaFiles);

            if (javaFiles.isEmpty()) {
                Messages.showInfoMessage(project, "所选目录中没有找到Java文件", "提示");
                return;
            }

            // 显示确认对话框
            int result = Messages.showYesNoDialog(project,
                "找到 " + javaFiles.size() + " 个Java文件，是否对所有文件执行智能一键生成？",
                "批量生成确认",
                Messages.getQuestionIcon());

            if (result != Messages.YES) {
                return;
            }

            // 执行批量生成
            performBatchGeneration(project, javaFiles);

        } catch (Exception ex) {
            Messages.showErrorDialog(project, "处理目录时发生错误: " + ex.getMessage(), "错误");
            ex.printStackTrace();
        }
    }

    /**
     * 递归收集Java文件
     */
    private void collectJavaFilesRecursively(VirtualFile directory, List<VirtualFile> javaFiles) {
        if (!directory.isDirectory()) {
            return;
        }

        VirtualFile[] children = directory.getChildren();
        for (VirtualFile child : children) {
            if (child.isDirectory()) {
                collectJavaFilesRecursively(child, javaFiles);
            } else if (child.getName().endsWith(".java")) {
                javaFiles.add(child);
            }
        }
    }

    /**
     * 执行批量生成
     */
    private void performBatchGeneration(Project project, List<VirtualFile> javaFiles) {
        PsiManager psiManager = PsiManager.getInstance(project);
        int successCount = 0;
        int failCount = 0;
        StringBuilder errorMessages = new StringBuilder();

        for (VirtualFile virtualFile : javaFiles) {
            try {
                PsiFile psiFile = psiManager.findFile(virtualFile);
                if (psiFile instanceof PsiJavaFile) {
                    PsiJavaFile javaFile = (PsiJavaFile) psiFile;
                    PsiClass[] classes = javaFile.getClasses();

                    for (PsiClass psiClass : classes) {
                        if (psiClass != null && !psiClass.isInterface() && !psiClass.isEnum()) {
                            WriteCommandAction.runWriteCommandAction(project, () -> {
                                performSmartGeneration(project, psiClass);
                            });
                            successCount++;
                        }
                    }
                }
            } catch (Exception e) {
                failCount++;
                errorMessages.append(virtualFile.getName()).append(": ").append(e.getMessage()).append("\n");
                System.err.println("处理文件失败: " + virtualFile.getName() + " - " + e.getMessage());
            }
        }

        // 显示结果
        String message = "批量生成完成！\n成功: " + successCount + " 个类\n失败: " + failCount + " 个类";
        if (failCount > 0) {
            message += "\n\n失败详情:\n" + errorMessages.toString();
        }
        Messages.showInfoMessage(project, message, "批量生成结果");
    }

    /**
     * 检查是否选中了类名
     */
    private boolean isClassNameSelected(Editor editor, PsiFile psiFile, String selectedText) {
        int startOffset = editor.getSelectionModel().getSelectionStart();
        PsiElement elementAtStart = psiFile.findElementAt(startOffset);

        if (elementAtStart == null) {
            return false;
        }

        // 检查是否在类声明中
        PsiClass psiClass = PsiTreeUtil.getParentOfType(elementAtStart, PsiClass.class);
        if (psiClass == null) {
            return false;
        }

        // 检查选中的文本是否是类名
        return selectedText.trim().equals(psiClass.getName());
    }

    /**
     * 处理类选择 - 生成DTO/VO类
     */
    private void handleClassSelection(Project project, Editor editor, PsiFile psiFile, String className) {
        try {
            // 获取当前类
            int startOffset = editor.getSelectionModel().getSelectionStart();
            PsiElement elementAtStart = psiFile.findElementAt(startOffset);
            PsiClass sourceClass = PsiTreeUtil.getParentOfType(elementAtStart, PsiClass.class);

            if (sourceClass == null) {
                Messages.showErrorDialog(project, "无法找到源类", "错误");
                return;
            }

            // 显示选择对话框：DTO、VO还是BO
            String[] options = {"DTO", "VO", "BO", "取消"};
            int choice = Messages.showChooseDialog(
                "选择要生成的类型：",
                "生成数据传输对象",
                options,
                options[0],
                Messages.getQuestionIcon()
            );

            if (choice == 3 || choice == -1) { // 取消
                return;
            }

            String suffix = options[choice];

            WriteCommandAction.runWriteCommandAction(project, () -> {
                try {
                    String result = generateDtoVoClass(project, sourceClass, suffix);
                    Messages.showInfoMessage(project, result, "生成成功");
                } catch (Exception e) {
                    Messages.showErrorDialog(project, "生成失败: " + e.getMessage(), "错误");
                }
            });

        } catch (Exception ex) {
            Messages.showErrorDialog(project, "处理类选择时发生错误: " + ex.getMessage(), "错误");
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
    public String performSmartGeneration(Project project, PsiClass psiClass) {
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
     * 生成DTO/VO类
     */
    private String generateDtoVoClass(Project project, PsiClass sourceClass, String suffix) throws Exception {
        // 获取源文件信息
        PsiFile sourceFile = sourceClass.getContainingFile();
        VirtualFile sourceVirtualFile = sourceFile.getVirtualFile();
        VirtualFile sourceDir = sourceVirtualFile.getParent();

        // 生成新类名
        String sourceClassName = sourceClass.getName();
        String newClassName = sourceClassName + suffix;
        String newFileName = newClassName + ".java";

        // 确定目标目录：在源目录下创建dto或vo子目录
        String subDirName = suffix.toLowerCase(); // "DTO" -> "dto", "VO" -> "vo"
        VirtualFile targetDir = sourceDir.findChild(subDirName);
        if (targetDir == null) {
            // 创建子目录
            targetDir = sourceDir.createChildDirectory(this, subDirName);
        }

        // 检查文件是否已存在
        VirtualFile existingFile = targetDir.findChild(newFileName);
        if (existingFile != null) {
            int choice = Messages.showYesNoDialog(
                project,
                "文件 " + newFileName + " 已存在，是否覆盖？",
                "文件已存在",
                Messages.getQuestionIcon()
            );
            if (choice != Messages.YES) {
                return "操作已取消";
            }
        }

        // 生成类内容
        String classContent = generateDtoVoClassContent(sourceClass, newClassName, suffix, subDirName);

        // 创建或覆盖文件
        if (existingFile != null) {
            // 覆盖现有文件
            existingFile.setBinaryContent(classContent.getBytes("UTF-8"));
        } else {
            // 创建新文件
            existingFile = targetDir.createChildData(this, newFileName);
            existingFile.setBinaryContent(classContent.getBytes("UTF-8"));
        }

        // 打开新创建的文件
        FileEditorManager.getInstance(project).openFile(existingFile, true);

        return "成功生成 " + suffix + " 类：" + newClassName + "\n文件位置：" + existingFile.getPath();
    }

    /**
     * 生成DTO/VO类的内容
     */
    private String generateDtoVoClassContent(PsiClass sourceClass, String newClassName, String suffix, String subDirName) {
        StringBuilder sb = new StringBuilder();

        // 获取包名
        PsiFile sourceFile = sourceClass.getContainingFile();
        String packageName = "";
        String sourceClassFullName = "";
        if (sourceFile instanceof PsiJavaFile) {
            PsiJavaFile javaFile = (PsiJavaFile) sourceFile;
            packageName = javaFile.getPackageName();
            sourceClassFullName = packageName.isEmpty() ? sourceClass.getName() : packageName + "." + sourceClass.getName();
        }

        // 包声明 - 添加子包
        if (!packageName.isEmpty()) {
            sb.append("package ").append(packageName).append(".").append(subDirName).append(";\n\n");
        } else {
            sb.append("package ").append(subDirName).append(";\n\n");
        }

        // 收集需要导入的类型
        Set<String> imports = new LinkedHashSet<>();
        imports.add("java.io.Serializable");

        // 添加源类的导入
        if (!packageName.isEmpty()) {
            imports.add(sourceClassFullName);
        }

        // 收集字段类型的导入
        List<PsiField> fields = JavaBeanUtils.getInstanceFields(sourceClass);
        for (PsiField field : fields) {
            String fieldType = field.getType().getCanonicalText();
            // 收集需要导入的类型
            collectImportsFromType(fieldType, imports, packageName + "." + subDirName);
        }

        // 从源文件收集导入语句
        if (sourceFile instanceof PsiJavaFile) {
            PsiJavaFile javaFile = (PsiJavaFile) sourceFile;
            PsiImportList importList = javaFile.getImportList();
            if (importList != null) {
                for (PsiImportStatement importStatement : importList.getImportStatements()) {
                    String importText = importStatement.getQualifiedName();
                    if (importText != null && !importText.isEmpty()) {
                        // 检查是否是字段类型需要的导入
                        for (PsiField field : fields) {
                            String fieldType = field.getType().getCanonicalText();
                            if (fieldType.contains(importText.substring(importText.lastIndexOf('.') + 1))) {
                                imports.add(importText);
                            }
                        }
                    }
                }
            }
        }

        // 输出导入语句
        for (String importStr : imports) {
            sb.append("import ").append(importStr).append(";\n");
        }
        sb.append("\n");

        // 类注释
        sb.append("/**\n");
        sb.append(" * ").append(sourceClass.getName()).append(" ").append(suffix).append(" 类\n");
        sb.append(" * 自动生成的数据传输对象\n");
        sb.append(" * \n");
        sb.append(" * @author OneClick Plugin\n");
        sb.append(" * @date ").append(new java.text.SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date())).append("\n");
        sb.append(" */\n");

        // 类声明
        sb.append("public class ").append(newClassName).append(" implements Serializable {\n\n");

        // serialVersionUID
        sb.append("    private static final long serialVersionUID = 1L;\n\n");

        // 生成字段（使用之前已经获取的fields变量）
        for (PsiField field : fields) {
            String fieldType = getSimpleTypeName(field.getType().getCanonicalText());
            String fieldName = field.getName();

            sb.append("    private ").append(fieldType).append(" ").append(fieldName).append(";\n");
        }

        if (!fields.isEmpty()) {
            sb.append("\n");
        }

        // 生成构造方法
        sb.append("    public ").append(newClassName).append("() {\n");
        sb.append("    }\n\n");

        // 生成getter和setter方法
        for (PsiField field : fields) {
            String fieldType = getSimpleTypeName(field.getType().getCanonicalText());
            String fieldName = field.getName();
            String capitalizedName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            // Getter
            sb.append("    public ").append(fieldType).append(" get").append(capitalizedName).append("() {\n");
            sb.append("        return ").append(fieldName).append(";\n");
            sb.append("    }\n\n");

            // Setter
            sb.append("    public void set").append(capitalizedName).append("(").append(fieldType).append(" ").append(fieldName).append(") {\n");
            sb.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
            sb.append("    }\n\n");
        }

        // 生成转换方法
        String sourceClassName = sourceClass.getName();
        sb.append("    /**\n");
        sb.append("     * 转换为实体类\n");
        sb.append("     */\n");
        sb.append("    public ").append(sourceClassName).append(" toEntity() {\n");
        sb.append("        ").append(sourceClassName).append(" entity = new ").append(sourceClassName).append("();\n");

        for (PsiField field : fields) {
            String fieldName = field.getName();
            String capitalizedName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            sb.append("        entity.set").append(capitalizedName).append("(this.").append(fieldName).append(");\n");
        }

        sb.append("        return entity;\n");
        sb.append("    }\n\n");

        // 生成从实体类转换的静态方法
        sb.append("    /**\n");
        sb.append("     * 从实体类转换\n");
        sb.append("     */\n");
        sb.append("    public static ").append(newClassName).append(" fromEntity(").append(sourceClassName).append(" entity) {\n");
        sb.append("        if (entity == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        ").append(newClassName).append(" ").append(suffix.toLowerCase()).append(" = new ").append(newClassName).append("();\n");

        for (PsiField field : fields) {
            String fieldName = field.getName();
            String capitalizedName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            sb.append("        ").append(suffix.toLowerCase()).append(".set").append(capitalizedName).append("(entity.get").append(capitalizedName).append("());\n");
        }

        sb.append("        return ").append(suffix.toLowerCase()).append(";\n");
        sb.append("    }\n");

        sb.append("}\n");

        return sb.toString();
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

    /**
     * 获取简单类型名称（去除包名）
     */
    private String getSimpleTypeName(String fullTypeName) {
        if (fullTypeName == null || fullTypeName.isEmpty()) {
            return fullTypeName;
        }

        // 处理泛型类型
        if (fullTypeName.contains("<")) {
            int genericStart = fullTypeName.indexOf("<");
            int genericEnd = fullTypeName.lastIndexOf(">");

            String baseType = fullTypeName.substring(0, genericStart);
            String genericPart = fullTypeName.substring(genericStart + 1, genericEnd);

            // 递归处理泛型参数
            String[] genericTypes = genericPart.split(",");
            StringBuilder simplifiedGeneric = new StringBuilder();
            for (int i = 0; i < genericTypes.length; i++) {
                if (i > 0) {
                    simplifiedGeneric.append(", ");
                }
                simplifiedGeneric.append(getSimpleTypeName(genericTypes[i].trim()));
            }

            return getSimpleClassName(baseType) + "<" + simplifiedGeneric.toString() + ">";
        }

        // 处理数组类型
        if (fullTypeName.endsWith("[]")) {
            String baseType = fullTypeName.substring(0, fullTypeName.length() - 2);
            return getSimpleTypeName(baseType) + "[]";
        }

        return getSimpleClassName(fullTypeName);
    }

    /**
     * 获取简单类名（去除包名）
     */
    private String getSimpleClassName(String fullClassName) {
        if (fullClassName == null || !fullClassName.contains(".")) {
            return fullClassName;
        }
        return fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
    }

    /**
     * 从类型字符串中收集需要导入的包
     */
    private void collectImportsFromType(String typeStr, Set<String> imports, String currentPackage) {
        // 移除泛型参数
        String baseType = typeStr;
        if (typeStr.contains("<")) {
            baseType = typeStr.substring(0, typeStr.indexOf("<"));
            // 处理泛型参数
            String genericPart = typeStr.substring(typeStr.indexOf("<") + 1, typeStr.lastIndexOf(">"));
            String[] genericTypes = genericPart.split(",");
            for (String genericType : genericTypes) {
                collectImportsFromType(genericType.trim(), imports, currentPackage);
            }
        }

        // 移除数组标记
        baseType = baseType.replace("[]", "").trim();

        // 跳过基本类型和java.lang包中的类
        if (isPrimitiveOrJavaLang(baseType)) {
            return;
        }

        // 如果包含包名，直接添加
        if (baseType.contains(".")) {
            // 不导入当前包的类
            if (!baseType.startsWith(currentPackage + ".")) {
                imports.add(baseType);
            }
        }
    }

    /**
     * 判断是否是基本类型或java.lang包中的类
     */
    private boolean isPrimitiveOrJavaLang(String type) {
        return type.equals("byte") || type.equals("short") || type.equals("int") ||
               type.equals("long") || type.equals("float") || type.equals("double") ||
               type.equals("boolean") || type.equals("char") || type.equals("void") ||
               type.equals("String") || type.equals("Object") || type.equals("Integer") ||
               type.equals("Long") || type.equals("Double") || type.equals("Float") ||
               type.equals("Boolean") || type.equals("Character") || type.equals("Byte") ||
               type.equals("Short");
    }

    @Override
    public void update(AnActionEvent e) {
        // 动态设置国际化文本
        e.getPresentation().setText("🚀 " + I18nUtils.message("action.smart.oneclick.text"));
        e.getPresentation().setDescription(I18nUtils.message("action.smart.oneclick.description"));

        // 简化逻辑：始终启用，让actionPerformed方法处理具体检查
        e.getPresentation().setEnabled(true);
        e.getPresentation().setVisible(true);
    }
}
