package com.glowxq.plugs.service;

/**
 * 测试字段排序的业务类
 * 这个类应该被识别为业务类，字段排序功能应该生效
 */
public class TestFieldSortingService {

    // 这些字段应该按照设置的排序规则重新排列
    // 默认按名称排序：应该是 aLongFieldName, bShortField, cMediumField, zLastField

    // @NotNull - 模拟注解
    private String zLastField;

    // @Resource - 模拟注解
    private Object bShortField;

    private Integer cMediumField;

    // @NotNull @Resource - 模拟多个注解
    private String aLongFieldName;

    // 业务方法
    public void doSomething() {
        System.out.println("Business logic here");
    }

    public String processData(String input) {
        return "Processed: " + input;
    }
}
