package com.glowxq.plugs.utils;

import com.intellij.psi.*;

/**
 * 类型检测工具类
 * 简化版本：所有类都识别为JavaBean
 */
public class ClassTypeDetector {

    /**
     * 类型枚举
     */
    public enum ClassType {
        JAVA_BEAN,      // JavaBean类
        ENUM            // 枚举类
    }

    /**
     * 检测类的类型
     * 简化版本：所有类都识别为JavaBean，枚举类识别为ENUM
     */
    public static ClassType detectClassType(PsiClass psiClass) {
        if (psiClass == null) {
            return ClassType.JAVA_BEAN;
        }

        // 检查是否为枚举类
        if (psiClass.isEnum()) {
            return ClassType.ENUM;
        }

        // 其他所有类都识别为JavaBean
        return ClassType.JAVA_BEAN;
    }
}
