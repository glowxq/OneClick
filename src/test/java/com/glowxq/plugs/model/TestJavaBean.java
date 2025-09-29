package com.glowxq.plugs.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 测试用的JavaBean类
 * 用于验证插件功能
 * 包名包含model，应该被识别为JavaBean类型
 *
 * @author glowxq
 */
public class TestJavaBean implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(TestJavaBean.class);

    private String name;
    private int age;
    private boolean active;
    private Date createTime;
    private List<String> tags;

    // 业务逻辑方法
    public void doSomething() {
        log.info("Doing something with {}", name);
    }

    public boolean isValid() {
        return name != null && !name.trim().isEmpty();
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + name + "\"" + "," +
                "\"age\":" + age + "," +
                "\"active\":" + active + "," +
                "\"createTime\":\"" + createTime + "\"" + "," +
                "\"tags\":" + tags +
                "}";
    }
}
