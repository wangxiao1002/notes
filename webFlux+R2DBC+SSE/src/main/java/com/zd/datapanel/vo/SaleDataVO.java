package com.zd.datapanel.vo;

import com.zd.datapanel.entities.ShopSalesData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 销售数据 vo
 * @author wang xiao
 * @date Created in 16:47 2021/1/27
 */
public class SaleDataVO {


    Map<String, ShopDataVO> upData;


    List<ShopSalesData> downDate;

    public Map<String, ShopDataVO> getUpData() {
        return upData;
    }

    public void setUpData(Map<String, ShopDataVO> upData) {
        this.upData = upData;
    }

    public List<ShopSalesData> getDownDate() {
        return downDate;
    }

    public void setDownDate(List<ShopSalesData> downDate) {
        this.downDate = downDate;
    }
}
