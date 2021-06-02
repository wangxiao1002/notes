package com.zd.datapanel.scheduleds;

import com.zd.datapanel.entities.ShopSalesData;
import com.zd.datapanel.service.facade.ShopSalesDataService;
import com.zd.datapanel.support.SalesCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 10:24 2021/1/27
 */
@Component
public class CaCheTask {

    private final Logger logger = LoggerFactory.getLogger(CaCheTask.class);

    private SalesCache salesCache;

    private ShopSalesDataService shopSalesDataService;

    @Scheduled(cron = "0 30 0 * * ?")
    public void saveAndClearSalesCache () {
        logger.info("数据缓存处理定时任务开始执行----------------->start");
        List<ShopSalesData> salesDataList = salesCache.getSaveValues();
        if (!CollectionUtils.isEmpty(salesDataList)) {
            logger.info("数据缓存处理定时任务开始执行----------------->保存昨天数据");
            shopSalesDataService.savaBatch(salesDataList);
        }
        logger.info("数据缓存处理定时任务开始执行----------------->重置缓存");
        salesCache.resetCache();
        logger.info("数据缓存处理定时任务开始执行----------------->end");
    }



    @Autowired
    public void setSalesCache(SalesCache salesCache) {
        this.salesCache = salesCache;
    }

    @Autowired
    public void setShopSalesDataService(ShopSalesDataService shopSalesDataService) {
        this.shopSalesDataService = shopSalesDataService;
    }
}
