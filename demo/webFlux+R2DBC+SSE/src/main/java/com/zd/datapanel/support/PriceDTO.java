package com.zd.datapanel.support;

import java.math.BigDecimal;

/**
 * @author wang xiao
 * @date Created in 17:54 2021/1/27
 */
public class PriceDTO {

    private String code;

    private BigDecimal price;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
