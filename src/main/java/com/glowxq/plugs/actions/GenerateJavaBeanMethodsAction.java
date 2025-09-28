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
        JavaBeanUtils.removeSeparatorComment(psiClass);

        // 3. 添加分割注释
        String separatorComment = JavaBeanUtils.generateSeparatorComment();
        PsiComment comment = factory.createCommentFromText(separatorComment, psiClass);
        psiClass.add(comment);

        int newGetterCount = 0;
        int newSetterCount = 0;

        // 4. 按字段顺序重新生成所有getter和setter方法（放在类的最底部）
        for (PsiField field : fields) {
            String fieldName = field.getName();

            // 生成getter方法
            String getterCode = JavaBeanUtils.generateGetterCode(field);
            PsiMethod getterMethod = factory.createMethodFromText(getterCode, psiClass);
            psiClass.add(getterMethod);
            newGetterCount++;
            System.out.println("Generated getter for field: " + fieldName);

            // 生成setter方法
            String setterCode = JavaBeanUtils.generateSetterCode(field);
            PsiMethod setterMethod = factory.createMethodFromText(setterCode, psiClass);
            psiClass.add(setterMethod);
            newSetterCount++;
            System.out.println("Generated setter for field: " + fieldName);
        }

        // 5. 生成新的JSON格式toString方法（放在最后）
        int newToStringCount = 0;
        if (!fields.isEmpty()) {
            String toStringCode = JavaBeanUtils.generateToStringCode(psiClass);
            PsiMethod toStringMethod = factory.createMethodFromText(toStringCode, psiClass);
            psiClass.add(toStringMethod);
            newToStringCount = 1;
        }

        // 显示详细的生成结果
        String message = String.format(
            "JavaBean方法重新整理完成！\n" +
            "- 移除了 %d 个旧的getter方法，重新生成了 %d 个\n" +
            "- 移除了 %d 个旧的setter方法，重新生成了 %d 个\n" +
            "- %s toString方法\n" +
            "- 所有JavaBean方法已移动到类的底部\n" +
            "- 添加了分割注释便于区分业务逻辑",
            existingGetterCount, newGetterCount,
            existingSetterCount, newSetterCount,
            newToStringCount > 0 ? "重新生成了" : "未生成"
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
