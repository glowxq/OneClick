package com.glowxq.plugs.model.dto;

import java.io.Serializable;
import java.io.Serial;
import com.glowxq.plugs.model.TestFieldSortingBean;
import java.lang.String;
import java.lang.Boolean;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TestFieldSortingBean DTO 类
 * 自动生成的数据传输对象
 * 
 * @author OneClick Plugin
 * @date 2025/09/30
 */
public class TestFieldSortingBeanDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String a;
    private String bb;
    private String ccc;
    private String dddd;
    private String eeeee;
    private Boolean flag;
    private int count;
    private long timestamp;
    private String name;
    private LocalDateTime createTime;
    private List<String> items;
    private String alpha;
    private String beta;
    private String gamma;
    private String delta;
    private String epsilon;

    public TestFieldSortingBeanDTO() {
    }

    /**
     * 转换为实体类
     */
    public TestFieldSortingBean toEntity() {
        TestFieldSortingBean entity = new TestFieldSortingBean();
        entity.setA(this.getA());
        entity.setBb(this.getBb());
        entity.setCcc(this.getCcc());
        entity.setDddd(this.getDddd());
        entity.setEeeee(this.getEeeee());
        entity.setFlag(this.getFlag());
        entity.setCount(this.getCount());
        entity.setTimestamp(this.getTimestamp());
        entity.setName(this.getName());
        entity.setCreateTime(this.getCreateTime());
        entity.setItems(this.getItems());
        entity.setAlpha(this.getAlpha());
        entity.setBeta(this.getBeta());
        entity.setGamma(this.getGamma());
        entity.setDelta(this.getDelta());
        entity.setEpsilon(this.getEpsilon());
        return entity;
    }

    /**
     * 从实体类转换
     */
    public static TestFieldSortingBeanDTO fromEntity(TestFieldSortingBean entity) {
        if (entity == null) {
            return null;
        }
        TestFieldSortingBeanDTO dto = new TestFieldSortingBeanDTO();
        dto.setA(entity.getA());
        dto.setBb(entity.getBb());
        dto.setCcc(entity.getCcc());
        dto.setDddd(entity.getDddd());
        dto.setEeeee(entity.getEeeee());
        dto.setFlag(entity.getFlag());
        dto.setCount(entity.getCount());
        dto.setTimestamp(entity.getTimestamp());
        dto.setName(entity.getName());
        dto.setCreateTime(entity.getCreateTime());
        dto.setItems(entity.getItems());
        dto.setAlpha(entity.getAlpha());
        dto.setBeta(entity.getBeta());
        dto.setGamma(entity.getGamma());
        dto.setDelta(entity.getDelta());
        dto.setEpsilon(entity.getEpsilon());
        return dto;
    }

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
                "\"a\":" + "\"" + a + "\"" +
                ", " +
                "\"bb\":" + "\"" + bb + "\"" +
                ", " +
                "\"ccc\":" + "\"" + ccc + "\"" +
                ", " +
                "\"dddd\":" + "\"" + dddd + "\"" +
                ", " +
                "\"eeeee\":" + "\"" + eeeee + "\"" +
                ", " +
                "\"flag\":" + flag +
                ", " +
                "\"count\":" + count +
                ", " +
                "\"timestamp\":" + timestamp +
                ", " +
                "\"name\":" + "\"" + name + "\"" +
                ", " +
                "\"createTime\":" + createTime +
                ", " +
                "\"items\":" + items +
                ", " +
                "\"alpha\":" + "\"" + alpha + "\"" +
                ", " +
                "\"beta\":" + "\"" + beta + "\"" +
                ", " +
                "\"gamma\":" + "\"" + gamma + "\"" +
                ", " +
                "\"delta\":" + "\"" + delta + "\"" +
                ", " +
                "\"epsilon\":" + "\"" + epsilon + "\"" +
                "}";
    }
}
