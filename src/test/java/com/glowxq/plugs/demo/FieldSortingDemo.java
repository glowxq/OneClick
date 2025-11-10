package com.glowxq.plugs.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldSortingDemo.class);

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

    // ================================ JavaBean Methods ================================

    public String getZLastField() {
        return zLastField;
    }

    public void setZLastField(String zLastField) {
        this.zLastField = zLastField;
    }

    public Object getBShortField() {
        return bShortField;
    }

    public void setBShortField(Object bShortField) {
        this.bShortField = bShortField;
    }

    public Integer getCMediumField() {
        return cMediumField;
    }

    public void setCMediumField(Integer cMediumField) {
        this.cMediumField = cMediumField;
    }

    public String getALongFieldName() {
        return aLongFieldName;
    }

    public void setALongFieldName(String aLongFieldName) {
        this.aLongFieldName = aLongFieldName;
    }

    @Override
    public String toString() {
        return "{" +
                "\"zLastField\":" + zLastField + "," +
                "\"bShortField\":" + bShortField + "," +
                "\"cMediumField\":" + cMediumField + "," +
                "\"aLongFieldName\":" + aLongFieldName +
                "}";
    }
}
