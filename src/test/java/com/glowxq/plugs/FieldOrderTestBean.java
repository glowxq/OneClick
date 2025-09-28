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

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String buildFullName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean isVip) {
        this.isVip = isVip;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    @Override
    public String toString() {
        return "{" +
                "\"firstName\":\"" + firstName + "\"" + "," +
                "\"lastName\":\"" + lastName + "\"" + "," +
                "\"age\":" + age + "," +
                "\"birthDate\":" + birthDate + "," +
                "\"active\":" + active + "," +
                "\"isVip\":" + isVip + "," +
                "\"salary\":" + salary + "," +
                "\"hobbies\":" + hobbies +
                "}";
    }

    // 测试说明：
    // 1. 将光标放在类定义内
    // 2. 使用 Cmd+Option+G 生成所有getter/setter方法
    // 3. 预期的方法生成顺序（getter和setter紧挨着）：
    //
    // 按字段声明顺序，每个字段的getter和setter紧挨着：
    // - getFirstName() + setFirstName(String)
    // - getLastName() + setLastName(String)
    // - getAge() + setAge(int)
    // - getBirthDate() + setBirthDate(Date)
    // - isActive() + setActive(boolean)
    // - isVip() + setVip(boolean)  // 注意：setter是setVip，不是setIsVip
    // - getSalary() + setSalary(Double)
    // - getHobbies() + setHobbies(List<String>)
    //
    // toString方法：
    // - 最后生成JSON格式的toString方法

}// ================================ JavaBean Methods ================================
