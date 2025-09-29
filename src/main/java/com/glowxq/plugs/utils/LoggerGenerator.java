package com.glowxq.plugs.utils;

import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;

/**
 * 日志生成器
 */
public class LoggerGenerator {

    /**
     * 日志类型枚举
     */
    public enum LoggerType {
        SLF4J("org.slf4j.Logger", "org.slf4j.LoggerFactory"),
        LOG4J("org.apache.log4j.Logger", null),
        JUL("java.util.logging.Logger", null);

        private final String loggerClass;
        private final String factoryClass;

        LoggerType(String loggerClass, String factoryClass) {
            this.loggerClass = loggerClass;
            this.factoryClass = factoryClass;
        }

        public String getLoggerClass() {
            return loggerClass;
        }

        public String getFactoryClass() {
            return factoryClass;
        }
    }

    /**
     * 生成日志字段
     */
    public static PsiField generateLoggerField(PsiClass psiClass, String fieldName, LoggerType loggerType) {
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(psiClass.getProject());
        
        String loggerFieldCode = createLoggerFieldCode(psiClass, fieldName, loggerType);
        PsiField loggerField = factory.createFieldFromText(loggerFieldCode, psiClass);
        
        // 添加必要的import
        addImports(psiClass, loggerType);
        
        return loggerField;
    }

    /**
     * 创建日志字段代码
     */
    private static String createLoggerFieldCode(PsiClass psiClass, String fieldName, LoggerType loggerType) {
        String className = psiClass.getName();
        
        switch (loggerType) {
            case SLF4J:
                return String.format("private static final Logger %s = LoggerFactory.getLogger(%s.class);", 
                                   fieldName, className);
            case LOG4J:
                return String.format("private static final Logger %s = Logger.getLogger(%s.class);", 
                                   fieldName, className);
            case JUL:
                return String.format("private static final Logger %s = Logger.getLogger(%s.class.getName());", 
                                   fieldName, className);
            default:
                throw new IllegalArgumentException("Unsupported logger type: " + loggerType);
        }
    }

    /**
     * 添加必要的import语句
     */
    private static void addImports(PsiClass psiClass, LoggerType loggerType) {
        PsiFile containingFile = psiClass.getContainingFile();
        if (!(containingFile instanceof PsiJavaFile)) {
            return;
        }
        
        PsiJavaFile javaFile = (PsiJavaFile) containingFile;
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(psiClass.getProject());
        
        // 检查是否已经有import
        PsiImportList importList = javaFile.getImportList();
        if (importList == null) {
            return;
        }
        
        // 添加Logger类的import
        if (!hasImport(importList, loggerType.getLoggerClass())) {
            PsiImportStatement loggerImport = factory.createImportStatement(
                findClass(psiClass.getProject(), loggerType.getLoggerClass())
            );
            if (loggerImport != null) {
                importList.add(loggerImport);
            }
        }
        
        // 添加Factory类的import（如果需要）
        if (loggerType.getFactoryClass() != null && !hasImport(importList, loggerType.getFactoryClass())) {
            PsiImportStatement factoryImport = factory.createImportStatement(
                findClass(psiClass.getProject(), loggerType.getFactoryClass())
            );
            if (factoryImport != null) {
                importList.add(factoryImport);
            }
        }
    }

    /**
     * 检查是否已经有指定的import
     */
    private static boolean hasImport(PsiImportList importList, String qualifiedName) {
        PsiImportStatement[] imports = importList.getImportStatements();
        for (PsiImportStatement importStatement : imports) {
            if (qualifiedName.equals(importStatement.getQualifiedName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查找类
     */
    private static PsiClass findClass(com.intellij.openapi.project.Project project, String qualifiedName) {
        return JavaPsiFacade.getInstance(project).findClass(qualifiedName, GlobalSearchScope.allScope(project));
    }

    /**
     * 插入日志字段到类中（插入到第一行字段位置）
     */
    public static PsiElement insertLoggerField(PsiClass psiClass, String fieldName, LoggerType loggerType) {
        // 检查是否已经有日志字段
        if (ClassTypeDetector.hasLoggerField(psiClass)) {
            return null; // 已经有日志字段，不重复添加
        }

        // 生成日志字段
        PsiField loggerField = generateLoggerField(psiClass, fieldName, loggerType);

        // 插入到类的第一行（左大括号之后）
        PsiElement lBrace = psiClass.getLBrace();
        PsiElement inserted;

        if (lBrace != null) {
            inserted = psiClass.addAfter(loggerField, lBrace);
        } else {
            inserted = psiClass.add(loggerField);
        }

        // 格式化代码
        CodeStyleManager.getInstance(psiClass.getProject()).reformat(inserted);

        return inserted;
    }

    /**
     * 找到日志字段的插入位置（应该在第一个字段位置）
     */
    private static PsiElement findLoggerInsertionPoint(PsiClass psiClass) {
        PsiField[] fields = psiClass.getFields();

        // 如果有字段，插入到第一个字段之前
        if (fields.length > 0) {
            // 查找第一个非静态字段，日志字段应该在所有字段的最前面
            for (PsiField field : fields) {
                if (!field.hasModifierProperty(PsiModifier.STATIC)) {
                    return field.getPrevSibling(); // 插入到第一个非静态字段之前
                }
            }
            // 如果所有字段都是静态的，插入到最后一个静态字段之后
            return fields[fields.length - 1];
        }

        // 如果没有字段，插入到左大括号之后
        return psiClass.getLBrace();
    }

    /**
     * 根据字符串获取日志类型
     */
    public static LoggerType getLoggerType(String typeString) {
        if (typeString == null) {
            return LoggerType.SLF4J; // 默认使用SLF4J
        }
        
        switch (typeString.toLowerCase()) {
            case "slf4j":
                return LoggerType.SLF4J;
            case "log4j":
                return LoggerType.LOG4J;
            case "jul":
                return LoggerType.JUL;
            default:
                return LoggerType.SLF4J;
        }
    }

    /**
     * 检查项目中是否可用指定的日志框架
     */
    public static boolean isLoggerTypeAvailable(com.intellij.openapi.project.Project project, LoggerType loggerType) {
        PsiClass loggerClass = findClass(project, loggerType.getLoggerClass());
        return loggerClass != null;
    }

    /**
     * 获取推荐的日志类型（基于项目依赖）
     */
    public static LoggerType getRecommendedLoggerType(com.intellij.openapi.project.Project project) {
        // 按优先级检查
        if (isLoggerTypeAvailable(project, LoggerType.SLF4J)) {
            return LoggerType.SLF4J;
        } else if (isLoggerTypeAvailable(project, LoggerType.LOG4J)) {
            return LoggerType.LOG4J;
        } else {
            return LoggerType.JUL; // JUL总是可用的
        }
    }
}
