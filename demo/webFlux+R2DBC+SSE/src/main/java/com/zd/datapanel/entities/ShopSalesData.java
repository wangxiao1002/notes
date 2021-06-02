package com.zd.datapanel.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

/**
 * 店铺销售数据
 * @author wang xiao
 * @date Created in 17:30 2021/1/26
 */
@Table("t_shop_sales")
public class ShopSalesData {

    @Id
    private Long id;

    /**
     * 商店 id 暂定 10001 京东pop(旗舰) 10002 京东自营店 20001 天猫旗舰店
     */
    private String shopId;

    /**
     * 订单数
     */
    private Long orderNum;

    /**
     * 交易金额
     */
    private BigDecimal orderPrice;

    /**
     * 用户访问数
     */
    private Long uvNum;

    /**
     * 日期
     */
    private String orderDate;

    public ShopSalesData() {
    }

    public ShopSalesData(String shopId, String orderDate) {
        this.shopId = shopId;
        this.orderDate = orderDate;
        this.orderNum = 0L;
        this.orderPrice = BigDecimal.ZERO;
        this.uvNum = 0L;
    }

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

    public Long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Long getUvNum() {
        return uvNum;
    }

    public void setUvNum(Long uvNum) {
        this.uvNum = uvNum;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }



}
