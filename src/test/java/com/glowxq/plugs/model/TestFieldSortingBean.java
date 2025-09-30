package com.glowxq.plugs.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 测试字段排序功能的JavaBean类
 * 包含不同长度和类型的字段，用于验证排序功能
 * 
 * @author glowxq
 */
public class TestFieldSortingBean {

    // 不同长度的字段名，用于测试按名称长度排序
    private String a;                    // 长度1
    private String bb;                   // 长度2
    private String ccc;                  // 长度3
    private String dddd;                 // 长度4
    private String eeeee;                // 长度5
    
    // 不同类型的字段，用于测试按类型排序
    private Boolean flag;                // boolean类型
    private int count;                   // int类型
    private long timestamp;              // long类型
    private String name;                 // String类型
    private LocalDateTime createTime;    // LocalDateTime类型
    private List<String> items;          // List类型
    
    // 按字母顺序的字段，用于测试按名称排序
    private String alpha;
    private String beta;
    private String gamma;
    private String delta;
    private String epsilon;

    public boolean isFlag() {
        return flag;
    }

    // ================================ JavaBean Methods ================================

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getBb() {
        return bb;
    }

    public void setBb(String bb) {
        this.bb = bb;
    }

    public String getCcc() {
        return ccc;
    }

    public void setCcc(String ccc) {
        this.ccc = ccc;
    }

    public String getDddd() {
        return dddd;
    }

    public void setDddd(String dddd) {
        this.dddd = dddd;
    }

    public String getEeeee() {
        return eeeee;
    }

    public void setEeeee(String eeeee) {
        this.eeeee = eeeee;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public String getAlpha() {
        return alpha;
    }

    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }

    public String getBeta() {
        return beta;
    }

    public void setBeta(String beta) {
        this.beta = beta;
    }

    public String getGamma() {
        return gamma;
    }

    public void setGamma(String gamma) {
        this.gamma = gamma;
    }

    public String getDelta() {
        return delta;
    }

    public void setDelta(String delta) {
        this.delta = delta;
    }

    public String getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(String epsilon) {
        this.epsilon = epsilon;
    }

    @Override
    public String toString() {
        return "{" +
                "\"a\":\"" + a + "\"" + "," +
                "\"bb\":\"" + bb + "\"" + "," +
                "\"ccc\":\"" + ccc + "\"" + "," +
                "\"dddd\":\"" + dddd + "\"" + "," +
                "\"eeeee\":\"" + eeeee + "\"" + "," +
                "\"flag\":" + flag + "," +
                "\"count\":" + count + "," +
                "\"timestamp\":" + timestamp + "," +
                "\"name\":\"" + name + "\"" + "," +
                "\"createTime\":" + createTime + "," +
                "\"items\":" + items + "," +
                "\"alpha\":\"" + alpha + "\"" + "," +
                "\"beta\":\"" + beta + "\"" + "," +
                "\"gamma\":\"" + gamma + "\"" + "," +
                "\"delta\":\"" + delta + "\"" + "," +
                "\"epsilon\":\"" + epsilon + "\"" +
                "}";
    }
}
