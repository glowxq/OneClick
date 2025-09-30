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

    // ================================ JavaBean Methods ================================

    public String getOuterName() {
        return outerName;
    }

    public void setOuterName(String outerName) {
        this.outerName = outerName;
    }

    public int getOuterAge() {
        return outerAge;
    }

    public void setOuterAge(int outerAge) {
        this.outerAge = outerAge;
    }

    @Override
    public String toString() {
        return "{" +
                "\"outerName\":\"" + outerName + "\"" + "," +
                "\"outerAge\":" + outerAge +
                "}";
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

        // Inner Class UserInfo Methods

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

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        @Override
        public String toString() {
            return "{" +
                    "\"username\":\"" + username + "\"" + "," +
                    "\"email\":\"" + email + "\"" + "," +
                    "\"active\":" + active +
                    "}";
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

            // Inner Class Address Methods

            public String getStreet() {
                return street;
            }

            public void setStreet(String street) {
                this.street = street;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getZipCode() {
                return zipCode;
            }

            public void setZipCode(String zipCode) {
                this.zipCode = zipCode;
            }

            @Override
            public String toString() {
                return "{" +
                        "\"street\":\"" + street + "\"" + "," +
                        "\"city\":\"" + city + "\"" + "," +
                        "\"zipCode\":\"" + zipCode + "\"" +
                        "}";
            }
        }
        
        /**
         * 嵌套内部类 - 用户偏好设置
         */
        public static class Preferences {
            private String language;
            private String theme;
            private boolean notifications;

            // Inner Class Preferences Methods

            public String getLanguage() {
                return language;
            }

            public void setLanguage(String language) {
                this.language = language;
            }

            public String getTheme() {
                return theme;
            }

            public void setTheme(String theme) {
                this.theme = theme;
            }

            public boolean isNotifications() {
                return notifications;
            }

            public void setNotifications(boolean notifications) {
                this.notifications = notifications;
            }

            @Override
            public String toString() {
                return "{" +
                        "\"language\":\"" + language + "\"" + "," +
                        "\"theme\":\"" + theme + "\"" + "," +
                        "\"notifications\":" + notifications +
                        "}";
            }
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

        // Inner Class OrderInfo Methods

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "{" +
                    "\"orderId\":\"" + orderId + "\"" + "," +
                    "\"amount\":" + amount + "," +
                    "\"status\":\"" + status + "\"" +
                    "}";
        }

        /**
         * 嵌套内部类 - 订单项
         */
        public class OrderItem {
            private String productId;
            private int quantity;
            private double price;

            // Inner Class OrderItem Methods

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            @Override
            public String toString() {
                return "{" +
                        "\"productId\":\"" + productId + "\"" + "," +
                        "\"quantity\":" + quantity + "," +
                        "\"price\":" + price +
                        "}";
            }
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

        // Inner Class ConfigInfo Methods

        public String getConfigKey() {
            return configKey;
        }

        public void setConfigKey(String configKey) {
            this.configKey = configKey;
        }

        public String getConfigValue() {
            return configValue;
        }

        public void setConfigValue(String configValue) {
            this.configValue = configValue;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public String toString() {
            return "{" +
                    "\"configKey\":\"" + configKey + "\"" + "," +
                    "\"configValue\":\"" + configValue + "\"" + "," +
                    "\"enabled\":" + enabled +
                    "}";
        }
    }
}
