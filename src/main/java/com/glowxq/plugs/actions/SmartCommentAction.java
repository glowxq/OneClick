package com.glowxq.plugs.actions;

import com.glowxq.plugs.utils.I18nUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 智能注释生成器Action
 * 根据代码上下文自动生成合适的注释
 * 
 * @author glowxq
 */
public class SmartCommentAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        if (project == null || editor == null || psiFile == null) {
            return;
        }

        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                generateSmartComment(editor, psiFile);
                Messages.showInfoMessage(project,
                    I18nUtils.message("message.comment.generated"),
                    I18nUtils.message("action.comment.title"));
            } catch (Exception ex) {
                Messages.showErrorDialog(project,
                    I18nUtils.message("message.comment.failed") + ": " + ex.getMessage(),
                    I18nUtils.message("action.comment.title"));
            }
        });
    }

    private void generateSmartComment(Editor editor, PsiFile psiFile) {
        PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
        
        // 查找最近的方法、类或字段
        PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
        PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        PsiField field = PsiTreeUtil.getParentOfType(element, PsiField.class);

        String comment = null;
        int insertOffset = editor.getCaretModel().getOffset();

        if (method != null && isAtMethodStart(element, method)) {
            comment = generateMethodComment(method);
            insertOffset = method.getTextRange().getStartOffset();
        } else if (field != null && isAtFieldStart(element, field)) {
            comment = generateFieldComment(field);
            insertOffset = field.getTextRange().getStartOffset();
        } else if (psiClass != null && isAtClassStart(element, psiClass)) {
            comment = generateClassComment(psiClass);
            insertOffset = psiClass.getTextRange().getStartOffset();
        } else {
            // 生成行内注释
            comment = generateInlineComment(element);
        }

        if (comment != null) {
            editor.getDocument().insertString(insertOffset, comment);
        }
    }

    private String generateMethodComment(PsiMethod method) {
        StringBuilder comment = new StringBuilder();
        comment.append("    /**\n");
        
        // 方法描述
        String methodName = method.getName();
        String description = generateMethodDescription(methodName, method);
        comment.append("     * ").append(description).append("\n");
        
        // 参数注释
        PsiParameter[] parameters = method.getParameterList().getParameters();
        if (parameters.length > 0) {
            comment.append("     *\n");
            for (PsiParameter param : parameters) {
                String paramDesc = generateParameterDescription(param);
                comment.append("     * @param ").append(param.getName())
                       .append(" ").append(paramDesc).append("\n");
            }
        }
        
        // 返回值注释
        PsiType returnType = method.getReturnType();
        if (returnType != null && !returnType.equals(PsiType.VOID)) {
            String returnDesc = generateReturnDescription(returnType, methodName);
            comment.append("     * @return ").append(returnDesc).append("\n");
        }
        
        // 异常注释
        PsiReferenceList throwsList = method.getThrowsList();
        if (throwsList.getReferencedTypes().length > 0) {
            for (PsiClassType exceptionType : throwsList.getReferencedTypes()) {
                comment.append("     * @throws ").append(exceptionType.getClassName())
                       .append(" ").append(generateExceptionDescription(exceptionType)).append("\n");
            }
        }
        
        // 作者和时间
        comment.append("     * @author ").append(System.getProperty("user.name")).append("\n");
        comment.append("     * @since ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n");
        comment.append("     */\n");
        
        return comment.toString();
    }

    private String generateClassComment(PsiClass psiClass) {
        StringBuilder comment = new StringBuilder();
        comment.append("/**\n");
        
        String className = psiClass.getName();
        String description = generateClassDescription(className, psiClass);
        comment.append(" * ").append(description).append("\n");
        comment.append(" *\n");
        
        // 功能描述
        if (isController(psiClass)) {
            comment.append(" * 控制器类，处理HTTP请求和响应\n");
        } else if (isService(psiClass)) {
            comment.append(" * 服务类，包含业务逻辑处理\n");
        } else if (isRepository(psiClass)) {
            comment.append(" * 数据访问层，处理数据库操作\n");
        } else if (isEntity(psiClass)) {
            comment.append(" * 实体类，对应数据库表结构\n");
        } else if (isUtil(psiClass)) {
            comment.append(" * 工具类，提供通用的辅助方法\n");
        }
        
        comment.append(" *\n");
        comment.append(" * @author ").append(System.getProperty("user.name")).append("\n");
        comment.append(" * @version 1.0\n");
        comment.append(" * @since ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n");
        comment.append(" */\n");
        
        return comment.toString();
    }

    private String generateFieldComment(PsiField field) {
        String fieldName = field.getName();
        String fieldType = field.getType().getPresentableText();
        
        StringBuilder comment = new StringBuilder();
        comment.append("    /**\n");
        comment.append("     * ").append(generateFieldDescription(fieldName, fieldType)).append("\n");
        comment.append("     */\n");
        
        return comment.toString();
    }

    private String generateInlineComment(PsiElement element) {
        // 生成简单的行内注释
        return "        // TODO: Add comment\n";
    }

    private String generateMethodDescription(String methodName, PsiMethod method) {
        if (methodName.startsWith("get")) {
            return "获取" + extractPropertyName(methodName.substring(3));
        } else if (methodName.startsWith("set")) {
            return "设置" + extractPropertyName(methodName.substring(3));
        } else if (methodName.startsWith("is")) {
            return "判断是否" + extractPropertyName(methodName.substring(2));
        } else if (methodName.startsWith("create") || methodName.startsWith("add")) {
            return "创建或添加数据";
        } else if (methodName.startsWith("update") || methodName.startsWith("modify")) {
            return "更新或修改数据";
        } else if (methodName.startsWith("delete") || methodName.startsWith("remove")) {
            return "删除数据";
        } else if (methodName.startsWith("find") || methodName.startsWith("query") || methodName.startsWith("search")) {
            return "查询数据";
        } else if (methodName.startsWith("validate") || methodName.startsWith("check")) {
            return "验证数据";
        } else if (methodName.startsWith("calculate") || methodName.startsWith("compute")) {
            return "计算处理";
        } else if (methodName.startsWith("process") || methodName.startsWith("handle")) {
            return "处理业务逻辑";
        } else if (methodName.equals("toString")) {
            return "返回对象的字符串表示";
        } else if (methodName.equals("equals")) {
            return "比较对象是否相等";
        } else if (methodName.equals("hashCode")) {
            return "返回对象的哈希码";
        } else {
            return "执行" + methodName + "操作";
        }
    }

    private String generateParameterDescription(PsiParameter param) {
        String paramName = param.getName();
        String paramType = param.getType().getPresentableText();
        
        if (paramName.contains("id") || paramName.contains("Id")) {
            return "唯一标识符";
        } else if (paramName.contains("name") || paramName.contains("Name")) {
            return "名称";
        } else if (paramName.contains("email") || paramName.contains("Email")) {
            return "邮箱地址";
        } else if (paramName.contains("phone") || paramName.contains("Phone")) {
            return "电话号码";
        } else if (paramName.contains("password") || paramName.contains("Password")) {
            return "密码";
        } else if (paramName.contains("date") || paramName.contains("Date") || paramName.contains("time") || paramName.contains("Time")) {
            return "日期时间";
        } else if (paramName.contains("status") || paramName.contains("Status")) {
            return "状态";
        } else if (paramName.contains("type") || paramName.contains("Type")) {
            return "类型";
        } else if (paramName.contains("count") || paramName.contains("Count") || paramName.contains("size") || paramName.contains("Size")) {
            return "数量或大小";
        } else if (paramType.equals("boolean")) {
            return "布尔标志";
        } else if (paramType.contains("List") || paramType.contains("Set") || paramType.contains("Collection")) {
            return "集合数据";
        } else {
            return paramName + "参数";
        }
    }

    private String generateReturnDescription(PsiType returnType, String methodName) {
        String typeName = returnType.getPresentableText();
        
        if (methodName.startsWith("get")) {
            return "获取的" + extractPropertyName(methodName.substring(3)) + "值";
        } else if (methodName.startsWith("is")) {
            return "判断结果";
        } else if (methodName.startsWith("find") || methodName.startsWith("query")) {
            return "查询结果";
        } else if (methodName.startsWith("create") || methodName.startsWith("add")) {
            return "创建的对象";
        } else if (methodName.startsWith("update")) {
            return "更新后的对象";
        } else if (methodName.startsWith("calculate") || methodName.startsWith("compute")) {
            return "计算结果";
        } else if (typeName.equals("boolean")) {
            return "操作是否成功";
        } else if (typeName.contains("List") || typeName.contains("Set")) {
            return "结果集合";
        } else if (typeName.equals("String")) {
            return "字符串结果";
        } else if (typeName.equals("int") || typeName.equals("long") || typeName.equals("Integer") || typeName.equals("Long")) {
            return "数值结果";
        } else {
            return "操作结果";
        }
    }

    private String generateExceptionDescription(PsiClassType exceptionType) {
        String exceptionName = exceptionType.getClassName();
        
        if (exceptionName.contains("IllegalArgument")) {
            return "参数不合法时抛出";
        } else if (exceptionName.contains("NullPointer")) {
            return "空指针异常";
        } else if (exceptionName.contains("IO")) {
            return "IO操作异常";
        } else if (exceptionName.contains("SQL")) {
            return "数据库操作异常";
        } else if (exceptionName.contains("Runtime")) {
            return "运行时异常";
        } else {
            return "操作异常时抛出";
        }
    }

    private String generateClassDescription(String className, PsiClass psiClass) {
        if (className.endsWith("Controller")) {
            return className.replace("Controller", "") + "控制器";
        } else if (className.endsWith("Service")) {
            return className.replace("Service", "") + "服务类";
        } else if (className.endsWith("Repository") || className.endsWith("Dao")) {
            return className.replace("Repository", "").replace("Dao", "") + "数据访问对象";
        } else if (className.endsWith("Entity") || className.endsWith("Model")) {
            return className.replace("Entity", "").replace("Model", "") + "实体类";
        } else if (className.endsWith("Utils") || className.endsWith("Util")) {
            return className.replace("Utils", "").replace("Util", "") + "工具类";
        } else if (className.endsWith("Config") || className.endsWith("Configuration")) {
            return className.replace("Config", "").replace("Configuration", "") + "配置类";
        } else if (className.endsWith("Exception")) {
            return className.replace("Exception", "") + "异常类";
        } else if (className.endsWith("Test")) {
            return className.replace("Test", "") + "测试类";
        } else {
            return className + "类";
        }
    }

    private String generateFieldDescription(String fieldName, String fieldType) {
        if (fieldName.contains("id") || fieldName.contains("Id")) {
            return "唯一标识符";
        } else if (fieldName.contains("name") || fieldName.contains("Name")) {
            return "名称";
        } else if (fieldName.contains("email") || fieldName.contains("Email")) {
            return "邮箱地址";
        } else if (fieldName.contains("phone") || fieldName.contains("Phone")) {
            return "电话号码";
        } else if (fieldName.contains("password") || fieldName.contains("Password")) {
            return "密码";
        } else if (fieldName.contains("create") && (fieldName.contains("time") || fieldName.contains("Time") || fieldName.contains("date") || fieldName.contains("Date"))) {
            return "创建时间";
        } else if (fieldName.contains("update") && (fieldName.contains("time") || fieldName.contains("Time") || fieldName.contains("date") || fieldName.contains("Date"))) {
            return "更新时间";
        } else if (fieldName.contains("status") || fieldName.contains("Status")) {
            return "状态";
        } else if (fieldName.contains("type") || fieldName.contains("Type")) {
            return "类型";
        } else if (fieldName.contains("count") || fieldName.contains("Count")) {
            return "计数";
        } else if (fieldName.contains("size") || fieldName.contains("Size")) {
            return "大小";
        } else if (fieldName.contains("active") || fieldName.contains("Active")) {
            return "是否激活";
        } else if (fieldName.contains("enable") || fieldName.contains("Enable")) {
            return "是否启用";
        } else if (fieldType.equals("boolean")) {
            return "布尔标志";
        } else if (fieldType.contains("List") || fieldType.contains("Set")) {
            return "集合数据";
        } else {
            return fieldName + "字段";
        }
    }

    private String extractPropertyName(String name) {
        if (name.length() <= 1) return name.toLowerCase();
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    private boolean isAtMethodStart(PsiElement element, PsiMethod method) {
        return element.getTextOffset() <= method.getTextRange().getStartOffset() + 50;
    }

    private boolean isAtFieldStart(PsiElement element, PsiField field) {
        return element.getTextOffset() <= field.getTextRange().getStartOffset() + 20;
    }

    private boolean isAtClassStart(PsiElement element, PsiClass psiClass) {
        return element.getTextOffset() <= psiClass.getTextRange().getStartOffset() + 100;
    }

    private boolean isController(PsiClass psiClass) {
        return psiClass.getName() != null && psiClass.getName().endsWith("Controller");
    }

    private boolean isService(PsiClass psiClass) {
        return psiClass.getName() != null && psiClass.getName().endsWith("Service");
    }

    private boolean isRepository(PsiClass psiClass) {
        return psiClass.getName() != null && 
               (psiClass.getName().endsWith("Repository") || psiClass.getName().endsWith("Dao"));
    }

    private boolean isEntity(PsiClass psiClass) {
        return psiClass.getName() != null && 
               (psiClass.getName().endsWith("Entity") || psiClass.getName().endsWith("Model"));
    }

    private boolean isUtil(PsiClass psiClass) {
        return psiClass.getName() != null && 
               (psiClass.getName().endsWith("Utils") || psiClass.getName().endsWith("Util"));
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        boolean isJavaFile = psiFile instanceof PsiJavaFile;
        e.getPresentation().setEnabledAndVisible(isJavaFile);
    }
}
