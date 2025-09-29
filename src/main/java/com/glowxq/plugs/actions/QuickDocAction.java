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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * 快速文档生成器
 * 为类、方法、字段生成标准的JavaDoc文档
 * 
 * @author glowxq
 */
public class QuickDocAction extends AnAction {

    private static final List<DocType> DOC_TYPES = Arrays.asList(
        new DocType("类文档", "Class Documentation", QuickDocAction::generateClassDoc),
        new DocType("方法文档", "Method Documentation", QuickDocAction::generateMethodDoc),
        new DocType("字段文档", "Field Documentation", QuickDocAction::generateFieldDoc),
        new DocType("完整文档", "Complete Documentation", QuickDocAction::generateCompleteDoc)
    );

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        if (project == null || editor == null || psiFile == null) {
            return;
        }

        if (!(psiFile instanceof PsiJavaFile)) {
            Messages.showWarningDialog(project, "只支持Java文件", "快速文档生成");
            return;
        }

        // 显示文档类型选择弹窗
        showDocTypePopup(project, editor, (PsiJavaFile) psiFile, e);
    }

    private void showDocTypePopup(Project project, Editor editor, PsiJavaFile psiFile, AnActionEvent e) {
        BaseListPopupStep<DocType> step = new BaseListPopupStep<DocType>(
            "选择文档类型", DOC_TYPES) {
            
            @Override
            public @NotNull String getTextFor(DocType value) {
                return I18nUtils.isUseEnglish() ? value.getEnglishName() : value.getChineseName();
            }

            @Override
            public @Nullable PopupStep<?> onChosen(DocType selectedValue, boolean finalChoice) {
                if (finalChoice) {
                    generateDocumentation(project, editor, psiFile, selectedValue);
                }
                return FINAL_CHOICE;
            }
        };

        ListPopup popup = JBPopupFactory.getInstance().createListPopup(step);
        popup.showInBestPositionFor(editor);
    }

    private void generateDocumentation(Project project, Editor editor, PsiJavaFile psiFile, DocType docType) {
        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                String result = docType.getGenerator().apply(project, editor, psiFile);
                
                Messages.showInfoMessage(project, 
                    "文档生成完成: " + result,
                    "快速文档生成");
                    
            } catch (Exception ex) {
                Messages.showErrorDialog(project,
                    "文档生成失败: " + ex.getMessage(),
                    "快速文档生成");
            }
        });
    }

    private static String generateClassDoc(Project project, Editor editor, PsiJavaFile psiFile) {
        PsiClass[] classes = psiFile.getClasses();
        int docCount = 0;

        for (PsiClass psiClass : classes) {
            if (psiClass.getDocComment() == null) {
                String classDoc = createClassDocumentation(psiClass);
                insertDocumentation(project, psiClass, classDoc);
                docCount++;
            }
        }

        return "为 " + docCount + " 个类生成了文档";
    }

    private static String generateMethodDoc(Project project, Editor editor, PsiJavaFile psiFile) {
        PsiMethod[] methods = PsiTreeUtil.getChildrenOfType(psiFile, PsiMethod.class);
        if (methods == null) {
            return "没有找到方法";
        }

        int docCount = 0;
        for (PsiMethod method : methods) {
            if (method.getDocComment() == null) {
                String methodDoc = createMethodDocumentation(method);
                insertDocumentation(project, method, methodDoc);
                docCount++;
            }
        }

        return "为 " + docCount + " 个方法生成了文档";
    }

    private static String generateFieldDoc(Project project, Editor editor, PsiJavaFile psiFile) {
        PsiField[] fields = PsiTreeUtil.getChildrenOfType(psiFile, PsiField.class);
        if (fields == null) {
            return "没有找到字段";
        }

        int docCount = 0;
        for (PsiField field : fields) {
            if (field.getDocComment() == null) {
                String fieldDoc = createFieldDocumentation(field);
                insertDocumentation(project, field, fieldDoc);
                docCount++;
            }
        }

        return "为 " + docCount + " 个字段生成了文档";
    }

    private static String generateCompleteDoc(Project project, Editor editor, PsiJavaFile psiFile) {
        String classResult = generateClassDoc(project, editor, psiFile);
        String methodResult = generateMethodDoc(project, editor, psiFile);
        String fieldResult = generateFieldDoc(project, editor, psiFile);

        return "完整文档生成完成:\n" + classResult + "\n" + methodResult + "\n" + fieldResult;
    }

    private static String createClassDocumentation(PsiClass psiClass) {
        StringBuilder doc = new StringBuilder();
        doc.append("/**\n");
        doc.append(" * ").append(generateClassDescription(psiClass)).append("\n");
        doc.append(" * \n");
        
        // 添加作者和日期
        doc.append(" * @author ").append(System.getProperty("user.name")).append("\n");
        doc.append(" * @since ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n");
        
        // 如果是泛型类，添加类型参数文档
        PsiTypeParameter[] typeParameters = psiClass.getTypeParameters();
        for (PsiTypeParameter typeParam : typeParameters) {
            doc.append(" * @param <").append(typeParam.getName()).append("> 类型参数\n");
        }
        
        doc.append(" */");
        return doc.toString();
    }

    private static String createMethodDocumentation(PsiMethod method) {
        StringBuilder doc = new StringBuilder();
        doc.append("/**\n");
        doc.append(" * ").append(generateMethodDescription(method)).append("\n");
        doc.append(" * \n");
        
        // 添加参数文档
        PsiParameter[] parameters = method.getParameterList().getParameters();
        for (PsiParameter param : parameters) {
            doc.append(" * @param ").append(param.getName()).append(" ").append(generateParameterDescription(param)).append("\n");
        }
        
        // 添加返回值文档
        PsiType returnType = method.getReturnType();
        if (returnType != null && !PsiType.VOID.equals(returnType)) {
            doc.append(" * @return ").append(generateReturnDescription(returnType)).append("\n");
        }
        
        // 添加异常文档
        PsiReferenceList throwsList = method.getThrowsList();
        PsiJavaCodeReferenceElement[] exceptions = throwsList.getReferenceElements();
        for (PsiJavaCodeReferenceElement exception : exceptions) {
            doc.append(" * @throws ").append(exception.getReferenceName()).append(" 当发生错误时\n");
        }
        
        doc.append(" */");
        return doc.toString();
    }

    private static String createFieldDocumentation(PsiField field) {
        StringBuilder doc = new StringBuilder();
        doc.append("/**\n");
        doc.append(" * ").append(generateFieldDescription(field)).append("\n");
        doc.append(" */");
        return doc.toString();
    }

    private static String generateClassDescription(PsiClass psiClass) {
        String className = psiClass.getName();
        if (className == null) {
            return "类描述";
        }
        
        // 根据类名生成描述
        if (className.endsWith("Service")) {
            return className.replace("Service", "") + "服务类";
        } else if (className.endsWith("Controller")) {
            return className.replace("Controller", "") + "控制器";
        } else if (className.endsWith("Repository") || className.endsWith("Dao")) {
            return className.replace("Repository", "").replace("Dao", "") + "数据访问对象";
        } else if (className.endsWith("Entity") || className.endsWith("Model")) {
            return className.replace("Entity", "").replace("Model", "") + "实体类";
        } else if (className.endsWith("DTO") || className.endsWith("Dto")) {
            return className.replace("DTO", "").replace("Dto", "") + "数据传输对象";
        } else if (className.endsWith("VO") || className.endsWith("Vo")) {
            return className.replace("VO", "").replace("Vo", "") + "视图对象";
        } else if (className.endsWith("Utils") || className.endsWith("Util")) {
            return className.replace("Utils", "").replace("Util", "") + "工具类";
        } else if (className.endsWith("Config") || className.endsWith("Configuration")) {
            return className.replace("Config", "").replace("Configuration", "") + "配置类";
        } else {
            return className + "类";
        }
    }

    private static String generateMethodDescription(PsiMethod method) {
        String methodName = method.getName();
        
        // 根据方法名生成描述
        if (methodName.startsWith("get")) {
            return "获取" + methodName.substring(3).toLowerCase() + "属性";
        } else if (methodName.startsWith("set")) {
            return "设置" + methodName.substring(3).toLowerCase() + "属性";
        } else if (methodName.startsWith("is")) {
            return "判断是否" + methodName.substring(2).toLowerCase();
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
        } else if (methodName.equals("toString")) {
            return "返回对象的字符串表示";
        } else if (methodName.equals("equals")) {
            return "比较对象是否相等";
        } else if (methodName.equals("hashCode")) {
            return "返回对象的哈希码";
        } else {
            return methodName + "方法";
        }
    }

    private static String generateFieldDescription(PsiField field) {
        String fieldName = field.getName();
        if (fieldName == null) {
            return "字段描述";
        }
        
        // 根据字段名生成描述
        if (fieldName.toLowerCase().contains("id")) {
            return "唯一标识符";
        } else if (fieldName.toLowerCase().contains("name")) {
            return "名称";
        } else if (fieldName.toLowerCase().contains("code")) {
            return "编码";
        } else if (fieldName.toLowerCase().contains("time") || fieldName.toLowerCase().contains("date")) {
            return "时间";
        } else if (fieldName.toLowerCase().contains("status")) {
            return "状态";
        } else if (fieldName.toLowerCase().contains("type")) {
            return "类型";
        } else if (fieldName.toLowerCase().contains("count") || fieldName.toLowerCase().contains("num")) {
            return "数量";
        } else if (fieldName.toLowerCase().contains("flag")) {
            return "标志";
        } else {
            return fieldName + "属性";
        }
    }

    private static String generateParameterDescription(PsiParameter param) {
        String paramName = param.getName();
        PsiType paramType = param.getType();
        
        if (paramName.toLowerCase().contains("id")) {
            return "唯一标识符";
        } else if (paramType.getCanonicalText().contains("String")) {
            return "字符串参数";
        } else if (paramType.getCanonicalText().contains("int") || paramType.getCanonicalText().contains("Integer")) {
            return "整数参数";
        } else if (paramType.getCanonicalText().contains("boolean") || paramType.getCanonicalText().contains("Boolean")) {
            return "布尔参数";
        } else if (paramType.getCanonicalText().contains("List")) {
            return "列表参数";
        } else if (paramType.getCanonicalText().contains("Map")) {
            return "映射参数";
        } else {
            return paramName + "参数";
        }
    }

    private static String generateReturnDescription(PsiType returnType) {
        String typeName = returnType.getCanonicalText();
        
        if (typeName.contains("String")) {
            return "字符串结果";
        } else if (typeName.contains("int") || typeName.contains("Integer")) {
            return "整数结果";
        } else if (typeName.contains("boolean") || typeName.contains("Boolean")) {
            return "布尔结果";
        } else if (typeName.contains("List")) {
            return "列表结果";
        } else if (typeName.contains("Map")) {
            return "映射结果";
        } else if (typeName.contains("void")) {
            return "无返回值";
        } else {
            return "返回结果";
        }
    }

    private static void insertDocumentation(Project project, PsiElement element, String documentation) {
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);

        try {
            PsiComment docComment = factory.createCommentFromText(documentation, element);

            element.getParent().addBefore(docComment, element);
            // 简单添加换行，不使用复杂的API
        } catch (Exception e) {
            // 如果创建失败，使用简单的注释
            PsiComment simpleComment = factory.createCommentFromText("/** " + documentation.replaceAll("/\\*\\*|\\*/", "").trim() + " */", element);
            element.getParent().addBefore(simpleComment, element);
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        
        boolean enabled = project != null && editor != null && psiFile instanceof PsiJavaFile;
        e.getPresentation().setEnabledAndVisible(enabled);
    }

    // 文档类型类
    private static class DocType {
        private final String chineseName;
        private final String englishName;
        private final DocGenerator generator;

        public DocType(String chineseName, String englishName, DocGenerator generator) {
            this.chineseName = chineseName;
            this.englishName = englishName;
            this.generator = generator;
        }

        public String getChineseName() { return chineseName; }
        public String getEnglishName() { return englishName; }
        public DocGenerator getGenerator() { return generator; }
    }

    @FunctionalInterface
    private interface DocGenerator {
        String apply(Project project, Editor editor, PsiJavaFile psiFile) throws Exception;
    }
}
