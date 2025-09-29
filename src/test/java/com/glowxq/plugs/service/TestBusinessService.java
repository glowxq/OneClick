package com.glowxq.plugs.service;

import java.util.List;
import java.util.Date;

/**
 * 测试用的业务服务类
 * 用于验证插件对业务类的日志字段生成
 * 包名包含service，应该被识别为业务类类型
 *
 * @author glowxq
 */
public class TestBusinessService {

    // 这里应该生成日志字段（LOGGER）
    
    /**
     * 业务方法1：验证用户
     */
    public boolean validateUser(String name, int age) {
        return name != null && !name.trim().isEmpty() && age > 0;
    }

    /**
     * 业务方法2：计算用户积分
     */
    public int calculatePoints(String username) {
        // 模拟积分计算逻辑
        return username.length() * 10;
    }

    /**
     * 业务方法3：发送通知
     */
    public void sendNotification(String message) {
        System.out.println("Sending notification: " + message);
    }

    /**
     * 业务方法4：处理数据
     */
    public void processData(List<String> data) {
        if (data != null && !data.isEmpty()) {
            System.out.println("Processing " + data.size() + " items");
        }
    }

    /**
     * 业务方法5：清理资源
     */
    public void cleanup() {
        System.out.println("Cleaning up resources");
    }
}
