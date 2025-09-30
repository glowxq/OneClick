package com.glowxq.plugs.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类 - 应该被识别为JavaBean
 * 测试JavaBean方法生成功能
 * 包名包含model，应该被识别为JavaBean类型
 */
public class UserEntity implements Serializable {

    private Long id;
    private String username;
    private String email;
    private Date createTime;
    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
                "\"id\":" + id + "," +
                "\"username\":\"" + username + "\"" + "," +
                "\"email\":\"" + email + "\"" + "," +
                "\"createTime\":" + createTime + "," +
                "\"active\":" + active +
                "}";
    }

    // 这里应该生成JavaBean方法
    // 使用 Ctrl+Alt+G 测试生成功能
}
