package com.zd.datapanel.scheduleds;

import com.zd.datapanel.entities.ShopSalesData;
import com.zd.datapanel.support.Constants;
import com.zd.datapanel.support.SalesCache;
import com.zd.datapanel.vo.SaleDataVO;
import com.zd.datapanel.vo.ShopDataVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wang xiao
 * @date Created in 11:03 2021/1/27
 */
@Component
public class DataTask {


    private final Logger logger = LoggerFactory.getLogger(DataTask.class);

    private SalesCache salesCache;


    private static SaleDataVO saleDataVO;

    @Scheduled(cron = "* 0/5 * * * ?")
    public void  sendSocketMsgTask () {
        String month = LocalDate.now().format(Constants.MONTH_FORMATTER);
        List<ShopSalesData> shopSalesDataList = salesCache.getNowValues();
        if (null == saleDataVO) {
            saleDataVO = new SaleDataVO();
        }
        String shopId = null;
        ShopDataVO shopDataVO = null;
        BigDecimal salePrice = BigDecimal.ZERO;
        Map<String, ShopDataVO> upData = new HashMap<>(3);
        for (ShopSalesData shopSalesData :shopSalesDataList){
            shopId = shopSalesData.getShopId();
            salePrice = shopSalesData.getOrderPrice();
            BigDecimal monthPrice = salesCache.getMonthPrice(shopId).add(salePrice);
            BigDecimal yearPrice = salesCache.getYearPrice(shopId).add(salePrice);
            shopDataVO = new ShopDataVO(shopId,monthPrice,salesCache.getTargetValue(month,shopId),yearPrice,salesCache.getTargetValue(null,shopId));
            upData.put(shopId,shopDataVO);
        }
        saleDataVO.setUpData(upData);
        saleDataVO.setDownDate(salesCache.getAllYearData(shopId));
    }


    @Autowired
    public void setSalesCache(SalesCache salesCache) {
        this.salesCache = salesCache;
    }


    public static SaleDataVO getSaleDataVO() {
        return saleDataVO;
    }
}
