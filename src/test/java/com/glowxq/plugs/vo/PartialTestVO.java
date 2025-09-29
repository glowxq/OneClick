package com.glowxq.plugs.vo;

import java.util.Date;

/**
 * 部分方法测试VO类
 * 用于验证插件在已有部分getter/setter方法时的行为
 * 包名包含vo，应该被识别为JavaBean类型
 *
 * @author glowxq
 */
public class PartialTestVO {

    private String username;
    private String email;
    private Date createTime;
    private boolean enabled;
    private int loginCount;

    // 已存在的getter方法
    public String getUsername() {
        return username;
    }

    // 已存在的setter方法
    public void setEmail(String email) {
        this.email = email;
    }

    // 已存在的boolean getter方法
    public boolean isEnabled() {
        return enabled;
    }

    // 在这里使用插件生成剩余的getter/setter/toString方法
    // 预期结果：
    // 1. 跳过已存在的getUsername()方法
    // 2. 生成缺失的setUsername()方法
    // 3. 生成缺失的getEmail()方法
    // 4. 跳过已存在的setEmail()方法
    // 5. 生成getCreateTime()和setCreateTime()方法
    // 6. 跳过已存在的isEnabled()方法
    // 7. 生成缺失的setEnabled()方法
    // 8. 生成getLoginCount()和setLoginCount()方法
    // 9. 替换或生成新的toString()方法
}
