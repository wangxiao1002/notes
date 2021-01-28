package com.zd.datapanel.Repositories;

import com.zd.datapanel.entities.ShopSalesData;
import com.zd.datapanel.support.PriceDTO;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;


/**
 * @author wang xiao
 * @date Created in 17:42 2021/1/26
 */
public interface ShopSalesDataRepository extends ReactiveCrudRepository<ShopSalesData,Long> {


    /**
     *  查询 时间内 数据
     * @author wangxiao
     * @date 18:27 2021/1/27
     * @param startDate startDate
     * @param endDate endDate
     * @return reactor.core.publisher.Flux<com.zd.datapanel.entities.ShopSalesData>
     */
    Flux<ShopSalesData> findByOrderDateBetween(String startDate,String endDate);

    /**
     *  查询指定日期数据
     * @author wangxiao
     * @date 11:17 2021/1/27
     * @param orderDate 日期
     * @return reactor.core.publisher.Flux<com.zd.datapanel.entities.ShopSalesData>
     */
    Flux<ShopSalesData> findAllByOrderDate(String orderDate);

    /**
     *  查询一年 销售
     * @author wangxiao
     * @date 11:59 2021/1/28
     * @param orderDate orderDate
     * @return reactor.core.publisher.Flux<com.zd.datapanel.entities.ShopSalesData>
     */
    Flux<ShopSalesData> findByOrderDateGreaterThanEqual(String orderDate);
    
    /**
     *  销售额统计
     * @author wangxiao
     * @date 17:08 2021/1/27 
     * @param orderDate orderDate
     * @return reactor.core.publisher.Flux<java.util.Map<java.lang.String,java.math.BigDecimal>>
     */
    @Query("SELECT s.shop_id AS 'code' ,SUM(s.order_price) AS 'price' FROM t_shop_sales s WHERE s.order_date >= :orderDate GROUP BY s.shop_id")
    Flux<PriceDTO> sumYearPriceGroupShopId(String orderDate);


    /**
     *  销售额统计
     * @author wangxiao
     * @date 17:08 2021/1/27
     * @param startDate startDate
     * @param endDate endDate
     * @return reactor.core.publisher.Flux<java.util.Map<java.lang.String,java.math.BigDecimal>>
     */
    @Query("SELECT s.shop_id AS 'code' ,SUM(s.order_price) AS 'price' FROM t_shop_sales s WHERE  s.order_date >= :startDate AND s.order_date <= :endDate GROUP BY s.shop_id")
    Flux<PriceDTO> sumMonthPriceGroupShopId(String startDate,String endDate);




}
