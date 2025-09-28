package com.glowxq.plugs.actions;

import com.glowxq.plugs.utils.JavaBeanUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.FoldRegion;
import com.intellij.openapi.editor.FoldingModel;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.openapi.ui.Messages;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 折叠JavaBean方法的Action
 * @author glowxq
 */
public class FoldJavaBeanMethodsAction extends AnAction {

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

            // 获取当前光标位置的类
            PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
            PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);

            if (psiClass == null) {
                Messages.showErrorDialog(project, "请将光标放在类定义内", "错误");
                return;
            }

            // 执行折叠操作
            foldJavaBeanMethods(editor, psiClass);

            Messages.showInfoMessage(project, "JavaBean方法折叠完成！", "成功");
        } catch (Exception ex) {
            Messages.showErrorDialog("折叠JavaBean方法时发生错误: " + ex.getMessage(), "错误");
            ex.printStackTrace();
        }
    }

    /**
     * 折叠JavaBean方法
     */
    private void foldJavaBeanMethods(Editor editor, PsiClass psiClass) {
        FoldingModel foldingModel = editor.getFoldingModel();
        
        // 获取所有需要折叠的方法
        List<PsiMethod> methodsToFold = Arrays.stream(psiClass.getMethods())
                .filter(method -> JavaBeanUtils.isGetterMethod(method) || 
                                JavaBeanUtils.isSetterMethod(method) || 
                                JavaBeanUtils.isToStringMethod(method))
                .collect(Collectors.toList());

        foldingModel.runBatchFoldingOperation(() -> {
            // 先展开所有现有的折叠区域
            for (FoldRegion region : foldingModel.getAllFoldRegions()) {
                if (!region.isExpanded()) {
                    region.setExpanded(true);
                }
            }

            // 为每个方法创建折叠区域
            for (PsiMethod method : methodsToFold) {
                int startOffset = method.getTextRange().getStartOffset();
                int endOffset = method.getTextRange().getEndOffset();
                
                // 创建折叠区域
                FoldRegion foldRegion = foldingModel.addFoldRegion(startOffset, endOffset, 
                        getFoldPlaceholderText(method));
                
                if (foldRegion != null) {
                    foldRegion.setExpanded(false);
                }
            }
        });
    }

    /**
     * 获取折叠时显示的占位文本
     */
    private String getFoldPlaceholderText(PsiMethod method) {
        String methodName = method.getName();
        
        if (JavaBeanUtils.isGetterMethod(method)) {
            return "getter: " + methodName + "()";
        } else if (JavaBeanUtils.isSetterMethod(method)) {
            PsiParameter[] parameters = method.getParameterList().getParameters();
            String paramType = parameters.length > 0 ? 
                    parameters[0].getType().getPresentableText() : "";
            return "setter: " + methodName + "(" + paramType + ")";
        } else if (JavaBeanUtils.isToStringMethod(method)) {
            return "toString(): String";
        }
        
        return methodName + "()";
    }

    @Override
    public void update(AnActionEvent e) {
        // 简化逻辑：始终启用，让actionPerformed方法处理具体检查
        e.getPresentation().setEnabled(true);
        e.getPresentation().setVisible(true);
    }
}
