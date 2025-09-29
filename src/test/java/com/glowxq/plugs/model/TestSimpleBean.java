package com.glowxq.plugs.model;

import java.util.Date;

/**
 * 测试用的简单JavaBean类（无业务方法）
 * 用于验证插件在没有业务方法时的正确插入位置
 * 包名包含model，应该被识别为JavaBean类型
 *
 * @author glowxq
 */
public class TestSimpleBean {

    private String username;
    private String email;
    private Date lastLoginTime;
    private boolean enabled;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "{" +
                "\"username\":\"" + username + "\"" + "," +
                "\"email\":\"" + email + "\"" + "," +
                "\"lastLoginTime\":" + lastLoginTime + "," +
                "\"enabled\":" + enabled +
                "}";
    }

    // 在这里使用插件生成getter/setter/toString方法
    // 预期结果：
    // 1. 不应该有分割线注释（因为没有业务方法）
    // 2. 直接生成getter方法
    // 3. 然后是setter方法
    // 4. 最后是toString方法
}
