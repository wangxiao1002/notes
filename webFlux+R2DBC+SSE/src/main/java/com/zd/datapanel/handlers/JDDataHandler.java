package com.zd.datapanel.handlers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zd.datapanel.entities.ShopSalesData;
import com.zd.datapanel.support.Constants;
import com.zd.datapanel.support.Result;
import com.zd.datapanel.support.SalesCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

/**
 * 京东数据处理
 * @author wang xiao
 * @date Created in 10:48 2021/1/26
 */
@RestController
@RequestMapping("/jd")
public class JDDataHandler {



    private final Logger logger = LoggerFactory.getLogger(JDDataHandler.class);


    private SalesCache salesCache;


    /**
     *  旗舰店数据
     * @author wangxiao
     * @date 11:17 2021/1/26
     * @param jsonObject 发送方数据
     * @return com.zd.datapanel.support.Result<java.lang.Void>
     */
    @PostMapping("/flagship")
    public Mono<Result<Void>> acceptPacketOfFlagship(@RequestBody @NonNull JSONObject jsonObject){
        logger.info("接受到京东旗舰店数据:{},时间：{}--------->start",jsonObject.toJSONString(),Instant.now().getEpochSecond());
        JSONObject shopDataOverviewInfo = jsonObject.getJSONObject("data").getJSONObject("shopDataOverviewInfo");
        if (Objects.isNull(shopDataOverviewInfo)){
            Result.ofSuccessMono(null);
        }
        JSONArray shopDataModuleItems = shopDataOverviewInfo.getJSONArray("shopDataModuleItemVOS");
        if (Objects.isNull(shopDataModuleItems)){
            Result.ofSuccessMono(null);
        }
        int length = shopDataModuleItems.size();
        String nowDate = LocalDate.now().format(Constants.DAY_FORMATTER);
        ShopSalesData nowShopSalesData = new ShopSalesData(Constants.JD_FLAGSHIP_SHOP,nowDate);
        JSONObject tempJsonObject = null;
        for (int i=0; i<length; i++) {
            tempJsonObject = shopDataModuleItems.getJSONObject(i);
            Optional.ofNullable(tempJsonObject).map(js->{
                assignment(js,nowShopSalesData);
                return js;
            });
        }
        salesCache.addCache(nowDate,Constants.JD_FLAGSHIP_SHOP,nowShopSalesData);
        logger.info("接受到京东旗舰店数据,时间：{}--------->end",Instant.now().getEpochSecond());
        return Result.ofSuccessMono(null);
    }


    /**
     *  自营店数据
     * @author wangxiao
     * @date 11:17 2021/1/26
     * @param jsonObject 发送方数据
     * @return reactor.core.publisher.Mono<com.zd.datapanel.support.Result<java.lang.Void>>
     */
    @PostMapping("/self")
    public Mono<Result<Void>> acceptPacketOfSelf(@RequestBody @NonNull JSONObject jsonObject){
        logger.info("接受到京东自营店数据:{},时间：{}--------->start",jsonObject.toJSONString(),Instant.now().getEpochSecond());
        JSONObject result = jsonObject.getJSONObject("result");
        String nowDate = LocalDate.now().format(Constants.DAY_FORMATTER);
        ShopSalesData nowShopSalesData = new ShopSalesData(Constants.JD_SELF_SHOP,nowDate);
        Optional.ofNullable(result).map(r->{
            nowShopSalesData.setOrderNum(r.getLong("orderCount"));
            nowShopSalesData.setOrderPrice(r.getBigDecimal("orderPayment"));
            nowShopSalesData.setUvNum(r.getLong("uvPC"));
            return r;
        });
        salesCache.addCache(nowDate,Constants.JD_FLAGSHIP_SHOP,nowShopSalesData);
        logger.info("接受到京东自营店数据,时间：{}--------->end",Instant.now().getEpochSecond());
        return Result.ofSuccessMono(null);
    }


    /**
     *  实体类赋值
     * @author wangxiao
     * @date 19:36 2021/1/26
     * @param jsonObject json 数据
     * @param shopSalesData 被复制的实体
     */
    private void assignment (JSONObject jsonObject,ShopSalesData shopSalesData){
        if (null == jsonObject){
            return;
        }
        String value = jsonObject.getString("realTimeNumber");
        switch (jsonObject.getIntValue("id")){
            case 1001:
                BigDecimal price = parseDecimal(value);
                shopSalesData.setOrderPrice(price);
                break;
            case 1002:
                Long orderNum = parseLong(value);
                shopSalesData.setOrderNum(orderNum);
                break;
            case 1004:
                Long uvNum = parseLong(value);
                shopSalesData.setUvNum(uvNum);
                break;
            default:
                break;
        }

    }

    /**
     *  千分位字符串 转 BigDecimal
     * @author wangxiao
     * @date 19:37 2021/1/26
     * @param source 源字符串
     * @return java.math.BigDecimal
     */
    private BigDecimal parseDecimal (String source) {
        return (BigDecimal) Constants.DECIMAL_FORMAT.parse(source, Constants.PARSE_POSITION);
    }

    /**
     *  千分位字符串 转 Long
     * @author wangxiao
     * @date 19:37 2021/1/26
     * @param source 源字符串
     * @return Long
     */
    private Long parseLong (String source) {
        return (Long) Constants.DECIMAL_FORMAT.parse(source, Constants.PARSE_POSITION);
    }


    @Autowired
    public void setSalesCache(SalesCache salesCache) {
        this.salesCache = salesCache;
    }
}
