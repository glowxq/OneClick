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

            // 执行生成操作
            String[] result = new String[1]; // 用于存储结果消息
            WriteCommandAction.runWriteCommandAction(project, () -> {
                result[0] = generateJavaBeanMethods(project, psiClass);
            });

            Messages.showInfoMessage(project, result[0], "成功");
        } catch (Exception ex) {
            Messages.showErrorDialog("生成JavaBean方法时发生错误: " + ex.getMessage(), "错误");
            ex.printStackTrace();
        }
    }

    /**
     * 生成JavaBean方法
     * @return 生成结果消息
     */
    private String generateJavaBeanMethods(Project project, PsiClass psiClass) {
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
        List<PsiField> fields = JavaBeanUtils.getInstanceFields(psiClass);

        int getterCount = 0;
        int setterCount = 0;

        // 1. 生成getter和setter方法
        for (PsiField field : fields) {
            String fieldName = field.getName();

            // 生成getter方法（如果不存在）
            if (!JavaBeanUtils.hasGetter(psiClass, field)) {
                String getterCode = JavaBeanUtils.generateGetterCode(field);
                PsiMethod getterMethod = factory.createMethodFromText(getterCode, psiClass);
                psiClass.add(getterMethod);
                getterCount++;
                System.out.println("Generated getter for field: " + fieldName);
            } else {
                System.out.println("Getter already exists for field: " + fieldName);
            }

            // 生成setter方法（如果不存在）
            if (!JavaBeanUtils.hasSetter(psiClass, field)) {
                String setterCode = JavaBeanUtils.generateSetterCode(field);
                PsiMethod setterMethod = factory.createMethodFromText(setterCode, psiClass);
                psiClass.add(setterMethod);
                setterCount++;
                System.out.println("Generated setter for field: " + fieldName);
            } else {
                System.out.println("Setter already exists for field: " + fieldName);
            }
        }

        // 2. 删除已存在的toString方法
        List<PsiMethod> existingToStringMethods = JavaBeanUtils.getToStringMethods(psiClass);
        int toStringCount = existingToStringMethods.size();
        for (PsiMethod method : existingToStringMethods) {
            method.delete();
        }

        // 3. 生成新的JSON格式toString方法
        if (!fields.isEmpty()) {
            String toStringCode = JavaBeanUtils.generateToStringCode(psiClass);
            PsiMethod toStringMethod = factory.createMethodFromText(toStringCode, psiClass);
            psiClass.add(toStringMethod);
            toStringCount = 1; // 重新生成了一个toString方法
        }

        // 显示详细的生成结果
        String message = String.format(
            "JavaBean方法生成完成！\n" +
            "- 生成了 %d 个getter方法\n" +
            "- 生成了 %d 个setter方法\n" +
            "- %s toString方法",
            getterCount, setterCount,
            toStringCount > 0 ? "重新生成了" : "未生成"
        );
        System.out.println(message);
        return message;
    }

    @Override
    public void update(AnActionEvent e) {
        // 简化逻辑：始终启用，让actionPerformed方法处理具体检查
        e.getPresentation().setEnabled(true);
        e.getPresentation().setVisible(true);
    }
}
