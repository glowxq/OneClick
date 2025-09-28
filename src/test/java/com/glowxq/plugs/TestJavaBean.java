package com.glowxq.plugs;

import java.util.Date;
import java.util.List;

/**
 * 测试用的JavaBean类
 * 用于验证插件功能
 * @author glowxq
 */
public class TestJavaBean {

    private String name;
    private int age;
    private boolean active;
    private Date createTime;
    private Double salary;
    private List<String> hobbies;
    private boolean isVip;

    // 这里可以测试插件功能：
    // 1. 将光标放在类定义内的任意位置
    // 2. 使用 Ctrl+Alt+G 生成getter/setter/toString方法
    // 3. 使用 Ctrl+Alt+F 折叠所有生成的方法
    // 4. 再次使用 Ctrl+Alt+G 验证不会重复生成已存在的方法
    // 5. 修改字段后再次生成，验证toString方法会被重新生成

}
