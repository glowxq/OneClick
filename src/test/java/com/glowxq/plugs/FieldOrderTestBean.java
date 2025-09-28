package com.glowxq.plugs;

import java.util.Date;
import java.util.List;

/**
 * 字段顺序测试类
 * 用于验证生成的getter/setter方法是否按照字段声明顺序排列
 * @author glowxq
 */
public class FieldOrderTestBean {

    // 字段按照特定顺序声明，生成的方法应该保持这个顺序
    private String firstName;      // 第1个字段
    private String lastName;       // 第2个字段
    private int age;              // 第3个字段
    private Date birthDate;       // 第4个字段
    private boolean active;       // 第5个字段
    private boolean isVip;        // 第6个字段
    private Double salary;        // 第7个字段
    private List<String> hobbies; // 第8个字段

    // 测试说明：
    // 1. 将光标放在类定义内
    // 2. 使用 Cmd+Option+G 生成所有getter/setter方法
    // 3. 预期的方法生成顺序：
    //
    // Getter方法（按字段声明顺序）：
    // - getFirstName()
    // - getLastName()
    // - getAge()
    // - getBirthDate()
    // - isActive()
    // - isVip()
    // - getSalary()
    // - getHobbies()
    //
    // Setter方法（按字段声明顺序）：
    // - setFirstName(String)
    // - setLastName(String)
    // - setAge(int)
    // - setBirthDate(Date)
    // - setActive(boolean)
    // - setVip(boolean)  // 注意：不是setIsVip
    // - setSalary(Double)
    // - setHobbies(List<String>)
    //
    // toString方法：
    // - 最后生成JSON格式的toString方法

}
