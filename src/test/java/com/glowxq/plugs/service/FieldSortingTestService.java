package com.glowxq.plugs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字段排序测试业务类
 * 用于验证字段排序功能：static final -> static -> final -> 实例字段
 *
 * @author glowxq
 */
public class FieldSortingTestService {

    // static final 字段（常量）- 应该保持在最上面
    private static final Logger LOGGER = LoggerFactory.getLogger(FieldSortingTestService.class);
    private static final String CONSTANT_VALUE = "CONSTANT";
    private static final int MAX_RETRY_COUNT = 3;

    // static 字段 - 应该在 static final 之后
    private static String staticField = "static";
    private static int staticCounter = 0;

    // final 字段 - 应该在 static 之后
    private final String finalField = "final";
    private final long serialVersionUID = 1L;

    // 实例字段 - 应该在最后，并且可以排序
    private String userName;
    private String email;
    private int age;
    private boolean active;
    private String address;

    // 构造函数
    public FieldSortingTestService() {
        // 构造函数内容
    }

    // 方法
    public void testMethod() {
        LOGGER.info("Test method");
    }
}
