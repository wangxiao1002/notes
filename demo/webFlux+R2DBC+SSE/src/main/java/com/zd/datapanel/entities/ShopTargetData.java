package com.zd.datapanel.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

/**
 * 商店年度计划值
 * @author wang xiao
 * @date Created in 16:30 2021/1/26
 */
@Table("t_shop_target")
public class ShopTargetData {

    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 商店 id 暂定 10001 京东pop(旗舰) 10002 京东自营店 20001 天猫旗舰店
     */
    private String shopId;

    /**
     * label
     */
    private String label;

    /**
     * 目标月份 格式yyyyMM
     */
    private String targetMonth;

    /**
     * 目标金额 小数点2位 单位元
     */
    private BigDecimal targetValue;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTargetMonth() {
        return targetMonth;
    }

    public void setTargetMonth(String targetMonth) {
        this.targetMonth = targetMonth;
    }

    public BigDecimal getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(BigDecimal targetValue) {
        this.targetValue = targetValue;
    }

    @Override
    public String toString() {
        return "ShopTargetData{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", label='" + label + '\'' +
                ", targetMonth='" + targetMonth + '\'' +
                ", targetValue=" + targetValue +
                '}';
    }
}
