package com.zd.datapanel.support;

import com.zd.datapanel.entities.ShopSalesData;
import com.zd.datapanel.entities.ShopTargetData;
import com.zd.datapanel.service.facade.ShopSalesDataService;
import com.zd.datapanel.service.facade.ShopTargetDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 销售数据缓存  因为数据补多 所以内存直接缓存  不需要额外介入中间件
 * @author wang xiao
 * @date Created in 18:04 2021/1/26
 */
@Component
public class SalesCache implements InitializingBean, DisposableBean {


    private final Logger logger = LoggerFactory.getLogger(SalesCache.class);

    private static final String FORMATTER = "%s-%s";



    private ShopSalesDataService shopSalesDataService;

    private ShopTargetDataService shopTargetDataService;

    /**
     * 昨日+今日销售数据
     */
    private static final ConcurrentHashMap<String, ShopSalesData> SHOP_SALES_DATA_CACHE = new ConcurrentHashMap<>();

    /**
     * 目标值
     */
    private static final List<ShopTargetData> TARGET_DATA_CACHE = new ArrayList<>(36);

    /**
     * 最新年度销售额
     */
    public static final Map<String,BigDecimal> YEAR_PRICE_CACHE = new ConcurrentHashMap<>(3);


    /**
     * 最新月度销售额
     */
    public static final Map<String,BigDecimal> MONTH_PRICE_CACHE = new ConcurrentHashMap<>(3);

    /**
     * 年度所欲数据 按道理来说应该会有 3*365 个数
     */
    public static final Map<String,List<ShopSalesData>>  ALL_SALES_CACHE = new ConcurrentHashMap<>(3);

    /**
     *  重置缓存  主要市重置昨天数据 今日数据 会在接受中覆盖
     * @author wangxiao
     * @date 18:11 2021/1/26
     */
    public void resetCache () {
        String now = LocalDate.now().plusDays(-2).format(Constants.DAY_FORMATTER);
        Enumeration<String> keys =  SHOP_SALES_DATA_CACHE.keys();
        String key = null;
        while (keys.hasMoreElements()){
            key = keys.nextElement();
            if (key.startsWith(now)){
                SHOP_SALES_DATA_CACHE.remove(key);
            }
        }
        initYearAndMonthData();
    }

    /**
     *  添加今日缓存
     * @author wangxiao
     * @date 18:31 2021/1/26
     * @param date 日期
     * @param shopId  商店id
     * @param shopSalesData 数据
     */
    public void addCache(String date,String shopId,ShopSalesData shopSalesData) {
        String key = initKey(date,shopId);
        logger.info("添加进缓存，key is :{}",key);
        SHOP_SALES_DATA_CACHE.put(key,shopSalesData);
    }

    /**
     *  initKey
     * @author wangxiao
     * @date 18:33 2021/1/26
     * @param dateStr dateStr
     * @param shopId shopId
     * @return java.lang.String
     */
    private String initKey(String dateStr,String shopId){
        return String.format(FORMATTER,dateStr,shopId);
    }


    /**
     *  获取需要保存的value
     * @author wangxiao
     * @date 10:42 2021/1/27
     * @return java.util.List<com.zd.datapanel.entities.ShopSalesData>
     */
    public List<ShopSalesData>  getSaveValues () {
        logger.info("获取需要保存的数据");
        String now = LocalDate.now().plusDays(-1).format(Constants.DAY_FORMATTER);
        return getValues(now);
    }

    /**
     *  获取今天所有的值
     * @author wangxiao
     * @date 14:37 2021/1/27
     * @return java.util.List<com.zd.datapanel.entities.ShopSalesData>
     */
    public List<ShopSalesData> getNowValues () {
        String now = LocalDate.now().format(Constants.DAY_FORMATTER);
        return getValues(now);
    }

    /**
     *  获取指定 时间前缀的value
     * @author wangxiao
     * @date 17:27 2021/1/27
     * @param date date
     * @return java.util.List<com.zd.datapanel.entities.ShopSalesData>
     */
    private List<ShopSalesData> getValues (String date) {
        List<ShopSalesData> shopSalesDataList = new ArrayList<>(3);
        Set<Map.Entry<String,ShopSalesData>> entrySet = SHOP_SALES_DATA_CACHE.entrySet();
        String key = null;
        ShopSalesData shopSalesData = null;
        for (Map.Entry<String,ShopSalesData> entry : entrySet) {
            key = entry.getKey();
            shopSalesData = entry.getValue();
            if (key.startsWith(date)){
                shopSalesDataList.add(shopSalesData);
            }
        }
        return shopSalesDataList;
    }


    /**
     *  获取商店 全年数据
     * @author wangxiao
     * @date 17:27 2021/1/27
     * @param shopId shopId
     * @return java.util.List<com.zd.datapanel.entities.ShopSalesData>
     */
    public List<ShopSalesData> getAllYearData (String shopId) {
        return ALL_SALES_CACHE.get(shopId);
    }


    /**
     *  获取年度值
     * @author wangxiao
     * @date 11:44 2021/1/27
     * @param monthDate 年度值
     * @param shopId 店铺
     * @return java.math.BigDecimal
     */
    public BigDecimal getTargetValue (String monthDate,String shopId) {
        if (null != monthDate) {
            return TARGET_DATA_CACHE.stream()
                    .filter(e->Objects.equals(e.getTargetMonth(),monthDate) && Objects.equals(e.getShopId(),shopId))
                    .findFirst()
                    .map(ShopTargetData::getTargetValue)
                    .orElse(BigDecimal.ZERO);
        }else {
            return TARGET_DATA_CACHE.stream()
                    .filter(e->Objects.equals(e.getShopId(),shopId))
                    .map(ShopTargetData::getTargetValue)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
        }
    }

    /**
     *  年度销售额值
     * @author wangxiao
     * @date 17:43 2021/1/27
     * @param shopId 商店id
     * @return java.math.BigDecimal
     */
    public BigDecimal getYearPrice (String shopId){
        return YEAR_PRICE_CACHE.get(shopId);
    }


    /**
     *  月度销售额值
     * @author wangxiao
     * @date 17:43 2021/1/27
     * @param shopId 商店id
     * @return java.math.BigDecimal
     */
    public BigDecimal getMonthPrice (String shopId){
        return MONTH_PRICE_CACHE.get(shopId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("销售数据缓存初始化---------->start");
        synchronized (SHOP_SALES_DATA_CACHE){
            SHOP_SALES_DATA_CACHE.clear();
            String lastDay = LocalDate.now().plusDays(-1).format(Constants.DAY_FORMATTER);
            logger.info("查询昨天数据！！！ 初始化销售数据,参数:{}",lastDay);
            shopSalesDataService.queryByDate(lastDay).subscribe(e -> {
                addCache(e.getOrderDate(),e.getShopId(),e);
            });
        }
        synchronized (TARGET_DATA_CACHE){
            logger.info("查询年度目标值！！！ 初始化年度目标数据");
            shopTargetDataService.queryNowYear().subscribe(TARGET_DATA_CACHE::add);
        }
        initYearAndMonthData();
        logger.info("销售数据缓存初始化---------->end!!");
    }

    /**
     *  初始化 年度和月度销售数据
     * @author wangxiao
     * @date 17:32 2021/1/27
     */
    private void  initYearAndMonthData () {
        YEAR_PRICE_CACHE.clear();
        MONTH_PRICE_CACHE.clear();
        ALL_SALES_CACHE.clear();
        String orderDate = LocalDate.now().getYear()+"0101";
        synchronized (YEAR_PRICE_CACHE){
            logger.info("查询目前年度销售数据！！！ 初始化当前年度销售数据,参数:{}",orderDate);
            shopSalesDataService.queryYearPrice(orderDate).subscribe(e->{
                YEAR_PRICE_CACHE.put(e.getCode(),e.getPrice());
            });
            logger.info("YEAR_PRICE_CACHE size :{}",YEAR_PRICE_CACHE.size());
        }
        synchronized (MONTH_PRICE_CACHE){
            String startDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).toString();
            String endDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).toString();
            logger.info("查询目前月度销售数据！！！ 初始化当前月度销售数据,参数:{} and {}",startDate,endDate);
            shopSalesDataService.queryMonthPrice(startDate,endDate).subscribe(e->{
                MONTH_PRICE_CACHE.put(e.getCode(),e.getPrice());
            });
            logger.info("MONTH_PRICE_CACHE size :{}",MONTH_PRICE_CACHE.size());
        }
        synchronized (ALL_SALES_CACHE) {
            List<ShopSalesData> shopSalesDataList = new ArrayList<>(10000);
            logger.info("查询全部数据！！！ 查询全部数据,参数:{}",orderDate);
            shopSalesDataService.findAll(orderDate).subscribe(shopSalesDataList::add);
            ALL_SALES_CACHE.putAll(shopSalesDataList.stream().collect(Collectors.groupingBy(ShopSalesData::getShopId)));
            shopSalesDataList.clear();
            shopSalesDataList = null;
            logger.info("ALL_SALES_CACHE size :{}",ALL_SALES_CACHE.size());
        }
    }

    @Override
    public void destroy() throws Exception {
        logger.info("销售数据缓存销毁---------->start");
        synchronized (SHOP_SALES_DATA_CACHE){
            SHOP_SALES_DATA_CACHE.clear();
        }
        synchronized (TARGET_DATA_CACHE){
            TARGET_DATA_CACHE.clear();
        }
        YEAR_PRICE_CACHE.clear();
        MONTH_PRICE_CACHE.clear();
        ALL_SALES_CACHE.clear();
        logger.info("销售数据缓存销毁---------->end!!");
    }

    @Autowired
    public void setShopSalesDataService(ShopSalesDataService shopSalesDataService) {
        this.shopSalesDataService = shopSalesDataService;
    }

    @Autowired
    public void setShopTargetDataService(ShopTargetDataService shopTargetDataService) {
        this.shopTargetDataService = shopTargetDataService;
    }


}
