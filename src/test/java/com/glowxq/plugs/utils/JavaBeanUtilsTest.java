package com.glowxq.plugs.utils;

import com.intellij.psi.*;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import org.junit.Test;

import java.util.List;

/**
 * JavaBeanUtils的单元测试
 * 验证方法插入位置的正确性
 */
public class JavaBeanUtilsTest extends LightJavaCodeInsightFixtureTestCase {

    @Test
    public void testGetBusinessMethods() {
        // 创建测试类
        String testClass = """
            public class TestClass {
                private String name;
                private int age;
                
                public void businessMethod1() {
                    // business logic
                }
                
                public String getName() {
                    return name;
                }
                
                public void setName(String name) {
                    this.name = name;
                }
                
                public boolean validateData() {
                    return name != null;
                }
                
                @Override
                public String toString() {
                    return "TestClass";
                }
            }
            """;
        
        PsiJavaFile psiFile = (PsiJavaFile) myFixture.configureByText("TestClass.java", testClass);
        PsiClass psiClass = psiFile.getClasses()[0];
        List<PsiField> fields = JavaBeanUtils.getInstanceFields(psiClass);
        
        // 获取业务方法
        List<PsiMethod> businessMethods = JavaBeanUtils.getBusinessMethods(psiClass, fields);
        
        // 验证结果
        assertEquals("应该有2个业务方法", 2, businessMethods.size());
        assertEquals("第一个业务方法名称", "businessMethod1", businessMethods.get(0).getName());
        assertEquals("第二个业务方法名称", "validateData", businessMethods.get(1).getName());
    }

    @Test
    public void testIsJavaBeanMethod() {
        String testClass = """
            public class TestClass {
                private String name;
                
                public String getName() {
                    return name;
                }
                
                public void setName(String name) {
                    this.name = name;
                }
                
                public void businessMethod() {
                    // business logic
                }
                
                @Override
                public String toString() {
                    return "TestClass";
                }
            }
            """;
        
        PsiJavaFile psiFile = (PsiJavaFile) myFixture.configureByText("TestClass.java", testClass);
        PsiClass psiClass = psiFile.getClasses()[0];
        List<PsiField> fields = JavaBeanUtils.getInstanceFields(psiClass);
        PsiMethod[] methods = psiClass.getMethods();
        
        // 验证各个方法的类型判断
        for (PsiMethod method : methods) {
            String methodName = method.getName();
            boolean isJavaBean = JavaBeanUtils.isJavaBeanMethod(method, fields);
            
            switch (methodName) {
                case "getName":
                case "setName":
                case "toString":
                    assertTrue(methodName + " 应该被识别为JavaBean方法", isJavaBean);
                    break;
                case "businessMethod":
                    assertFalse(methodName + " 不应该被识别为JavaBean方法", isJavaBean);
                    break;
            }
        }
    }

    @Test
    public void testFindInsertionPointWithBusinessMethods() {
        String testClass = """
            public class TestClass {
                private String name;
                private int age;
                
                public void firstBusinessMethod() {
                    // business logic
                }
                
                public void secondBusinessMethod() {
                    // business logic
                }
                
                public String getName() {
                    return name;
                }
            }
            """;
        
        PsiJavaFile psiFile = (PsiJavaFile) myFixture.configureByText("TestClass.java", testClass);
        PsiClass psiClass = psiFile.getClasses()[0];
        List<PsiField> fields = JavaBeanUtils.getInstanceFields(psiClass);
        
        // 找到插入点
        PsiElement insertionPoint = JavaBeanUtils.findInsertionPoint(psiClass, fields);
        
        // 验证插入点是最后一个业务方法
        assertNotNull("插入点不应该为null", insertionPoint);
        assertTrue("插入点应该是方法", insertionPoint instanceof PsiMethod);
        assertEquals("插入点应该是最后一个业务方法", "secondBusinessMethod", ((PsiMethod) insertionPoint).getName());
    }

    @Test
    public void testFindInsertionPointWithoutBusinessMethods() {
        String testClass = """
            public class TestClass {
                private String name;
                private int age;
                
                public String getName() {
                    return name;
                }
            }
            """;
        
        PsiJavaFile psiFile = (PsiJavaFile) myFixture.configureByText("TestClass.java", testClass);
        PsiClass psiClass = psiFile.getClasses()[0];
        List<PsiField> fields = JavaBeanUtils.getInstanceFields(psiClass);
        
        // 找到插入点
        PsiElement insertionPoint = JavaBeanUtils.findInsertionPoint(psiClass, fields);
        
        // 验证插入点是最后一个字段
        assertNotNull("插入点不应该为null", insertionPoint);
        assertTrue("插入点应该是字段", insertionPoint instanceof PsiField);
        assertEquals("插入点应该是最后一个字段", "age", ((PsiField) insertionPoint).getName());
    }
}
