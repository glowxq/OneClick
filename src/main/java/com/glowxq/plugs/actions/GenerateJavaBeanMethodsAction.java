package com.glowxq.plugs.actions;

import com.glowxq.plugs.utils.JavaBeanUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
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
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        if (project == null || editor == null || psiFile == null) {
            return;
        }

        // 检查是否为Java文件
        if (!(psiFile instanceof PsiJavaFile)) {
            Messages.showErrorDialog(project, "请在Java文件中使用此功能", "错误");
            return;
        }

        // 获取当前光标位置的类
        PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);

        if (psiClass == null) {
            Messages.showErrorDialog(project, "请将光标放在类定义内", "错误");
            return;
        }

        // 执行生成操作
        WriteCommandAction.runWriteCommandAction(project, () -> {
            generateJavaBeanMethods(project, psiClass);
        });

        Messages.showInfoMessage(project, "JavaBean方法生成完成！", "成功");
    }

    /**
     * 生成JavaBean方法
     */
    private void generateJavaBeanMethods(Project project, PsiClass psiClass) {
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
        List<PsiField> fields = JavaBeanUtils.getInstanceFields(psiClass);

        // 1. 生成getter和setter方法
        for (PsiField field : fields) {
            // 生成getter方法（如果不存在）
            if (!JavaBeanUtils.hasGetter(psiClass, field)) {
                String getterCode = JavaBeanUtils.generateGetterCode(field);
                PsiMethod getterMethod = factory.createMethodFromText(getterCode, psiClass);
                psiClass.add(getterMethod);
            }

            // 生成setter方法（如果不存在）
            if (!JavaBeanUtils.hasSetter(psiClass, field)) {
                String setterCode = JavaBeanUtils.generateSetterCode(field);
                PsiMethod setterMethod = factory.createMethodFromText(setterCode, psiClass);
                psiClass.add(setterMethod);
            }
        }

        // 2. 删除已存在的toString方法
        List<PsiMethod> existingToStringMethods = JavaBeanUtils.getToStringMethods(psiClass);
        for (PsiMethod method : existingToStringMethods) {
            method.delete();
        }

        // 3. 生成新的JSON格式toString方法
        if (!fields.isEmpty()) {
            String toStringCode = JavaBeanUtils.generateToStringCode(psiClass);
            PsiMethod toStringMethod = factory.createMethodFromText(toStringCode, psiClass);
            psiClass.add(toStringMethod);
        }
    }

    @Override
    public void update(AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        // 只在Java文件中启用此Action，但始终显示菜单项
        boolean enabled = project != null && editor != null &&
                         psiFile instanceof PsiJavaFile;

        e.getPresentation().setEnabled(enabled);
        // 移除setVisible调用，让菜单项始终可见
    }
}
