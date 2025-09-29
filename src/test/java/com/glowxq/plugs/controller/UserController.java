package com.glowxq.plugs.controller;

import com.glowxq.plugs.model.TestFieldSortingBean;
import com.glowxq.plugs.model.TestInnerClassBean;
import com.glowxq.plugs.model.TestJavaBean;
import com.glowxq.plugs.model.UserEntity;
import com.glowxq.plugs.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户控制器类 - 应该被识别为业务类
 * 测试业务类代码生成功能（如日志字段）
 * 包名包含controller，应该被识别为业务类类型
 *
 * @author glowxq
 * @date 2025/09/29
 */
// @RestController
// @RequestMapping("/api/users")
public class UserController {

    private static final String USER = "User";

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    // 这里应该生成日志字段（LOGGER）
    // 使用 Ctrl+Alt+G 测试生成功能

    TestFieldSortingBean testFieldSortingBean;

    UserService userService;

    TestJavaBean testJavaBean;

    UserEntity userEntity;

    TestInnerClassBean testInnerClassBean;

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

    // ================================ JavaBean Methods ================================

    public TestFieldSortingBean getTestFieldSortingBean() {
        return testFieldSortingBean;
    }

    public void setTestFieldSortingBean(TestFieldSortingBean testFieldSortingBean) {
        this.testFieldSortingBean = testFieldSortingBean;
    }

    public TestInnerClassBean getTestInnerClassBean() {
        return testInnerClassBean;
    }

    public void setTestInnerClassBean(TestInnerClassBean testInnerClassBean) {
        this.testInnerClassBean = testInnerClassBean;
    }

    public TestJavaBean getTestJavaBean() {
        return testJavaBean;
    }

    public void setTestJavaBean(TestJavaBean testJavaBean) {
        this.testJavaBean = testJavaBean;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String toString() {
        return "{" +
                "\"testFieldSortingBean\":" + testFieldSortingBean + "," +
                "\"testInnerClassBean\":" + testInnerClassBean + "," +
                "\"testJavaBean\":" + testJavaBean + "," +
                "\"userEntity\":" + userEntity + "," +
                "\"userService\":" + userService +
                "}";
    }
}
