package com.glowxq.plugs.model;

/**
 * 测试Boolean和boolean字段的getter/setter方法生成
 * 
 * 预期行为：
 * - boolean类型字段：使用isXxx()方法
 * - Boolean类型字段：使用getXxx()方法
 * 
 * @author glowxq
 */
public class TestBooleanFieldBean {
    
    // 基本类型boolean - 应该生成isActive()
    private boolean active;
    
    // 包装类型Boolean - 应该生成getEnabled()
    private Boolean enabled;
    
    // 基本类型boolean，字段名以is开头 - 应该生成isValid()
    private boolean isValid;
    
    // 包装类型Boolean，字段名以is开头 - 应该生成getIsDeleted()
    private Boolean isDeleted;
    
    // 其他字段
    private String name;
    private Integer age;
    
    // 测试DTO/VO/BO生成时的toEntity和fromEntity方法
    // 应该正确调用对应的getter/setter方法
}

