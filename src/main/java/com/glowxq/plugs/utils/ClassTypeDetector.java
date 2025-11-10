package com.glowxq.plugs.utils;

import com.intellij.psi.*;

/**
 * 类型检测工具类
 * 
 * <p>简化版本：所有类都识别为JavaBean，只有枚举类识别为ENUM</p>
 * 
 * @author glowxq
 */
public class ClassTypeDetector {

    /**
     * 类型枚举
     */
    public enum ClassType {
        /** JavaBean类 */
        JAVA_BEAN,
        /** 枚举类 */
        ENUM
    }

    /**
     * 检测类的类型
     * 
     * <p>简化版本：所有类都识别为JavaBean，只有枚举类识别为ENUM</p>
     * 
     * @param psiClass 要检测的类
     * @return 类的类型，如果为null则返回JAVA_BEAN
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
