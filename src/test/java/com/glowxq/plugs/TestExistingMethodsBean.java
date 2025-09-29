package com.glowxq.plugs;

/**
 * 测试用的已有部分JavaBean方法的类
 * 用于验证插件在已有方法时的重新整理功能
 *
 * @author glowxq
 */
public class TestExistingMethodsBean {

    private String productName;
    private double price;
    private int quantity;
    private boolean available;

    /**
     * 业务方法：计算总价
     */
    public double calculateTotal() {
        return price * quantity;
    }

    /**
     * 业务方法：检查库存
     */
    public boolean checkStock() {
        return available && quantity > 0;
    }

    // 现有的一些getter/setter方法（位置不正确，应该被重新整理）
    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    /**
     * 另一个业务方法：应用折扣
     */
    public double applyDiscount(double discountRate) {
        return calculateTotal() * (1 - discountRate);
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "Old toString method - should be replaced";
    }

    // 在这里使用插件生成getter/setter/toString方法
    // 预期结果：
    // 1. 现有的getter/setter/toString方法应该被移除
    // 2. 分割线注释应该出现在最后一个业务方法（applyDiscount）之后
    // 3. 然后重新生成所有的getter/setter/toString方法，按正确顺序排列
}
