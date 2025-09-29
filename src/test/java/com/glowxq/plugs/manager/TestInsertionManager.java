package com.glowxq.plugs.manager;

/**
 * 插入顺序测试管理器类
 * 用于验证插件对业务类的代码生成和插入位置
 * 包名包含manager，应该被识别为业务类类型
 *
 * @author glowxq
 */
public class TestInsertionManager {

    // 这里应该生成日志字段（LOGGER）

    /**
     * 业务方法1：初始化系统
     */
    public void initializeSystem() {
        System.out.println("Initializing system...");
    }

    /**
     * 业务方法2：处理请求
     */
    public String processRequest(String request) {
        return "Processed: " + request;
    }

    /**
     * 业务方法3：验证权限
     */
    public boolean checkPermission(String user, String action) {
        return user != null && action != null;
    }

    /**
     * 业务方法4：记录日志
     */
    public void logActivity(String activity) {
        System.out.println("Activity logged: " + activity);
    }

    /**
     * 业务方法5：关闭系统
     */
    public void shutdown() {
        System.out.println("System shutting down...");
    }

    // 测试说明：
    // 1. 这是一个业务类（包名包含manager）
    // 2. 使用 Cmd+Option+G 后，插件应该：
    //    a) 在类的第一行生成日志字段
    //    b) 不生成JavaBean方法（因为这是业务类）
    //    c) 保持所有业务方法不变
    //
    // 预期的最终结构：
    // - 日志字段（LOGGER）
    // - 所有业务方法保持原位置
}
