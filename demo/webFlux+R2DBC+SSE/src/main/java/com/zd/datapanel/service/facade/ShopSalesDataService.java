package com.zd.datapanel.service.facade;

import com.zd.datapanel.entities.ShopSalesData;
import com.zd.datapanel.support.PriceDTO;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 10:46 2021/1/27
 */
public interface ShopSalesDataService {
    
    /**
     *  保存数据
     * @author wangxiao
     * @date 10:48 2021/1/27 
     * @param entities 数据
     */
    void savaBatch(List<ShopSalesData> entities);


    /**
     *  查询日期全部数据
     * @author wangxiao
     * @date 11:14 2021/1/27
     * @param lastDay 日期
     * @return reactor.core.publisher.Flux<com.zd.datapanel.entities.ShopSalesData>
     */
    Flux<ShopSalesData> queryByDate(String lastDay);

    /**
     *  查询年度已经销售额
     * @author wangxiao
     * @date 17:18 2021/1/27
     * @param startDate 年度开始日期
     * @return reactor.core.publisher.Flux<PriceDTO>
     */
    Flux<PriceDTO> queryYearPrice (String startDate);


    /**
     *  查询当月已经销售额
     * @author wangxiao
     * @date 17:18 2021/1/27
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return reactor.core.publisher.Flux<PriceDTO>
     */
    Flux<PriceDTO> queryMonthPrice (String startDate,String endDate);


    /**
     *  查询 时间内 数据
     * @author wangxiao
     * @date 18:27 2021/1/27
     * @param startDate startDate
     * @param endDate endDate
     * @return reactor.core.publisher.Flux<com.zd.datapanel.entities.ShopSalesData>
     */
    Flux<ShopSalesData> queryByOrderDateBetween(String startDate,String endDate);


    /**
     *  查询全部
     * @author wangxiao
     * @date 11:56 2021/1/28
     * @param beginDate  beginDate
     * @return reactor.core.publisher.Flux<com.zd.datapanel.entities.ShopSalesData>
     */
    Flux<ShopSalesData> findAll (String beginDate);
}
