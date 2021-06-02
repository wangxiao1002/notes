package com.zd.datapanel.vo;

import java.math.BigDecimal;

/**
 * @author wang xiao
 * @date Created in 16:51 2021/1/27
 */
public class ShopDataVO {

    private String label;

    private BigDecimal monthPrice;

    private BigDecimal monthTargetPrice;

    private BigDecimal yearPrice;

    private BigDecimal yearTargetPrice;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BigDecimal getMonthPrice() {
        return monthPrice;
    }

    public void setMonthPrice(BigDecimal monthPrice) {
        this.monthPrice = monthPrice;
    }

    public BigDecimal getMonthTargetPrice() {
        return monthTargetPrice;
    }

    public void setMonthTargetPrice(BigDecimal monthTargetPrice) {
        this.monthTargetPrice = monthTargetPrice;
    }

    public BigDecimal getYearPrice() {
        return yearPrice;
    }

    public void setYearPrice(BigDecimal yearPrice) {
        this.yearPrice = yearPrice;
    }

    public BigDecimal getYearTargetPrice() {
        return yearTargetPrice;
    }

    public void setYearTargetPrice(BigDecimal yearTargetPrice) {
        this.yearTargetPrice = yearTargetPrice;
    }

    public ShopDataVO(String label, BigDecimal monthPrice, BigDecimal monthTargetPrice, BigDecimal yearPrice, BigDecimal yearTargetPrice) {
        this.label = label;
        this.monthPrice = monthPrice;
        this.monthTargetPrice = monthTargetPrice;
        this.yearPrice = yearPrice;
        this.yearTargetPrice = yearTargetPrice;
    }

    public ShopDataVO(String label, BigDecimal monthPrice) {
        this.label = label;
        this.monthPrice = monthPrice;
    }

    @Override
    public String toString() {
        return "ShopDataVO{" +
                "label='" + label + '\'' +
                ", monthPrice=" + monthPrice +
                ", monthTargetPrice=" + monthTargetPrice +
                ", yearPrice=" + yearPrice +
                ", yearTargetPrice=" + yearTargetPrice +
                '}';
    }
}
