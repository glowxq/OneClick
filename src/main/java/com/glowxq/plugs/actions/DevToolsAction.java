package com.glowxq.plugs.actions;

import com.glowxq.plugs.utils.I18nUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiJavaDirectoryImpl;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ide.fileTemplates.JavaTemplateUtil;
import com.intellij.testFramework.LightVirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 开发工具集合Action
 * 提供各种实用的开发辅助功能
 */
public class DevToolsAction extends AnAction {

    private static final List<DevTool> DEV_TOOLS = Arrays.asList(
        new DevTool("🔧 生成UUID", "生成随机UUID", DevToolsAction::generateUUID),
        new DevTool("📅 插入时间戳", "插入当前时间戳", DevToolsAction::insertTimestamp),
        new DevTool("🔍 生成序列化ID", "生成serialVersionUID", DevToolsAction::generateSerialVersionUID),
        new DevTool("📝 生成TODO注释", "生成带时间的TODO注释", DevToolsAction::generateTodoComment),
        new DevTool("🎯 生成测试方法", "生成单元测试方法模板", DevToolsAction::generateTestMethod),
        new DevTool("🔒 生成常量", "将选中文本转换为常量", DevToolsAction::generateConstant),
        new DevTool("📊 生成枚举", "生成枚举类模板", DevToolsAction::generateEnum),
        new DevTool("🌐 生成JSON模板", "生成JSON数据模板", DevToolsAction::generateJsonTemplate),
        new DevTool("🔄 转换命名风格", "转换驼峰/下划线命名", DevToolsAction::convertNamingStyle),
        new DevTool("📋 生成Builder模式", "为当前类生成Builder模式", DevToolsAction::generateBuilderPattern)
    );

    public DevToolsAction() {
        super("Developer Tools", "Various development utilities", null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        ListPopup popup = JBPopupFactory.getInstance().createListPopup(
            new BaseListPopupStep<DevTool>("选择开发工具", DEV_TOOLS) {
                @Override
                public PopupStep onChosen(DevTool selectedValue, boolean finalChoice) {
                    selectedValue.action.execute(e);
                    return FINAL_CHOICE;
                }

                @NotNull
                @Override
                public String getTextFor(DevTool value) {
                    return value.name + " - " + value.description;
                }
            }
        );
        popup.showInBestPositionFor(e.getDataContext());
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }

    // 生成UUID
    private static void generateUUID(AnActionEvent e) {
        String uuid = UUID.randomUUID().toString();
        insertTextAtCursor(e, uuid);
        showNotification("UUID已生成: " + uuid);
    }

    // 插入时间戳
    private static void insertTimestamp(AnActionEvent e) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        insertTextAtCursor(e, timestamp);
        showNotification("时间戳已插入: " + timestamp);
    }

    // 生成serialVersionUID
    private static void generateSerialVersionUID(AnActionEvent e) {
        long serialVersionUID = System.currentTimeMillis();
        String code = "private static final long serialVersionUID = " + serialVersionUID + "L;";
        insertTextAtCursor(e, code);
        showNotification("serialVersionUID已生成");
    }

    // 生成TODO注释
    private static void generateTodoComment(AnActionEvent e) {
        String author = System.getProperty("user.name", "Developer");
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String todo = "// TODO: [" + author + " " + date + "] ";
        insertTextAtCursor(e, todo);
    }

    // 生成测试方法
    private static void generateTestMethod(AnActionEvent e) {
        String methodName = Messages.showInputDialog(
            "请输入测试方法名称:",
            "生成测试方法",
            Messages.getQuestionIcon()
        );
        
        if (methodName != null && !methodName.trim().isEmpty()) {
            String testMethod = String.format(
                "@Test\n" +
                "public void %s() {\n" +
                "    // Given\n" +
                "    \n" +
                "    // When\n" +
                "    \n" +
                "    // Then\n" +
                "    \n" +
                "}",
                methodName.trim()
            );
            insertTextAtCursor(e, testMethod);
            showNotification("测试方法已生成: " + methodName);
        }
    }

    // 生成常量
    private static void generateConstant(AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;

        String selectedText = editor.getSelectionModel().getSelectedText();
        if (selectedText == null || selectedText.trim().isEmpty()) {
            Messages.showWarningDialog("请先选中要转换为常量的文本", "生成常量");
            return;
        }

        String constantName = Messages.showInputDialog(
            "请输入常量名称:",
            "生成常量",
            Messages.getQuestionIcon(),
            selectedText.toUpperCase().replace(" ", "_"),
            null
        );

        if (constantName != null && !constantName.trim().isEmpty()) {
            String constant = String.format(
                "public static final String %s = \"%s\";",
                constantName.trim(),
                selectedText
            );
            insertTextAtCursor(e, constant);
            showNotification("常量已生成: " + constantName);
        }
    }

    // 生成枚举
    private static void generateEnum(AnActionEvent e) {
        String enumName = Messages.showInputDialog(
            "请输入枚举名称:",
            "生成枚举",
            Messages.getQuestionIcon()
        );

        if (enumName != null && !enumName.trim().isEmpty()) {
            createEnumFile(e, enumName.trim());
        }
    }

    // 生成JSON模板
    private static void generateJsonTemplate(AnActionEvent e) {
        String jsonTemplate = "{\n" +
            "    \"id\": 1,\n" +
            "    \"name\": \"示例名称\",\n" +
            "    \"status\": \"active\",\n" +
            "    \"createTime\": \"2024-01-01 12:00:00\",\n" +
            "    \"data\": {\n" +
            "        \"key\": \"value\"\n" +
            "    },\n" +
            "    \"list\": [\n" +
            "        \"item1\",\n" +
            "        \"item2\"\n" +
            "    ]\n" +
            "}";
        openInNewEditor(e, jsonTemplate, "template.json");
        showNotification("JSON模板已在新标签页中打开");
    }

    // 转换命名风格
    private static void convertNamingStyle(AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;

        String selectedText = editor.getSelectionModel().getSelectedText();
        if (selectedText == null || selectedText.trim().isEmpty()) {
            Messages.showWarningDialog("请先选中要转换的文本", "转换命名风格");
            return;
        }

        String[] options = {"驼峰转下划线", "下划线转驼峰", "首字母大写", "首字母小写"};
        int selectedIndex = Messages.showChooseDialog(
            "选择转换方式:",
            "转换命名风格",
            options,
            options[0],
            Messages.getQuestionIcon()
        );

        String selected = selectedIndex >= 0 ? options[selectedIndex] : null;

        if (selected != null) {
            String converted = convertText(selectedText.trim(), selected);
            replaceSelectedText(e, converted);
            showNotification("命名风格已转换: " + selected);
        }
    }

    /**
     * 创建枚举文件
     */
    private static void createEnumFile(AnActionEvent e, String enumName) {
        Project project = e.getProject();
        if (project == null) return;

        // 获取当前文件所在的目录
        PsiFile currentFile = e.getData(CommonDataKeys.PSI_FILE);
        if (currentFile == null) {
            showNotification("无法获取当前文件信息");
            return;
        }

        PsiDirectory directory = currentFile.getContainingDirectory();
        if (directory == null) {
            showNotification("无法获取当前目录信息");
            return;
        }

        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                // 获取包名
                String packageName = "";
                if (currentFile instanceof PsiJavaFile) {
                    packageName = ((PsiJavaFile) currentFile).getPackageName();
                }

                // 生成枚举内容
                String enumContent = generateEnumContent(enumName, packageName);

                // 创建文件
                PsiFile enumFile = PsiFileFactory.getInstance(project)
                    .createFileFromText(enumName + ".java", enumContent);

                // 添加到目录
                PsiElement addedFile = directory.add(enumFile);

                // 打开新创建的文件
                if (addedFile instanceof PsiFile) {
                    FileEditorManager.getInstance(project).openFile(
                        ((PsiFile) addedFile).getVirtualFile(), true);
                }

                showNotification("枚举文件已创建: " + enumName + ".java");
            } catch (Exception ex) {
                showNotification("创建枚举文件失败: " + ex.getMessage());
            }
        });
    }

    /**
     * 生成枚举内容
     */
    private static String generateEnumContent(String enumName, String packageName) {
        StringBuilder content = new StringBuilder();

        // 包声明
        if (!packageName.isEmpty()) {
            content.append("package ").append(packageName).append(";\n\n");
        }

        // 枚举类
        content.append("/**\n");
        content.append(" * ").append(enumName).append(" 枚举\n");
        content.append(" */\n");
        content.append("public enum ").append(enumName).append(" {\n");
        content.append("    // 枚举值\n");
        content.append("    VALUE1(\"value1\", \"描述1\"),\n");
        content.append("    VALUE2(\"value2\", \"描述2\");\n");
        content.append("\n");
        content.append("    private final String code;\n");
        content.append("    private final String description;\n");
        content.append("\n");
        content.append("    ").append(enumName).append("(String code, String description) {\n");
        content.append("        this.code = code;\n");
        content.append("        this.description = description;\n");
        content.append("    }\n");
        content.append("\n");
        content.append("    public String getCode() {\n");
        content.append("        return code;\n");
        content.append("    }\n");
        content.append("\n");
        content.append("    public String getDescription() {\n");
        content.append("        return description;\n");
        content.append("    }\n");
        content.append("}\n");

        return content.toString();
    }

    // 生成Builder模式
    private static void generateBuilderPattern(AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (!(psiFile instanceof PsiJavaFile)) {
            Messages.showWarningDialog("请在Java文件中使用此功能", "生成Builder模式");
            return;
        }

        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;

        PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        
        if (psiClass == null) {
            Messages.showWarningDialog("请将光标放在类中", "生成Builder模式");
            return;
        }

        String builderCode = generateBuilderCode(psiClass);
        insertTextAtCursor(e, builderCode);
        showNotification("Builder模式已生成");
    }

    // 辅助方法
    private static void insertTextAtCursor(AnActionEvent e, String text) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        Project project = e.getProject();
        if (editor == null || project == null) return;

        WriteCommandAction.runWriteCommandAction(project, () -> {
            Document document = editor.getDocument();
            int offset = editor.getCaretModel().getOffset();
            document.insertString(offset, text);
        });
    }

    private static void replaceSelectedText(AnActionEvent e, String text) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        Project project = e.getProject();
        if (editor == null || project == null) return;

        WriteCommandAction.runWriteCommandAction(project, () -> {
            editor.getDocument().replaceString(
                editor.getSelectionModel().getSelectionStart(),
                editor.getSelectionModel().getSelectionEnd(),
                text
            );
        });
    }

    private static String convertText(String text, String conversionType) {
        switch (conversionType) {
            case "驼峰转下划线":
                return text.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
            case "下划线转驼峰":
                StringBuilder result = new StringBuilder();
                boolean capitalizeNext = false;
                for (char c : text.toCharArray()) {
                    if (c == '_') {
                        capitalizeNext = true;
                    } else {
                        result.append(capitalizeNext ? Character.toUpperCase(c) : Character.toLowerCase(c));
                        capitalizeNext = false;
                    }
                }
                return result.toString();
            case "首字母大写":
                return text.substring(0, 1).toUpperCase() + text.substring(1);
            case "首字母小写":
                return text.substring(0, 1).toLowerCase() + text.substring(1);
            default:
                return text;
        }
    }

    private static String generateBuilderCode(PsiClass psiClass) {
        StringBuilder builder = new StringBuilder();
        String className = psiClass.getName();
        
        builder.append("\n    public static class Builder {\n");
        
        // 添加字段
        for (PsiField field : psiClass.getFields()) {
            if (!field.hasModifierProperty(PsiModifier.STATIC) && 
                !field.hasModifierProperty(PsiModifier.FINAL)) {
                builder.append("        private ").append(field.getType().getPresentableText())
                       .append(" ").append(field.getName()).append(";\n");
            }
        }
        
        builder.append("\n");
        
        // 添加setter方法
        for (PsiField field : psiClass.getFields()) {
            if (!field.hasModifierProperty(PsiModifier.STATIC) && 
                !field.hasModifierProperty(PsiModifier.FINAL)) {
                String fieldName = field.getName();
                String fieldType = field.getType().getPresentableText();
                builder.append("        public Builder ").append(fieldName)
                       .append("(").append(fieldType).append(" ").append(fieldName).append(") {\n")
                       .append("            this.").append(fieldName).append(" = ").append(fieldName).append(";\n")
                       .append("            return this;\n")
                       .append("        }\n\n");
            }
        }
        
        // 添加build方法
        builder.append("        public ").append(className).append(" build() {\n")
               .append("            return new ").append(className).append("(this);\n")
               .append("        }\n")
               .append("    }\n");
        
        return builder.toString();
    }

    private static void showNotification(String message) {
        // 这里可以使用IntelliJ的通知系统
        // 暂时使用简单的消息框
    }

    /**
     * 在新的编辑器标签页中打开内容
     */
    private static void openInNewEditor(AnActionEvent e, String content, String fileName) {
        Project project = e.getProject();
        if (project == null) return;

        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                // 创建一个轻量级虚拟文件
                LightVirtualFile virtualFile = new LightVirtualFile(fileName, content);

                // 在新的编辑器标签页中打开
                FileEditorManager.getInstance(project).openFile(virtualFile, true);
            } catch (Exception ex) {
                showNotification("打开编辑器失败: " + ex.getMessage());
            }
        });
    }

    // 内部类定义开发工具
    private static class DevTool {
        final String name;
        final String description;
        final DevToolAction action;

        DevTool(String name, String description, DevToolAction action) {
            this.name = name;
            this.description = description;
            this.action = action;
        }
    }

    @FunctionalInterface
    private interface DevToolAction {
        void execute(AnActionEvent e);
    }
}
