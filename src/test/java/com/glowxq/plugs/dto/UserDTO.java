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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
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
                "\"username\":\"" + username + "\"" + "," +
                "\"email\":\"" + email + "\"" + "," +
                "\"displayName\":\"" + displayName + "\"" + "," +
                "\"lastLoginTime\":" + lastLoginTime + "," +
                "\"enabled\":" + enabled +
                "}";
    }

    public class Address {

        private String street;
        private String city;
        private String state;
        private String zipCode;

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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
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
                    "\"state\":\"" + state + "\"" + "," +
                    "\"zipCode\":\"" + zipCode + "\"" +
                    "}";
        }

        // 这里应该生成JavaBean方法
        // 使用 Ctrl+Alt+G 测试生成功能
    }


    // 这里应该生成JavaBean方法
    // 使用 Ctrl+Alt+G 测试生成功能
    
    // 测试fluent setter功能
    // 在设置中开启fluent setter后，应该生成链式调用的setter方法
}
