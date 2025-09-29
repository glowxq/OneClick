package com.glowxq.plugs.controller;

/**
 * 用户控制器类 - 应该被识别为业务类
 * 测试业务类代码生成功能（如日志字段）
 * 包名包含controller，应该被识别为业务类类型
 */
// @RestController
// @RequestMapping("/api/users")
public class UserController {

    // 这里应该生成日志字段（LOGGER）
    // 使用 Ctrl+Alt+G 测试生成功能
    
    // @Autowired
    // private UserService userService;
    
    // @GetMapping("/{id}")
    public String getUser(Long id) {
        // 业务逻辑
        return "User: " + id;
    }
    
    // @PostMapping
    public String createUser(String username) {
        // 业务逻辑
        return "Created user: " + username;
    }
    
    // @PutMapping("/{id}")
    public String updateUser(Long id, String username) {
        // 业务逻辑
        return "Updated user " + id + " with username: " + username;
    }
    
    // @DeleteMapping("/{id}")
    public String deleteUser(Long id) {
        // 业务逻辑
        return "Deleted user: " + id;
    }
}
