package com.glowxq.plugs.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 测试字段排序功能的JavaBean类
 * 包含不同长度和类型的字段，用于验证排序功能
 * 
 * @author glowxq
 */
public class TestFieldSortingBean {

    // 不同长度的字段名，用于测试按名称长度排序
    private String a;                    // 长度1
    private String bb;                   // 长度2
    private String ccc;                  // 长度3
    private String dddd;                 // 长度4
    private String eeeee;                // 长度5
    
    // 不同类型的字段，用于测试按类型排序
    private boolean flag;                // boolean类型
    private int count;                   // int类型
    private long timestamp;              // long类型
    private String name;                 // String类型
    private LocalDateTime createTime;    // LocalDateTime类型
    private List<String> items;          // List类型
    
    // 按字母顺序的字段，用于测试按名称排序
    private String alpha;
    private String beta;
    private String gamma;
    private String delta;
    private String epsilon;

    // 在这里使用插件生成getter/setter/toString方法
    // 可以在设置中启用字段排序功能，测试不同的排序方式：
    // 1. 按名称排序（字母顺序）
    // 2. 按长度排序（字段名长度）
    // 3. 按类型排序（数据类型）
    // 4. 升序/降序排列
}
