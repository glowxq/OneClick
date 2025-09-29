package com.glowxq.plugs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户服务类 - 应该被识别为业务类
 * 测试业务类代码生成功能（如日志字段）
 * 包名包含service，应该被识别为业务类类型
 * 
 * 注意：这里使用注释模拟@Service注解，避免Spring依赖
 * 在实际项目中会有 @Service 注解
 */
// @Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    // 这里应该生成日志字段（LOGGER）
    // 使用 Ctrl+Alt+G 测试生成功能
    
    public void createUser(String username) {
        // 业务逻辑
        System.out.println("Creating user: " + username);
    }
    
    public void updateUser(Long id, String username) {
        // 业务逻辑
        System.out.println("Updating user " + id + " with username: " + username);
    }
    
    public void deleteUser(Long id) {
        // 业务逻辑
        System.out.println("Deleting user: " + id);
    }
}
