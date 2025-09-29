package com.glowxq.plugs.demo;

/**
 * 字段排序演示类
 * 这是一个业务类（包名包含demo），应该启用字段排序功能
 * 
 * 使用方法：
 * 1. 确保在设置中启用了字段排序功能
 * 2. 在这个类中按 Ctrl+Alt+G (智能一键生成)
 * 3. 观察字段是否按照设置的排序规则重新排列
 * 
 * 当前字段顺序（未排序）：
 * - zLastField
 * - bShortField  
 * - cMediumField
 * - aLongFieldName
 * 
 * 按名称排序后应该是：
 * - aLongFieldName
 * - bShortField
 * - cMediumField
 * - zLastField
 */
public class FieldSortingDemo {

    // 这些字段故意按非字母顺序排列，用于测试排序功能
    
    private String zLastField;
    
    private Object bShortField;
    
    private Integer cMediumField;
    
    private String aLongFieldName;

    // 业务方法
    public void processData() {
        System.out.println("Processing data...");
    }

    public String formatOutput(String input) {
        return "Formatted: " + input;
    }

    public void validateInput(Object input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
    }
}
