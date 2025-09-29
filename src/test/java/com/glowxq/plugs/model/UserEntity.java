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

    // 这里应该生成JavaBean方法
    // 使用 Ctrl+Alt+G 测试生成功能
}
