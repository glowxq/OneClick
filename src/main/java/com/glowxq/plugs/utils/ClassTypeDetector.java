package com.glowxq.plugs.utils;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 类型检测工具类
 * 用于判断一个类是JavaBean还是业务类
 */
public class ClassTypeDetector {

    /**
     * 类型枚举
     */
    public enum ClassType {
        JAVA_BEAN,      // JavaBean类
        BUSINESS_CLASS, // 业务类
        UNKNOWN         // 未知类型
    }

    // JavaBean常见的包名模式
    private static final Set<String> BEAN_PACKAGE_PATTERNS = new HashSet<>(Arrays.asList(
            "entity", "model", "bean", "pojo", "dto", "vo", "domain", "data"
    ));

    // 业务类常见的包名模式
    private static final Set<String> BUSINESS_PACKAGE_PATTERNS = new HashSet<>(Arrays.asList(
            "service", "controller", "manager", "handler", "processor", "component",
            "util", "helper", "factory", "builder", "config", "configuration"
    ));

    // JavaBean常见的注解
    private static final Set<String> BEAN_ANNOTATIONS = new HashSet<>(Arrays.asList(
            "Entity", "Table", "Document", "Data", "Getter", "Setter", "ToString",
            "EqualsAndHashCode", "NoArgsConstructor", "AllArgsConstructor",
            "JsonIgnoreProperties", "JsonProperty", "XmlRootElement", "XmlElement"
    ));

    // 业务类常见的注解
    private static final Set<String> BUSINESS_ANNOTATIONS = new HashSet<>(Arrays.asList(
            "Service", "Controller", "RestController", "Component", "Repository",
            "Configuration", "Bean", "Autowired", "Inject", "Resource",
            "RequestMapping", "GetMapping", "PostMapping", "PutMapping", "DeleteMapping"
    ));

    /**
     * 检测类的类型
     */
    public static ClassType detectClassType(PsiClass psiClass) {
        if (psiClass == null) {
            return ClassType.UNKNOWN;
        }

        int beanScore = 0;
        int businessScore = 0;

        // 1. 检查包名
        String packageName = getPackageName(psiClass);
        if (packageName != null) {
            String lowerPackage = packageName.toLowerCase();
            for (String pattern : BEAN_PACKAGE_PATTERNS) {
                if (lowerPackage.contains(pattern)) {
                    beanScore += 3;
                    break;
                }
            }
            for (String pattern : BUSINESS_PACKAGE_PATTERNS) {
                if (lowerPackage.contains(pattern)) {
                    businessScore += 3;
                    break;
                }
            }
        }

        // 2. 检查类注解
        PsiAnnotation[] annotations = psiClass.getAnnotations();
        for (PsiAnnotation annotation : annotations) {
            String annotationName = getSimpleAnnotationName(annotation);
            if (BEAN_ANNOTATIONS.contains(annotationName)) {
                beanScore += 2;
            }
            if (BUSINESS_ANNOTATIONS.contains(annotationName)) {
                businessScore += 2;
            }
        }

        // 3. 检查字段特征
        PsiField[] fields = psiClass.getFields();
        int privateFieldCount = 0;
        int staticFieldCount = 0;
        boolean hasLogger = false;

        for (PsiField field : fields) {
            if (field.hasModifierProperty(PsiModifier.PRIVATE)) {
                privateFieldCount++;
            }
            if (field.hasModifierProperty(PsiModifier.STATIC)) {
                staticFieldCount++;
            }
            
            // 检查是否有日志字段
            String fieldType = field.getType().getCanonicalText();
            if (fieldType.contains("Logger") || fieldType.contains("Log")) {
                hasLogger = true;
                businessScore += 1;
            }
        }

        // 4. 检查方法特征
        PsiMethod[] methods = psiClass.getMethods();
        int getterSetterCount = 0;
        int businessMethodCount = 0;

        for (PsiMethod method : methods) {
            String methodName = method.getName();
            
            // 检查getter/setter方法
            if (isGetterOrSetter(method, fields)) {
                getterSetterCount++;
            } else if (!method.isConstructor() && 
                      !methodName.equals("toString") && 
                      !methodName.equals("equals") && 
                      !methodName.equals("hashCode")) {
                businessMethodCount++;
            }
        }

        // 5. 根据字段和方法特征评分
        if (privateFieldCount > 0 && getterSetterCount >= privateFieldCount) {
            beanScore += 2;
        }
        
        if (businessMethodCount > 2) {
            businessScore += 2;
        }

        if (hasLogger) {
            businessScore += 1;
        }

        // 6. 检查继承关系
        PsiClass superClass = psiClass.getSuperClass();
        if (superClass != null) {
            String superClassName = superClass.getName();
            if (superClassName != null) {
                if (superClassName.contains("Entity") || superClassName.contains("Model")) {
                    beanScore += 1;
                } else if (superClassName.contains("Service") || superClassName.contains("Controller")) {
                    businessScore += 1;
                }
            }
        }

        // 7. 检查接口实现
        PsiClassType[] interfaces = psiClass.getImplementsListTypes();
        for (PsiClassType interfaceType : interfaces) {
            String interfaceName = interfaceType.getClassName();
            if (interfaceName != null) {
                if (interfaceName.equals("Serializable")) {
                    beanScore += 1;
                } else if (interfaceName.contains("Service") || interfaceName.contains("Repository")) {
                    businessScore += 1;
                }
            }
        }

        // 决定类型
        if (beanScore > businessScore) {
            return ClassType.JAVA_BEAN;
        } else if (businessScore > beanScore) {
            return ClassType.BUSINESS_CLASS;
        } else {
            // 分数相等时，根据默认规则判断
            if (privateFieldCount > 0 && getterSetterCount > 0 && businessMethodCount <= 1) {
                return ClassType.JAVA_BEAN;
            } else {
                return ClassType.BUSINESS_CLASS;
            }
        }
    }

    /**
     * 获取包名
     */
    private static String getPackageName(PsiClass psiClass) {
        PsiFile containingFile = psiClass.getContainingFile();
        if (containingFile instanceof PsiJavaFile) {
            return ((PsiJavaFile) containingFile).getPackageName();
        }
        return null;
    }

    /**
     * 获取注解的简单名称
     */
    private static String getSimpleAnnotationName(PsiAnnotation annotation) {
        String qualifiedName = annotation.getQualifiedName();
        if (qualifiedName != null) {
            int lastDot = qualifiedName.lastIndexOf('.');
            return lastDot >= 0 ? qualifiedName.substring(lastDot + 1) : qualifiedName;
        }
        return "";
    }

    /**
     * 检查方法是否为getter或setter
     */
    private static boolean isGetterOrSetter(PsiMethod method, PsiField[] fields) {
        String methodName = method.getName();
        
        // 检查getter
        if (methodName.startsWith("get") && methodName.length() > 3) {
            String fieldName = methodName.substring(3);
            fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
            return hasField(fields, fieldName) && method.getParameterList().getParametersCount() == 0;
        }
        
        // 检查boolean getter
        if (methodName.startsWith("is") && methodName.length() > 2) {
            String fieldName = methodName.substring(2);
            fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
            return hasField(fields, fieldName) && method.getParameterList().getParametersCount() == 0;
        }
        
        // 检查setter
        if (methodName.startsWith("set") && methodName.length() > 3) {
            String fieldName = methodName.substring(3);
            fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
            return hasField(fields, fieldName) && method.getParameterList().getParametersCount() == 1;
        }
        
        return false;
    }

    /**
     * 检查是否有指定名称的字段
     */
    private static boolean hasField(PsiField[] fields, String fieldName) {
        for (PsiField field : fields) {
            if (fieldName.equals(field.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查类是否已经有日志字段
     */
    public static boolean hasLoggerField(PsiClass psiClass) {
        PsiField[] fields = psiClass.getFields();
        for (PsiField field : fields) {
            String fieldType = field.getType().getCanonicalText();
            String fieldName = field.getName();
            
            if ((fieldType.contains("Logger") || fieldType.contains("Log")) &&
                (fieldName != null && (fieldName.toUpperCase().contains("LOG") || 
                 fieldName.toUpperCase().contains("LOGGER")))) {
                return true;
            }
        }
        return false;
    }
}
