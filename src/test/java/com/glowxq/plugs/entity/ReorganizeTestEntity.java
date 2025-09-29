package com.glowxq.plugs.entity;

import java.util.List;

/**
 * 方法重新整理测试实体类
 * 用于验证插件能够重新整理现有的getter/setter/toString方法
 * 包名包含entity，应该被识别为JavaBean类型
 * @author glowxq
 */
public class ReorganizeTestEntity {

    private String name;
    private int age;
    private boolean active;

    // 业务逻辑方法1
    public void doSomething() {
        System.out.println("Business logic method 1");
    }

    // 业务逻辑方法2
    public void processData() {
        System.out.println("Business logic method 2");
    }

    // 业务逻辑方法3
    public boolean validateData() {
        return true;
    }

    // 业务逻辑方法4
    public void cleanup() {
        System.out.println("Business logic method 4");
    }

    public ReorganizeTestEntity() {
        this.active = true;
        this.age = 18;
        this.name = "John Doe";
    }

    public static void main(String[] args) {
        ReorganizeTestEntity entity = new ReorganizeTestEntity();
        System.out.println(entity);
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
    // 测试说明：
    // 1. 当前类的方法顺序混乱：业务逻辑方法和JavaBean方法混在一起
    // 2. 缺少age的getter、name的setter、active的getter/setter
    // 3. 使用 Cmd+Option+G 后，插件应该：
    //    a) 移除所有现有的getter/setter/toString方法
    //    b) 保留所有业务逻辑方法在原位置
    //    c) 在类的底部添加分割注释
    //    d) 按字段顺序重新生成所有getter/setter方法
    //    e) 最后生成新的JSON格式toString方法
    //
    // 预期的最终结构：
    // - 字段声明
    // - 业务逻辑方法（doSomething, processData, validateData, cleanup）
    // - getName() + setName(String)
    // - getAge() + setAge(int)
    // - isActive() + setActive(boolean)
    // - toString()

}
