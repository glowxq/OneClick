package com.glowxq.plugs.model;

/**
 * 测试内部类的JavaBean
 * 用于验证内部类的getter/setter/toString生成功能
 * 
 * @author glowxq
 */
public class TestInnerClassBean {
    
    private String outerName;
    private int outerAge;
    
    public void businessMethod() {
        System.out.println("This is a business method in outer class");
    }
    
    /**
     * 内部类 - 用户信息
     */
    public static class UserInfo {
        private String username;
        private String email;
        private boolean active;
        
        public void validateUser() {
            System.out.println("Validating user: " + username);
        }
        
        /**
         * 嵌套内部类 - 用户地址
         */
        public static class Address {
            private String street;
            private String city;
            private String zipCode;
            
            public void validateAddress() {
                System.out.println("Validating address");
            }
        }
        
        /**
         * 嵌套内部类 - 用户偏好设置
         */
        public static class Preferences {
            private String language;
            private String theme;
            private boolean notifications;
        }
    }
    
    /**
     * 内部类 - 订单信息
     */
    public class OrderInfo {
        private String orderId;
        private double amount;
        private String status;
        
        public void processOrder() {
            System.out.println("Processing order: " + orderId);
        }
        
        /**
         * 嵌套内部类 - 订单项
         */
        public class OrderItem {
            private String productId;
            private int quantity;
            private double price;
        }
    }
    
    /**
     * 内部类 - 配置信息
     */
    public static class ConfigInfo {
        private String configKey;
        private String configValue;
        private boolean enabled;
        
        public void applyConfig() {
            System.out.println("Applying config: " + configKey);
        }
    }
}
