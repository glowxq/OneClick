package com.glowxq.plugs.actions;

import com.glowxq.plugs.utils.I18nUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * 代码重构助手Action
 * 提供常用的重构操作
 * 
 * @author glowxq
 */
public class RefactorAssistantAction extends AnAction {

    private static final List<String> REFACTOR_OPTIONS = Arrays.asList(
        "Extract Constants",
        "Convert to Stream API",
        "Add Null Checks",
        "Extract Method",
        "Inline Variables",
        "Convert to Lambda",
        "Add Logging",
        "Add Documentation",
        "Optimize Imports",
        "Format Code Style"
    );

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        if (project == null || editor == null || psiFile == null) {
            return;
        }

        showRefactorOptionsPopup(project, editor, psiFile);
    }

    private void showRefactorOptionsPopup(Project project, Editor editor, PsiFile psiFile) {
        BaseListPopupStep<String> step = new BaseListPopupStep<String>(
                I18nUtils.message("action.refactor.title"), REFACTOR_OPTIONS) {
            @Override
            public PopupStep onChosen(String selectedValue, boolean finalChoice) {
                if (finalChoice) {
                    performRefactor(project, editor, psiFile, selectedValue);
                }
                return FINAL_CHOICE;
            }
        };

        JBPopupFactory.getInstance()
                .createListPopup(step)
                .showInBestPositionFor(editor);
    }

    private void performRefactor(Project project, Editor editor, PsiFile psiFile, String refactorType) {
        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                switch (refactorType) {
                    case "Extract Constants":
                        extractConstants(editor, psiFile);
                        break;
                    case "Convert to Stream API":
                        convertToStreamAPI(editor, psiFile);
                        break;
                    case "Add Null Checks":
                        addNullChecks(editor, psiFile);
                        break;
                    case "Extract Method":
                        extractMethod(editor, psiFile);
                        break;
                    case "Inline Variables":
                        inlineVariables(editor, psiFile);
                        break;
                    case "Convert to Lambda":
                        convertToLambda(editor, psiFile);
                        break;
                    case "Add Logging":
                        addLogging(editor, psiFile);
                        break;
                    case "Add Documentation":
                        addDocumentation(editor, psiFile);
                        break;
                    case "Optimize Imports":
                        optimizeImports(psiFile);
                        break;
                    case "Format Code Style":
                        formatCodeStyle(psiFile);
                        break;
                }
                
                Messages.showInfoMessage(project,
                    I18nUtils.message("message.refactor.success", refactorType),
                    I18nUtils.message("action.refactor.title"));
                    
            } catch (Exception ex) {
                Messages.showErrorDialog(project,
                    I18nUtils.message("message.refactor.failed") + ": " + ex.getMessage(),
                    I18nUtils.message("action.refactor.title"));
            }
        });
    }

    private void extractConstants(Editor editor, PsiFile psiFile) {
        // 提取字符串常量
        String selectedText = editor.getSelectionModel().getSelectedText();
        if (selectedText != null && selectedText.startsWith("\"") && selectedText.endsWith("\"")) {
            String constantName = "CONSTANT_" + selectedText.replaceAll("[^A-Za-z0-9]", "_").toUpperCase();
            String constantDeclaration = String.format("    private static final String %s = %s;\n", 
                constantName, selectedText);
            
            // 在类的开始处插入常量声明
            PsiClass psiClass = PsiTreeUtil.getParentOfType(
                psiFile.findElementAt(editor.getCaretModel().getOffset()), PsiClass.class);
            if (psiClass != null) {
                insertAtClassBeginning(psiClass, constantDeclaration);
                // 替换选中的文本为常量名
                editor.getDocument().replaceString(
                    editor.getSelectionModel().getSelectionStart(),
                    editor.getSelectionModel().getSelectionEnd(),
                    constantName
                );
            }
        }
    }

    private void convertToStreamAPI(Editor editor, PsiFile psiFile) {
        // 简单的for循环转Stream API示例
        String suggestion = "    // Convert traditional for-each loop to Stream API:\n" +
                           "    // list.forEach(item -> System.out.println(item));\n" +
                           "    // list.stream().filter(item -> item != null).collect(Collectors.toList());\n" +
                           "    // list.stream().map(String::toUpperCase).collect(Collectors.toList());\n";
        
        insertAtCursor(editor, suggestion);
    }

    private void addNullChecks(Editor editor, PsiFile psiFile) {
        PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
        
        if (method != null) {
            StringBuilder nullChecks = new StringBuilder();
            nullChecks.append("        // Null checks\n");
            
            PsiParameter[] parameters = method.getParameterList().getParameters();
            for (PsiParameter param : parameters) {
                if (!param.getType().equals(PsiType.BOOLEAN) && 
                    !param.getType().equals(PsiType.INT) &&
                    !param.getType().equals(PsiType.LONG) &&
                    !param.getType().equals(PsiType.DOUBLE)) {
                    nullChecks.append(String.format(
                        "        if (%s == null) {\n" +
                        "            throw new IllegalArgumentException(\"%s cannot be null\");\n" +
                        "        }\n",
                        param.getName(), param.getName()
                    ));
                }
            }
            
            insertAtCursor(editor, nullChecks.toString());
        }
    }

    private void extractMethod(Editor editor, PsiFile psiFile) {
        String selectedText = editor.getSelectionModel().getSelectedText();
        if (selectedText != null && !selectedText.trim().isEmpty()) {
            String methodName = "extractedMethod";
            String extractedMethod = String.format(
                "\n    private void %s() {\n%s\n    }\n",
                methodName,
                selectedText.replaceAll("(?m)^", "        ") // 添加缩进
            );
            
            // 替换选中文本为方法调用
            editor.getDocument().replaceString(
                editor.getSelectionModel().getSelectionStart(),
                editor.getSelectionModel().getSelectionEnd(),
                "        " + methodName + "();"
            );
            
            // 在类的末尾添加提取的方法
            PsiClass psiClass = PsiTreeUtil.getParentOfType(
                psiFile.findElementAt(editor.getCaretModel().getOffset()), PsiClass.class);
            if (psiClass != null) {
                insertAtClassEnd(psiClass, extractedMethod);
            }
        }
    }

    private void inlineVariables(Editor editor, PsiFile psiFile) {
        String suggestion = "    // Inline variable suggestions:\n" +
                           "    // Instead of: String temp = getValue(); return temp;\n" +
                           "    // Use: return getValue();\n" +
                           "    // Instead of: boolean flag = condition; if (flag) {...}\n" +
                           "    // Use: if (condition) {...}\n";
        
        insertAtCursor(editor, suggestion);
    }

    private void convertToLambda(Editor editor, PsiFile psiFile) {
        String suggestion = "    // Lambda conversion suggestions:\n" +
                           "    // Anonymous class -> Lambda:\n" +
                           "    // new Runnable() { public void run() { ... } } -> () -> { ... }\n" +
                           "    // new Comparator<String>() { public int compare(String a, String b) { return a.compareTo(b); } }\n" +
                           "    // -> (a, b) -> a.compareTo(b)\n";
        
        insertAtCursor(editor, suggestion);
    }

    private void addLogging(Editor editor, PsiFile psiFile) {
        PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
        
        if (method != null) {
            String methodName = method.getName();
            String logging = String.format(
                "        LOGGER.debug(\"Entering method: %s\");\n" +
                "        try {\n" +
                "            // Method implementation\n" +
                "            LOGGER.debug(\"Method %s completed successfully\");\n" +
                "        } catch (Exception e) {\n" +
                "            LOGGER.error(\"Error in method %s: {}\", e.getMessage(), e);\n" +
                "            throw e;\n" +
                "        }\n",
                methodName, methodName, methodName
            );
            
            insertAtCursor(editor, logging);
        }
    }

    private void addDocumentation(Editor editor, PsiFile psiFile) {
        PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
        
        if (method != null) {
            StringBuilder javadoc = new StringBuilder();
            javadoc.append("    /**\n");
            javadoc.append("     * TODO: Add method description\n");
            javadoc.append("     *\n");
            
            PsiParameter[] parameters = method.getParameterList().getParameters();
            for (PsiParameter param : parameters) {
                javadoc.append(String.format("     * @param %s TODO: Add parameter description\n", 
                    param.getName()));
            }
            
            if (!method.getReturnType().equals(PsiType.VOID)) {
                javadoc.append("     * @return TODO: Add return description\n");
            }
            
            javadoc.append("     */\n");
            
            // 在方法前插入JavaDoc
            int methodOffset = method.getTextRange().getStartOffset();
            editor.getDocument().insertString(methodOffset, javadoc.toString());
        }
    }

    private void optimizeImports(PsiFile psiFile) {
        // 这里可以调用IntelliJ的优化导入功能
        // 或者提供建议
        // 实际实现需要使用IntelliJ的API
    }

    private void formatCodeStyle(PsiFile psiFile) {
        // 这里可以调用IntelliJ的代码格式化功能
        // 实际实现需要使用IntelliJ的API
    }

    private void insertAtCursor(Editor editor, String text) {
        int offset = editor.getCaretModel().getOffset();
        editor.getDocument().insertString(offset, text);
    }

    private void insertAtClassBeginning(PsiClass psiClass, String text) {
        PsiElement lBrace = psiClass.getLBrace();
        if (lBrace != null) {
            int offset = lBrace.getTextRange().getEndOffset();
            psiClass.getContainingFile().getViewProvider().getDocument()
                .insertString(offset, "\n" + text);
        }
    }

    private void insertAtClassEnd(PsiClass psiClass, String text) {
        PsiElement rBrace = psiClass.getRBrace();
        if (rBrace != null) {
            int offset = rBrace.getTextRange().getStartOffset();
            psiClass.getContainingFile().getViewProvider().getDocument()
                .insertString(offset, text + "\n");
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        boolean isJavaFile = psiFile instanceof PsiJavaFile;
        e.getPresentation().setEnabledAndVisible(isJavaFile);
    }
}
