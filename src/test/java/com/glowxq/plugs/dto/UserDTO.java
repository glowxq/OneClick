package com.glowxq.plugs.dto;

import java.util.Date;

/**
 * 用户数据传输对象 - 应该被识别为JavaBean
 * 测试JavaBean方法生成功能
 * 包名包含dto，应该被识别为JavaBean类型
 */
public class UserDTO {

    private String username;
    private String email;
    private String displayName;
    private Date lastLoginTime;
    private boolean enabled;


    // 这里应该生成JavaBean方法
    // 使用 Ctrl+Alt+G 测试生成功能
    
    // 测试fluent setter功能
    // 在设置中开启fluent setter后，应该生成链式调用的setter方法
}
