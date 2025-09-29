package com.glowxq.plugs.pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 字段顺序测试POJO类
 * 用于验证插件按字段声明顺序生成getter/setter方法
 * 包名包含pojo，应该被识别为JavaBean类型
 *
 * @author glowxq
 */
public class FieldOrderTestPOJO {

    // 字段按特定顺序声明，验证生成的方法是否保持相同顺序
    private String firstName;      // 第1个字段
    private String lastName;       // 第2个字段
    private int age;              // 第3个字段
    private Date birthDate;       // 第4个字段
    private String email;         // 第5个字段
    private String phoneNumber;   // 第6个字段
    private boolean active;       // 第7个字段
    private BigDecimal salary;    // 第8个字段
    private List<String> skills;  // 第9个字段

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "{" +
                "\"firstName\":\"" + firstName + "\"" + "," +
                "\"lastName\":\"" + lastName + "\"" + "," +
                "\"age\":" + age + "," +
                "\"birthDate\":" + birthDate + "," +
                "\"email\":\"" + email + "\"" + "," +
                "\"phoneNumber\":\"" + phoneNumber + "\"" + "," +
                "\"active\":" + active + "," +
                "\"salary\":" + salary + "," +
                "\"skills\":" + skills +
                "}";
    }
}
