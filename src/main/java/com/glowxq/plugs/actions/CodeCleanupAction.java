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
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 代码清理助手
 * 提供各种代码清理和优化功能
 * 
 * @author glowxq
 */
public class CodeCleanupAction extends AnAction {

    private static final List<CleanupOperation> CLEANUP_OPERATIONS = Arrays.asList(
        new CleanupOperation("移除未使用的导入", "Remove Unused Imports", CodeCleanupAction::removeUnusedImports),
        new CleanupOperation("移除空行", "Remove Empty Lines", CodeCleanupAction::removeEmptyLines),
        new CleanupOperation("移除未使用的变量", "Remove Unused Variables", CodeCleanupAction::removeUnusedVariables),
        new CleanupOperation("移除TODO注释", "Remove TODO Comments", CodeCleanupAction::removeTodoComments),
        new CleanupOperation("格式化代码", "Format Code", CodeCleanupAction::formatCode),
        new CleanupOperation("优化导入顺序", "Optimize Imports", CodeCleanupAction::optimizeImports),
        new CleanupOperation("移除多余的空格", "Remove Extra Spaces", CodeCleanupAction::removeExtraSpaces),
        new CleanupOperation("统一换行符", "Normalize Line Endings", CodeCleanupAction::normalizeLineEndings),
        new CleanupOperation("移除调试代码", "Remove Debug Code", CodeCleanupAction::removeDebugCode),
        new CleanupOperation("全部清理", "Clean All", CodeCleanupAction::cleanAll)
    );

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        if (project == null || editor == null || psiFile == null) {
            return;
        }

        // 显示清理选项弹窗
        showCleanupPopup(project, editor, psiFile, e);
    }

    private void showCleanupPopup(Project project, Editor editor, PsiFile psiFile, AnActionEvent e) {
        BaseListPopupStep<CleanupOperation> step = new BaseListPopupStep<CleanupOperation>(
            I18nUtils.message("action.cleanup.title"), CLEANUP_OPERATIONS) {
            
            @Override
            public @NotNull String getTextFor(CleanupOperation value) {
                return I18nUtils.isUseEnglish() ? value.getEnglishName() : value.getChineseName();
            }

            @Override
            public @Nullable PopupStep<?> onChosen(CleanupOperation selectedValue, boolean finalChoice) {
                if (finalChoice) {
                    performCleanup(project, editor, psiFile, selectedValue);
                }
                return FINAL_CHOICE;
            }
        };

        ListPopup popup = JBPopupFactory.getInstance().createListPopup(step);
        popup.showInBestPositionFor(editor);
    }

    private void performCleanup(Project project, Editor editor, PsiFile psiFile, CleanupOperation operation) {
        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                String result = operation.getAction().apply(project, editor, psiFile);
                
                Messages.showInfoMessage(project, 
                    I18nUtils.message("message.cleanup.success", result),
                    I18nUtils.message("action.cleanup.title"));
                    
            } catch (Exception ex) {
                Messages.showErrorDialog(project,
                    I18nUtils.message("message.cleanup.failed", ex.getMessage()),
                    I18nUtils.message("action.cleanup.title"));
            }
        });
    }

    // 清理操作实现
    private static String removeUnusedImports(Project project, Editor editor, PsiFile psiFile) {
        if (!(psiFile instanceof PsiJavaFile)) {
            return "只支持Java文件";
        }

        PsiJavaFile javaFile = (PsiJavaFile) psiFile;
        PsiImportList importList = javaFile.getImportList();
        if (importList == null) {
            return "没有找到导入语句";
        }

        PsiImportStatement[] imports = importList.getImportStatements();
        int removedCount = 0;

        for (PsiImportStatement importStatement : imports) {
            if (isUnusedImport(importStatement, javaFile)) {
                importStatement.delete();
                removedCount++;
            }
        }

        return "移除了 " + removedCount + " 个未使用的导入";
    }

    private static String removeEmptyLines(Project project, Editor editor, PsiFile psiFile) {
        String text = psiFile.getText();
        String cleanedText = text.replaceAll("\\n\\s*\\n\\s*\\n", "\n\n");
        
        if (!text.equals(cleanedText)) {
            psiFile.getViewProvider().getDocument().setText(cleanedText);
            return "移除了多余的空行";
        }
        
        return "没有找到多余的空行";
    }

    private static String removeUnusedVariables(Project project, Editor editor, PsiFile psiFile) {
        if (!(psiFile instanceof PsiJavaFile)) {
            return "只支持Java文件";
        }

        Collection<PsiLocalVariable> variables = PsiTreeUtil.findChildrenOfType(psiFile, PsiLocalVariable.class);
        int removedCount = 0;

        for (PsiLocalVariable variable : variables) {
            if (isUnusedVariable(variable)) {
                PsiStatement statement = PsiTreeUtil.getParentOfType(variable, PsiStatement.class);
                if (statement != null) {
                    statement.delete();
                    removedCount++;
                }
            }
        }

        return "移除了 " + removedCount + " 个未使用的变量";
    }

    private static String removeTodoComments(Project project, Editor editor, PsiFile psiFile) {
        Collection<PsiComment> comments = PsiTreeUtil.findChildrenOfType(psiFile, PsiComment.class);
        int removedCount = 0;

        for (PsiComment comment : comments) {
            String text = comment.getText().toLowerCase();
            if (text.contains("todo") || text.contains("fixme") || text.contains("hack")) {
                comment.delete();
                removedCount++;
            }
        }

        return "移除了 " + removedCount + " 个TODO注释";
    }

    private static String formatCode(Project project, Editor editor, PsiFile psiFile) {
        // 使用IntelliJ的代码格式化功能
        com.intellij.psi.codeStyle.CodeStyleManager.getInstance(project).reformat(psiFile);
        return "代码格式化完成";
    }

    private static String optimizeImports(Project project, Editor editor, PsiFile psiFile) {
        if (!(psiFile instanceof PsiJavaFile)) {
            return "只支持Java文件";
        }

        // 使用IntelliJ的导入优化功能
        com.intellij.codeInsight.actions.OptimizeImportsProcessor processor = 
            new com.intellij.codeInsight.actions.OptimizeImportsProcessor(project, psiFile);
        processor.run();
        
        return "导入优化完成";
    }

    private static String removeExtraSpaces(Project project, Editor editor, PsiFile psiFile) {
        String text = psiFile.getText();
        String cleanedText = text.replaceAll("[ \\t]+", " ")
                                .replaceAll("[ \\t]+\\n", "\n")
                                .replaceAll("\\n[ \\t]+", "\n");
        
        if (!text.equals(cleanedText)) {
            psiFile.getViewProvider().getDocument().setText(cleanedText);
            return "移除了多余的空格";
        }
        
        return "没有找到多余的空格";
    }

    private static String normalizeLineEndings(Project project, Editor editor, PsiFile psiFile) {
        String text = psiFile.getText();
        String normalizedText = text.replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
        
        if (!text.equals(normalizedText)) {
            psiFile.getViewProvider().getDocument().setText(normalizedText);
            return "统一了换行符格式";
        }
        
        return "换行符格式已经统一";
    }

    private static String removeDebugCode(Project project, Editor editor, PsiFile psiFile) {
        if (!(psiFile instanceof PsiJavaFile)) {
            return "只支持Java文件";
        }

        Collection<PsiMethodCallExpression> methodCalls = PsiTreeUtil.findChildrenOfType(psiFile, PsiMethodCallExpression.class);
        int removedCount = 0;

        for (PsiMethodCallExpression call : methodCalls) {
            String methodName = call.getMethodExpression().getReferenceName();
            if ("println".equals(methodName) || "print".equals(methodName) || "printf".equals(methodName)) {
                PsiStatement statement = PsiTreeUtil.getParentOfType(call, PsiStatement.class);
                if (statement != null) {
                    statement.delete();
                    removedCount++;
                }
            }
        }

        return "移除了 " + removedCount + " 行调试代码";
    }

    private static String cleanAll(Project project, Editor editor, PsiFile psiFile) {
        List<String> results = new ArrayList<>();
        
        for (CleanupOperation operation : CLEANUP_OPERATIONS) {
            if (!"全部清理".equals(operation.getChineseName())) {
                try {
                    String result = operation.getAction().apply(project, editor, psiFile);
                    results.add(result);
                } catch (Exception e) {
                    results.add("操作失败: " + e.getMessage());
                }
            }
        }
        
        return "全部清理完成:\n" + String.join("\n", results);
    }

    // 辅助方法
    private static boolean isUnusedImport(PsiImportStatement importStatement, PsiJavaFile javaFile) {
        if (importStatement.isOnDemand()) {
            return false; // 通配符导入暂时不处理
        }
        
        PsiJavaCodeReferenceElement importReference = importStatement.getImportReference();
        if (importReference == null) {
            return true; // 无法解析的导入
        }

        PsiElement resolved = importReference.resolve();
        if (!(resolved instanceof PsiClass)) {
            return true; // 不是类的导入
        }

        PsiClass importedClass = (PsiClass) resolved;
        
        String className = importedClass.getName();
        if (className == null) {
            return true;
        }
        
        // 简单检查：在文件中搜索类名
        String fileText = javaFile.getText();
        return !fileText.contains(className);
    }

    private static boolean isUnusedVariable(PsiLocalVariable variable) {
        // 简单的未使用变量检测
        PsiCodeBlock codeBlock = PsiTreeUtil.getParentOfType(variable, PsiCodeBlock.class);
        if (codeBlock == null) {
            return false;
        }
        
        String variableName = variable.getName();
        if (variableName == null) {
            return false;
        }
        
        // 检查变量是否在代码块中被使用
        Collection<PsiReferenceExpression> references = PsiTreeUtil.findChildrenOfType(codeBlock, PsiReferenceExpression.class);
        for (PsiReferenceExpression ref : references) {
            if (variableName.equals(ref.getReferenceName()) && !ref.equals(variable.getNameIdentifier())) {
                return false; // 找到使用
            }
        }
        
        return true; // 未找到使用
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        
        boolean enabled = project != null && editor != null && psiFile != null;
        e.getPresentation().setEnabledAndVisible(enabled);
    }

    // 清理操作类
    private static class CleanupOperation {
        private final String chineseName;
        private final String englishName;
        private final CleanupAction action;

        public CleanupOperation(String chineseName, String englishName, CleanupAction action) {
            this.chineseName = chineseName;
            this.englishName = englishName;
            this.action = action;
        }

        public String getChineseName() { return chineseName; }
        public String getEnglishName() { return englishName; }
        public CleanupAction getAction() { return action; }
    }

    @FunctionalInterface
    private interface CleanupAction {
        String apply(Project project, Editor editor, PsiFile psiFile) throws Exception;
    }
}
