package com.glowxq.plugs;

import java.util.Date;

/**
 * 测试插入顺序的类
 * 用于验证分割注释和方法的插入位置
 */
public class TestInsertionOrder {

    private String name;
    private int age;
    private boolean active;

    /**
     * 业务方法1
     */
    public void businessMethod1() {
        System.out.println("Business method 1");
    }

    /**
     * 业务方法2
     */
    public String businessMethod2() {
        return "Business method 2";
    }

    /**
     * 业务方法3 - 这应该是最后一个业务方法
     */
    public boolean businessMethod3() {
        return true;
    }

    // ================================ JavaBean Methods ================================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + name + "\"" + "," +
                "\"age\":" + age + "," +
                "\"active\":" + active +
                "}";
    }
}
