package com.glowxq.plugs;

import java.util.List;
import java.util.Date;

/**
 * 测试用的包含业务方法的JavaBean类
 * 用于验证插件在有业务方法时的正确插入位置
 *
 * @author glowxq
 */
public class TestBusinessBean {

    private String name;
    private int age;
    private boolean active;
    private Date createTime;
    private List<String> hobbies;

    /**
     * 业务方法1：验证用户
     */
    public boolean validateUser() {
        return name != null && !name.trim().isEmpty() && age > 0;
    }

    /**
     * 业务方法2：计算用户积分
     */
    public int calculatePoints() {
        int points = age * 10;
        if (active) {
            points += 100;
        }
        if (hobbies != null && !hobbies.isEmpty()) {
            points += hobbies.size() * 5;
        }
        return points;
    }

    /**
     * 业务方法3：格式化用户信息
     */
    public String formatUserInfo() {
        return String.format("User: %s (Age: %d, Active: %s)", 
                           name != null ? name : "Unknown", 
                           age, 
                           active ? "Yes" : "No");
    }

    /**
     * 业务方法4：检查是否为VIP用户
     */
    public boolean isVipUser() {
        return age >= 18 && active && calculatePoints() > 200;
    }

    // 在这里使用插件生成getter/setter/toString方法
    // 预期结果：
    // 1. 分割线注释应该出现在这里（在业务方法之后）
    // 2. 然后是所有的getter方法
    // 3. 然后是所有的setter方法  
    // 4. 最后是toString方法
}
