package com.glowxq.plugs.utils;

import com.glowxq.plugs.settings.OneClickSettings;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiUtil;
import com.intellij.openapi.project.Project;

import java.util.*;
import java.util.stream.Collectors;

/**
 * JavaBean工具类，用于生成getter/setter/toString方法
 * @author glowxq
 */
public class JavaBeanUtils {

    /**
     * 获取类中的所有字段（排除静态和final字段）
     */
    public static List<PsiField> getInstanceFields(PsiClass psiClass) {
        List<PsiField> fields = Arrays.stream(psiClass.getFields())
                .filter(field -> !field.hasModifierProperty(PsiModifier.STATIC))
                .filter(field -> !field.hasModifierProperty(PsiModifier.FINAL))
                .collect(Collectors.toList());

        // 应用字段排序（仅对业务类生效）
        return sortFields(fields, psiClass);
    }

    /**
     * 根据设置对字段进行排序（仅对业务类生效）
     */
    public static List<PsiField> sortFields(List<PsiField> fields, PsiClass psiClass) {
        OneClickSettings settings = OneClickSettings.getInstance();

        // 只有业务类才启用字段排序
        if (!settings.isEnableFieldSorting() || ClassTypeDetector.detectClassType(psiClass) != ClassTypeDetector.ClassType.BUSINESS_CLASS) {
            return fields;
        }

        String sortType = settings.getFieldSortType();
        boolean ascending = settings.isSortAscending();
        boolean enableModifierSorting = settings.isEnableModifierSorting();

        // 如果启用权限修饰符排序，先按修饰符分组，然后在每个组内排序
        if (enableModifierSorting) {
            return sortFieldsByModifierGroups(fields, sortType, ascending, settings.getModifierSortOrder());
        }

        // 不启用修饰符排序时，按原有逻辑
        Comparator<PsiField> comparator;
        switch (sortType) {
            case "LENGTH":
                comparator = Comparator.comparing(field -> field.getName().length());
                break;
            case "TYPE":
                comparator = Comparator.comparing(field -> {
                    PsiType type = field.getType();
                    return type.getPresentableText();
                });
                break;
            case "MODIFIER":
                comparator = createModifierComparator(settings.getModifierSortOrder());
                break;
            case "NAME":
            default:
                comparator = Comparator.comparing(PsiField::getName);
                break;
        }

        if (!ascending) {
            comparator = comparator.reversed();
        }

        return fields.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    /**
     * 按权限修饰符分组排序字段
     */
    private static List<PsiField> sortFieldsByModifierGroups(List<PsiField> fields, String sortType, boolean ascending, String modifierOrder) {
        // 按修饰符分组
        String[] modifiers = modifierOrder.split(",");
        Map<String, List<PsiField>> groupedFields = new LinkedHashMap<>();

        // 初始化分组
        for (String modifier : modifiers) {
            groupedFields.put(modifier.trim(), new ArrayList<>());
        }

        // 将字段分配到对应的组
        for (PsiField field : fields) {
            String modifier = getFieldModifier(field);
            List<PsiField> group = groupedFields.get(modifier);
            if (group != null) {
                group.add(field);
            }
        }

        // 在每个组内排序
        Comparator<PsiField> innerComparator = createInnerComparator(sortType);
        if (!ascending) {
            innerComparator = innerComparator.reversed();
        }

        for (List<PsiField> group : groupedFields.values()) {
            if (!group.isEmpty()) {
                group.sort(innerComparator);
            }
        }

        // 合并所有组
        List<PsiField> result = new ArrayList<>();
        for (List<PsiField> group : groupedFields.values()) {
            result.addAll(group);
        }

        return result;
    }

    /**
     * 创建组内排序比较器
     */
    private static Comparator<PsiField> createInnerComparator(String sortType) {
        switch (sortType) {
            case "LENGTH":
                return Comparator.comparing(field -> field.getName().length());
            case "TYPE":
                return Comparator.comparing(field -> field.getType().getPresentableText());
            case "MODIFIER":
                // 修饰符排序时，组内不需要额外排序
                return (f1, f2) -> 0;
            case "NAME":
            default:
                return Comparator.comparing(PsiField::getName);
        }
    }

    /**
     * 创建权限修饰符比较器
     */
    private static Comparator<PsiField> createModifierComparator(String modifierOrder) {
        String[] modifiers = modifierOrder.split(",");
        Map<String, Integer> modifierPriority = new HashMap<>();

        for (int i = 0; i < modifiers.length; i++) {
            modifierPriority.put(modifiers[i].trim(), i);
        }

        return Comparator.comparing(field -> {
            String modifier = getFieldModifier(field);
            return modifierPriority.getOrDefault(modifier, 999); // 未知修饰符排在最后
        });
    }

    /**
     * 获取字段的权限修饰符
     */
    private static String getFieldModifier(PsiField field) {
        if (field.hasModifierProperty(PsiModifier.PUBLIC)) {
            return "public";
        } else if (field.hasModifierProperty(PsiModifier.PROTECTED)) {
            return "protected";
        } else if (field.hasModifierProperty(PsiModifier.PRIVATE)) {
            return "private";
        } else {
            return "package"; // 包级别访问权限
        }
    }

    /**
     * 重新排列类中字段的物理位置（包括注解）
     * 仅对业务类生效，按照 static final -> static -> final -> 实例字段 的顺序
     */
    public static void rearrangeFieldsPhysically(PsiClass psiClass) {
        OneClickSettings settings = OneClickSettings.getInstance();

        ClassTypeDetector.ClassType classType = ClassTypeDetector.detectClassType(psiClass);

        // 只有业务类才启用字段排序
        if (!settings.isEnableFieldSorting() || classType != ClassTypeDetector.ClassType.BUSINESS_CLASS) {
            System.out.println("字段排序未启用或不是业务类，跳过排序。启用状态: " + settings.isEnableFieldSorting() + ", 类类型: " + classType);
            return;
        }

        // 获取所有字段并按类型分组
        List<PsiField> allFields = Arrays.asList(psiClass.getFields());
        if (allFields.size() <= 1) {
            System.out.println("字段数量 <= 1，无需排序");
            return; // 没有需要排序的字段
        }

        System.out.println("开始字段排序，共 " + allFields.size() + " 个字段");

        // 按字段类型分组
        List<PsiField> staticFinalFields = new ArrayList<>();  // static final 字段
        List<PsiField> staticFields = new ArrayList<>();       // static 字段
        List<PsiField> finalFields = new ArrayList<>();        // final 字段
        List<PsiField> instanceFields = new ArrayList<>();     // 实例字段

        for (PsiField field : allFields) {
            if (field.hasModifierProperty(PsiModifier.STATIC) && field.hasModifierProperty(PsiModifier.FINAL)) {
                staticFinalFields.add(field);
            } else if (field.hasModifierProperty(PsiModifier.STATIC)) {
                staticFields.add(field);
            } else if (field.hasModifierProperty(PsiModifier.FINAL)) {
                finalFields.add(field);
            } else {
                instanceFields.add(field);
            }
        }

        // 只对实例字段进行排序，其他字段保持原有顺序
        List<PsiField> sortedInstanceFields = sortFields(instanceFields, psiClass);

        // 构建最终的字段顺序：static final -> static -> final -> 排序后的实例字段
        List<PsiField> finalOrder = new ArrayList<>();
        finalOrder.addAll(staticFinalFields);
        finalOrder.addAll(staticFields);
        finalOrder.addAll(finalFields);
        finalOrder.addAll(sortedInstanceFields);

        // 检查是否需要重新排列
        boolean needsRearrangement = false;
        for (int i = 0; i < allFields.size(); i++) {
            if (!allFields.get(i).equals(finalOrder.get(i))) {
                needsRearrangement = true;
                break;
            }
        }

        if (!needsRearrangement) {
            System.out.println("字段顺序已正确，无需重新排列");
            return; // 字段已经是正确的顺序
        }

        System.out.println("需要重新排列字段");

        // 找到一个稳定的锚点 - 使用类的第一个方法或右大括号
        PsiElement anchor = null;
        PsiMethod[] methods = psiClass.getMethods();
        if (methods.length > 0) {
            // 如果有方法，在第一个方法之前插入
            anchor = methods[0];
            System.out.println("使用第一个方法作为锚点: " + methods[0].getName());
        } else {
            // 如果没有方法，在右大括号之前插入
            anchor = psiClass.getRBrace();
            System.out.println("使用右大括号作为锚点");
        }

        if (anchor == null) {
            System.err.println("无法找到插入锚点");
            return;
        }

        // 收集字段的完整文本（包括注释和注解）
        Map<PsiField, String> fieldTexts = new LinkedHashMap<>();
        for (PsiField field : finalOrder) {
            String fullText = collectFieldWithComments(field);
            fieldTexts.put(field, fullText);
            System.out.println("收集字段: " + field.getName() + " -> " + fullText.substring(0, Math.min(50, fullText.length())));
        }

        // 删除所有字段及其注释
        for (PsiField field : allFields) {
            // 删除字段前的注释
            PsiElement prev = field.getPrevSibling();
            while (prev != null && (prev instanceof PsiWhiteSpace || prev instanceof PsiComment)) {
                PsiElement toDelete = prev;
                prev = prev.getPrevSibling();
                toDelete.delete();
            }
            // 删除字段
            field.delete();
        }

        // 按新顺序重新添加字段
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(psiClass.getProject());
        int successCount = 0;
        int failCount = 0;

        for (Map.Entry<PsiField, String> entry : fieldTexts.entrySet()) {
            try {
                String fullText = entry.getValue();

                // 创建字段（包含注释和注解）
                PsiField newField = factory.createFieldFromText(fullText, psiClass);

                // 在anchor之前插入
                psiClass.addBefore(newField, anchor);

                successCount++;
                System.out.println("字段创建成功: " + newField.getName());
            } catch (Exception e) {
                failCount++;
                System.err.println("字段创建失败: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // 格式化代码
        try {
            CodeStyleManager.getInstance(psiClass.getProject()).reformat(psiClass);
        } catch (Exception e) {
            System.err.println("代码格式化失败: " + e.getMessage());
        }

        System.out.println("字段重新排列完成！成功: " + successCount + ", 失败: " + failCount);
    }

    /**
     * 收集字段及其注释的完整文本
     */
    private static String collectFieldWithComments(PsiField field) {
        StringBuilder sb = new StringBuilder();

        // 收集字段前的注释
        List<PsiElement> comments = new ArrayList<>();
        PsiElement prev = field.getPrevSibling();

        while (prev != null) {
            if (prev instanceof PsiComment) {
                comments.add(0, prev);
            } else if (prev instanceof PsiWhiteSpace) {
                String text = prev.getText();
                if (text.contains("\n\n")) {
                    break; // 遇到空行停止
                }
            } else if (!(prev instanceof PsiAnnotation)) {
                break; // 遇到非注释、非空白、非注解元素停止
            }
            prev = prev.getPrevSibling();
        }

        // 添加注释
        for (PsiElement comment : comments) {
            sb.append(comment.getText()).append("\n");
        }

        // 添加字段声明（包括注解）
        sb.append(field.getText());

        return sb.toString();
    }

    /**
     * 判断是否为常量字段
     */
    private static boolean isConstantField(PsiField field) {
        // 检查字段名是否为全大写（常量命名规范）
        String fieldName = field.getName();
        if (fieldName != null && fieldName.equals(fieldName.toUpperCase()) && fieldName.contains("_")) {
            return true;
        }

        // 检查是否为static final组合
        if (field.hasModifierProperty(PsiModifier.STATIC) && field.hasModifierProperty(PsiModifier.FINAL)) {
            return true;
        }

        return false;
    }

    /**
     * 找到字段插入的最佳位置
     */
    private static PsiElement findFieldInsertionPoint(PsiClass psiClass) {
        // 查找类中的第一个方法或内部类
        PsiElement[] children = psiClass.getChildren();
        for (PsiElement child : children) {
            if (child instanceof PsiMethod || child instanceof PsiClass) {
                return child.getPrevSibling();
            }
        }

        // 如果没有方法或内部类，插入到类的开始位置
        PsiElement lBrace = psiClass.getLBrace();
        return lBrace != null ? lBrace : null;
    }

    /**
     * 字段信息类，用于保存字段的完整信息（包括注释）
     */
    private static class FieldInfo {
        private final String commentText;
        private final String fieldDeclaration;

        public FieldInfo(PsiField field) {
            // 收集注释
            StringBuilder commentSb = new StringBuilder();
            PsiElement prevElement = field.getPrevSibling();
            List<String> comments = new ArrayList<>();

            // 收集字段前的所有注释
            while (prevElement != null) {
                if (prevElement instanceof PsiComment) {
                    comments.add(0, prevElement.getText()); // 插入到开头保持顺序
                } else if (prevElement instanceof PsiWhiteSpace) {
                    String text = prevElement.getText();
                    if (text.contains("\n\n")) {
                        // 遇到空行，停止收集注释
                        break;
                    }
                } else if (!(prevElement instanceof PsiAnnotation)) {
                    // 遇到非注释、非空白、非注解的元素，停止收集
                    break;
                }
                prevElement = prevElement.getPrevSibling();
            }

            // 构建注释文本
            for (String comment : comments) {
                commentSb.append(comment).append("\n");
            }
            this.commentText = commentSb.toString().trim();

            // 构建字段声明
            StringBuilder fieldSb = new StringBuilder();

            // 添加注解
            PsiAnnotation[] annotations = field.getAnnotations();
            for (PsiAnnotation annotation : annotations) {
                fieldSb.append(annotation.getText()).append(" ");
            }

            // 添加所有修饰符
            PsiModifierList modifierList = field.getModifierList();
            if (modifierList != null) {
                // 可见性修饰符
                if (modifierList.hasModifierProperty(PsiModifier.PUBLIC)) {
                    fieldSb.append("public ");
                } else if (modifierList.hasModifierProperty(PsiModifier.PROTECTED)) {
                    fieldSb.append("protected ");
                } else if (modifierList.hasModifierProperty(PsiModifier.PRIVATE)) {
                    fieldSb.append("private ");
                }

                // static修饰符
                if (modifierList.hasModifierProperty(PsiModifier.STATIC)) {
                    fieldSb.append("static ");
                }

                // final修饰符
                if (modifierList.hasModifierProperty(PsiModifier.FINAL)) {
                    fieldSb.append("final ");
                }

                // 其他修饰符
                if (modifierList.hasModifierProperty(PsiModifier.TRANSIENT)) {
                    fieldSb.append("transient ");
                }
                if (modifierList.hasModifierProperty(PsiModifier.VOLATILE)) {
                    fieldSb.append("volatile ");
                }
            }

            // 添加类型和字段名
            fieldSb.append(field.getType().getCanonicalText()).append(" ");
            fieldSb.append(field.getName());

            // 添加初始化表达式（如果有）
            PsiExpression initializer = field.getInitializer();
            if (initializer != null) {
                fieldSb.append(" = ").append(initializer.getText());
            }

            fieldSb.append(";");

            this.fieldDeclaration = fieldSb.toString();
        }

        public String getCommentText() {
            return commentText;
        }

        public String getFieldDeclaration() {
            return fieldDeclaration;
        }
    }

    /**
     * 检查是否存在指定字段的getter方法
     */
    public static boolean hasGetter(PsiClass psiClass, PsiField field) {
        String getterName = getGetterName(field);
        return Arrays.stream(psiClass.getMethods())
                .anyMatch(method -> getterName.equals(method.getName()) && 
                         method.getParameterList().getParametersCount() == 0);
    }

    /**
     * 检查是否存在指定字段的setter方法
     */
    public static boolean hasSetter(PsiClass psiClass, PsiField field) {
        String setterName = getSetterName(field);
        return Arrays.stream(psiClass.getMethods())
                .anyMatch(method -> setterName.equals(method.getName()) && 
                         method.getParameterList().getParametersCount() == 1);
    }

    /**
     * 检查是否存在toString方法
     */
    public static boolean hasToString(PsiClass psiClass) {
        return Arrays.stream(psiClass.getMethods())
                .anyMatch(method -> "toString".equals(method.getName()) && 
                         method.getParameterList().getParametersCount() == 0);
    }

    /**
     * 获取所有toString方法
     */
    public static List<PsiMethod> getToStringMethods(PsiClass psiClass) {
        return Arrays.stream(psiClass.getMethods())
                .filter(method -> "toString".equals(method.getName()) && 
                        method.getParameterList().getParametersCount() == 0)
                .collect(Collectors.toList());
    }

    /**
     * 处理内部类的JavaBean方法生成
     */
    public static void processInnerClasses(PsiClass parentClass, OneClickSettings settings) {
        processInnerClasses(parentClass, settings, 1);
    }

    /**
     * 递归处理内部类的JavaBean方法生成
     */
    public static void processInnerClasses(PsiClass parentClass, OneClickSettings settings, int depth) {
        if (!settings.isProcessInnerClasses() || depth > settings.getMaxInnerClassDepth()) {
            return;
        }

        PsiClass[] innerClasses = parentClass.getInnerClasses();
        for (PsiClass innerClass : innerClasses) {
            if (innerClass != null && !innerClass.isInterface() && !innerClass.isEnum()) {
                // 为内部类生成JavaBean方法
                generateInnerClassMethods(innerClass, settings, depth);

                // 递归处理嵌套的内部类
                processInnerClasses(innerClass, settings, depth + 1);
            }
        }
    }

    /**
     * 为内部类生成JavaBean方法
     */
    private static void generateInnerClassMethods(PsiClass innerClass, OneClickSettings settings, int depth) {
        try {
            List<PsiField> fields = getInstanceFields(innerClass);
            if (fields.isEmpty()) {
                return;
            }

            Project project = innerClass.getProject();
            PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);

            // 移除现有的JavaBean方法
            List<PsiMethod> existingMethods = getAllJavaBeanMethods(innerClass, fields);
            for (PsiMethod method : existingMethods) {
                method.delete();
            }

            // 移除现有的分割注释
            if (settings.isGenerateSeparatorComment()) {
                removeSeparatorComment(innerClass);
            }

            // 找到插入点
            PsiElement insertionPoint = findInsertionPoint(innerClass, fields);
            PsiElement lastInserted = insertionPoint;

            // 添加内部类分割注释
            if (settings.isGenerateInnerClassSeparator()) {
                String separatorText = "// Inner Class " + innerClass.getName() + " Methods";
                PsiComment separatorComment = factory.createCommentFromText(separatorText, innerClass);
                lastInserted = insertCommentAfter(innerClass, separatorComment, lastInserted);
            }

            // 生成getter和setter方法
            if (settings.isGenerateGetterSetter()) {
                for (PsiField field : fields) {
                    // 生成getter
                    String getterCode = generateGetterCode(field);
                    PsiMethod getterMethod = factory.createMethodFromText(getterCode, innerClass);
                    lastInserted = insertAfter(innerClass, getterMethod, lastInserted);

                    // 生成setter
                    String setterCode = settings.isGenerateFluentSetters()
                        ? generateFluentSetterCode(field, innerClass)
                        : generateSetterCode(field);
                    PsiMethod setterMethod = factory.createMethodFromText(setterCode, innerClass);
                    lastInserted = insertAfter(innerClass, setterMethod, lastInserted);
                }
            }

            // 生成toString方法
            if (settings.isGenerateToString()) {
                String toStringCode;
                switch (settings.getToStringStyle()) {
                    case "simple":
                        toStringCode = generateSimpleToStringCode(innerClass);
                        break;
                    case "apache":
                        toStringCode = generateApacheToStringCode(innerClass);
                        break;
                    default:
                        toStringCode = generateToStringCode(innerClass);
                        break;
                }
                PsiMethod toStringMethod = factory.createMethodFromText(toStringCode, innerClass);
                insertAfter(innerClass, toStringMethod, lastInserted);
            }

        } catch (Exception e) {
            // 记录错误但不中断处理
            System.err.println("Error processing inner class " + innerClass.getName() + ": " + e.getMessage());
        }
    }

    /**
     * 生成getter方法名
     * 注意：boolean（基本类型）使用isXxx()，Boolean（包装类型）使用getXxx()
     */
    public static String getGetterName(PsiField field) {
        String fieldName = field.getName();
        PsiType fieldType = field.getType();

        // 只有基本类型boolean才使用isXxx()，包装类型Boolean使用getXxx()
        if (PsiType.BOOLEAN.equals(fieldType)) {
            if (fieldName.startsWith("is")) {
                return fieldName;
            } else {
                return "is" + capitalize(fieldName);
            }
        } else {
            return "get" + capitalize(fieldName);
        }
    }

    /**
     * 生成setter方法名
     */
    public static String getSetterName(PsiField field) {
        String fieldName = field.getName();
        PsiType fieldType = field.getType();

        // 对于boolean类型且字段名以"is"开头的情况，setter应该去掉"is"前缀
        if (PsiType.BOOLEAN.equals(fieldType) && fieldName.startsWith("is") && fieldName.length() > 2) {
            String nameWithoutIs = fieldName.substring(2);
            return "set" + capitalize(nameWithoutIs);
        } else {
            return "set" + capitalize(fieldName);
        }
    }

    /**
     * 首字母大写
     */
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 生成getter方法代码
     */
    public static String generateGetterCode(PsiField field) {
        String getterName = getGetterName(field);
        String fieldName = field.getName();
        String returnType = field.getType().getCanonicalText();
        
        return String.format(
            "public %s %s() {\n" +
            "    return %s;\n" +
            "}", 
            returnType, getterName, fieldName
        );
    }

    /**
     * 生成setter方法代码
     */
    public static String generateSetterCode(PsiField field) {
        String setterName = getSetterName(field);
        String fieldName = field.getName();
        String paramType = field.getType().getCanonicalText();

        return String.format(
            "public void %s(%s %s) {\n" +
            "    this.%s = %s;\n" +
            "}",
            setterName, paramType, fieldName, fieldName, fieldName
        );
    }

    /**
     * 生成fluent setter方法代码（返回this）
     */
    public static String generateFluentSetterCode(PsiField field, PsiClass psiClass) {
        String fieldName = field.getName();
        String fieldType = field.getType().getCanonicalText();
        String methodName = "set" + capitalize(fieldName);
        String className = psiClass.getName();

        return String.format(
            "public %s %s(%s %s) {\n" +
            "    this.%s = %s;\n" +
            "    return this;\n" +
            "}",
            className, methodName, fieldType, fieldName, fieldName, fieldName
        );
    }

    /**
     * 生成JSON格式的toString方法代码
     */
    public static String generateToStringCode(PsiClass psiClass) {
        List<PsiField> fields = getInstanceFields(psiClass);

        StringBuilder sb = new StringBuilder();
        sb.append("@Override\n");
        sb.append("public String toString() {\n");
        sb.append("    return \"{\" +\n");

        for (int i = 0; i < fields.size(); i++) {
            PsiField field = fields.get(i);
            String fieldName = field.getName();

            sb.append("            \"\\\"").append(fieldName).append("\\\":");

            // 处理不同类型的字段
            PsiType fieldType = field.getType();
            if (fieldType.equalsToText("java.lang.String")) {
                sb.append("\\\"\" + ").append(fieldName).append(" + \"\\\"\"");
            } else {
                sb.append("\" + ").append(fieldName);
            }

            if (i < fields.size() - 1) {
                sb.append(" + \",\" +\n");
            } else {
                sb.append(" +\n");
            }
        }

        sb.append("            \"}\";\n");
        sb.append("}");

        return sb.toString();
    }

    /**
     * 生成简单toString方法代码
     */
    public static String generateSimpleToStringCode(PsiClass psiClass) {
        List<PsiField> fields = getInstanceFields(psiClass);
        String className = psiClass.getName();

        StringBuilder sb = new StringBuilder();
        sb.append("@Override\n");
        sb.append("public String toString() {\n");
        sb.append("    return \"").append(className).append("{\" +\n");

        for (int i = 0; i < fields.size(); i++) {
            PsiField field = fields.get(i);
            String fieldName = field.getName();

            if (i == 0) {
                sb.append("            \"").append(fieldName).append("=\" + ").append(fieldName);
            } else {
                sb.append(" +\n            \", ").append(fieldName).append("=\" + ").append(fieldName);
            }
        }

        sb.append(" +\n            \"}\";\n");
        sb.append("}");

        return sb.toString();
    }

    /**
     * 生成Apache Commons风格toString方法代码
     */
    public static String generateApacheToStringCode(PsiClass psiClass) {
        List<PsiField> fields = getInstanceFields(psiClass);

        StringBuilder sb = new StringBuilder();
        sb.append("@Override\n");
        sb.append("public String toString() {\n");
        sb.append("    return new ToStringBuilder(this)\n");

        for (PsiField field : fields) {
            String fieldName = field.getName();
            sb.append("            .append(\"").append(fieldName).append("\", ").append(fieldName).append(")\n");
        }

        sb.append("            .toString();\n");
        sb.append("}");

        return sb.toString();
    }

    /**
     * 检查方法是否为getter方法
     */
    public static boolean isGetterMethod(PsiMethod method) {
        String name = method.getName();
        return (name.startsWith("get") || name.startsWith("is")) && 
               method.getParameterList().getParametersCount() == 0 &&
               !PsiType.VOID.equals(method.getReturnType());
    }

    /**
     * 检查方法是否为setter方法
     */
    public static boolean isSetterMethod(PsiMethod method) {
        String name = method.getName();
        return name.startsWith("set") && 
               method.getParameterList().getParametersCount() == 1 &&
               PsiType.VOID.equals(method.getReturnType());
    }

    /**
     * 检查方法是否为toString方法
     */
    public static boolean isToStringMethod(PsiMethod method) {
        return "toString".equals(method.getName()) &&
               method.getParameterList().getParametersCount() == 0;
    }



    /**
     * 获取类中所有的JavaBean方法（getter/setter/toString）
     */
    public static List<PsiMethod> getAllJavaBeanMethods(PsiClass psiClass, List<PsiField> fields) {
        List<PsiMethod> javaBeanMethods = new ArrayList<>();

        for (PsiMethod method : psiClass.getMethods()) {
            String methodName = method.getName();

            // 检查是否为toString方法
            if (isToStringMethod(method)) {
                javaBeanMethods.add(method);
                continue;
            }

            // 检查是否为getter或setter方法
            for (PsiField field : fields) {
                String getterName = getGetterName(field);
                String setterName = getSetterName(field);

                if (methodName.equals(getterName) || methodName.equals(setterName)) {
                    javaBeanMethods.add(method);
                    break;
                }
            }
        }

        return javaBeanMethods;
    }

    /**
     * 生成分割注释
     */
    public static String generateSeparatorComment() {
        return "// ================================ JavaBean Methods ================================";
    }

    /**
     * 创建格式化的分割注释元素
     */
    public static PsiElement createFormattedSeparatorComment(PsiElementFactory factory, PsiClass psiClass) {
        // 创建一个简单的注释
        return factory.createCommentFromText(generateSeparatorComment(), psiClass);
    }

    /**
     * 移除现有的分割注释
     */
    public static void removeSeparatorComment(PsiClass psiClass) {
        // 使用列表收集要删除的元素，避免在遍历时修改集合
        List<PsiElement> elementsToDelete = new ArrayList<>();

        PsiElement[] children = psiClass.getChildren();
        for (int i = 0; i < children.length; i++) {
            PsiElement child = children[i];

            // 检查注释元素
            if (child instanceof PsiComment) {
                String commentText = child.getText();
                if (commentText.contains("JavaBean Methods") ||
                    commentText.contains("================================")) {
                    elementsToDelete.add(child);

                    // 检查前面的空白字符
                    if (i > 0 && children[i-1] instanceof PsiWhiteSpace) {
                        String prevText = children[i-1].getText();
                        // 如果前面的空白包含多个换行，说明是我们添加的
                        if (prevText.contains("\n\n")) {
                            elementsToDelete.add(children[i-1]);
                        }
                    }

                    // 检查后面的空白字符
                    if (i < children.length - 1 && children[i+1] instanceof PsiWhiteSpace) {
                        String nextText = children[i+1].getText();
                        // 如果后面的空白包含多个换行，说明是我们添加的
                        if (nextText.contains("\n\n")) {
                            elementsToDelete.add(children[i+1]);
                        }
                    }
                }
            }
        }

        // 删除收集到的元素
        for (PsiElement element : elementsToDelete) {
            try {
                element.delete();
            } catch (Exception e) {
                // 忽略删除失败的情况，可能元素已经被删除
                System.out.println("Failed to delete element: " + e.getMessage());
            }
        }
    }

    /**
     * 获取类中所有的业务方法（非JavaBean方法）
     * 业务方法是指除了getter/setter/toString之外的方法
     */
    public static List<PsiMethod> getBusinessMethods(PsiClass psiClass, List<PsiField> fields) {
        List<PsiMethod> businessMethods = new ArrayList<>();

        for (PsiMethod method : psiClass.getMethods()) {
            // 跳过构造方法
            if (method.isConstructor()) {
                continue;
            }

            // 跳过JavaBean方法
            if (isJavaBeanMethod(method, fields)) {
                continue;
            }

            businessMethods.add(method);
        }

        return businessMethods;
    }

    /**
     * 检查方法是否为JavaBean方法（getter/setter/toString）
     */
    public static boolean isJavaBeanMethod(PsiMethod method, List<PsiField> fields) {
        // 检查是否为toString方法
        if (isToStringMethod(method)) {
            return true;
        }

        // 检查是否为getter或setter方法
        String methodName = method.getName();
        for (PsiField field : fields) {
            String getterName = getGetterName(field);
            String setterName = getSetterName(field);

            if (methodName.equals(getterName) && isGetterMethod(method)) {
                return true;
            }
            if (methodName.equals(setterName) && isSetterMethod(method)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 找到插入JavaBean方法的最佳位置
     * 返回应该在其后插入的元素，如果返回null则表示应该插入到类的开始位置
     */
    public static PsiElement findInsertionPoint(PsiClass psiClass, List<PsiField> fields) {
        List<PsiMethod> businessMethods = getBusinessMethods(psiClass, fields);

        // 如果没有业务方法，在最后一个字段后插入
        if (businessMethods.isEmpty()) {
            PsiField[] allFields = psiClass.getFields();
            if (allFields.length > 0) {
                return allFields[allFields.length - 1];
            }
            // 如果连字段都没有，返回null表示插入到类的开始
            return null;
        }

        // 找到最后一个业务方法
        PsiMethod lastBusinessMethod = businessMethods.get(businessMethods.size() - 1);
        return lastBusinessMethod;
    }

    /**
     * 在指定位置之后插入元素
     * 如果anchor为null，则插入到类的开始位置
     */
    public static PsiElement insertAfter(PsiClass psiClass, PsiElement elementToInsert, PsiElement anchor) {
        PsiElement inserted;
        if (anchor == null) {
            // 插入到类的开始位置（在左大括号之后）
            PsiElement lBrace = psiClass.getLBrace();
            if (lBrace != null) {
                inserted = psiClass.addAfter(elementToInsert, lBrace);
            } else {
                inserted = psiClass.add(elementToInsert);
            }
        } else {
            inserted = psiClass.addAfter(elementToInsert, anchor);
        }
        return inserted;
    }

    /**
     * 在指定位置之后插入注释，并确保有适当的换行
     */
    public static PsiElement insertCommentAfter(PsiClass psiClass, PsiElement commentToInsert, PsiElement anchor) {
        // 先插入注释
        PsiElement inserted = insertAfter(psiClass, commentToInsert, anchor);

        // 在注释前添加换行（如果需要）
        if (anchor != null) {
            try {
                PsiElementFactory factory = JavaPsiFacade.getElementFactory(psiClass.getProject());
                // 创建一个包含换行的代码块来提取换行符
                PsiMethod dummyMethod = factory.createMethodFromText("void dummy() {\n\n    // comment\n}", psiClass);
                PsiCodeBlock codeBlock = dummyMethod.getBody();
                if (codeBlock != null) {
                    // 查找代码块中的空白字符
                    PsiElement[] children = codeBlock.getChildren();
                    for (PsiElement child : children) {
                        if (child instanceof PsiWhiteSpace && child.getText().contains("\n\n")) {
                            // 复制这个空白字符并插入到注释前
                            PsiElement newLine = child.copy();
                            psiClass.addBefore(newLine, inserted);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                // 忽略换行添加失败
                System.out.println("Failed to add newline before comment: " + e.getMessage());
            }
        }

        return inserted;
    }

    /**
     * 在类的末尾插入元素（在右大括号之前）
     */
    public static PsiElement insertAtEnd(PsiClass psiClass, PsiElement elementToInsert) {
        PsiElement rBrace = psiClass.getRBrace();
        if (rBrace != null) {
            return psiClass.addBefore(elementToInsert, rBrace);
        } else {
            return psiClass.add(elementToInsert);
        }
    }
}
