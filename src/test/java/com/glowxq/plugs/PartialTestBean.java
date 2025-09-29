package com.glowxq.plugs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 部分已有getter/setter的测试类
 * 用于验证插件能为剩余字段生成方法
 * @author glowxq
 */
public class PartialTestBean {

    private String name;
    private int age;
    private boolean active;
    private boolean isVip;
    private List<String> tags;

    private List<ReorganizeTestBean> reorganizeTestBeans =  List.of(new ReorganizeTestBean());

    private static final Logger LOGGER = LoggerFactory.getLogger(PartialTestBean.class);

    // 测试说明：
    // 1. 将光标放在类定义内
    // 2. 使用 Cmd+Option+G 生成剩余字段的getter/setter
    // 3. 应该生成：
    //    - getAge() / setAge(int)
    //    - isActive() / setActive(boolean)
    //    - isVip() / setVip(boolean)  // 注意：setter是setVip，不是setIsVip
    //    - getTags() / setTags(List<String>)
    // 4. 不应该重复生成name的getter/setter
    // 5. 应该生成JSON格式的toString方法

}
