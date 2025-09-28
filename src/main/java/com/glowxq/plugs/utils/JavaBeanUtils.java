package com.glowxq.plugs.utils;

import com.intellij.psi.*;
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
        return Arrays.stream(psiClass.getFields())
                .filter(field -> !field.hasModifierProperty(PsiModifier.STATIC))
                .filter(field -> !field.hasModifierProperty(PsiModifier.FINAL))
                .collect(Collectors.toList());
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
     * 生成getter方法名
     */
    public static String getGetterName(PsiField field) {
        String fieldName = field.getName();
        PsiType fieldType = field.getType();
        
        if (PsiType.BOOLEAN.equals(fieldType) && fieldName.startsWith("is")) {
            return fieldName;
        } else if (PsiType.BOOLEAN.equals(fieldType)) {
            return "is" + capitalize(fieldName);
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
}
