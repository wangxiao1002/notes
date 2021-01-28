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

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

/**
 * 天猫数据处理
 * @author wang xiao
 * @date Created in 10:49 2021/1/26
 */
@RestController
@RequestMapping("/tmall")
public class TMallDataHandler {



    private final Logger logger = LoggerFactory.getLogger(TMallDataHandler.class);

    private SalesCache salesCache;


    @PostMapping
    public Mono<Result<Void>> acceptPacketOfStore(@RequestBody @NonNull JSONObject jsonObject){
        logger.info("接受到天猫旗舰店数据:{},时间：{}--------->start",jsonObject.toJSONString(),Instant.now().getEpochSecond());
        JSONObject today = jsonObject.getJSONObject("content").getJSONObject("data").getJSONObject("data").getJSONObject("today");
        if (Objects.isNull(today)){
            return Result.ofSuccessMono(null);
        }
        String nowDate = LocalDate.now().format(Constants.DAY_FORMATTER);
        ShopSalesData nowShopSalesData = new ShopSalesData(Constants.TMALL_SHOP,nowDate);
        JSONArray uv = today.getJSONArray("uv");
        JSONArray payByrCnt = today.getJSONArray("payByrCnt");
        JSONArray payAmt = today.getJSONArray("payAmt");
        // fixme null 不会反序列化进数组
        Optional.ofNullable(uv).ifPresent(js->{
            nowShopSalesData.setUvNum(js.getLong( js.size()-1));
        });
        Optional.ofNullable(payByrCnt).ifPresent(js->{
            nowShopSalesData.setOrderNum(js.getLong( js.size()-1));
        });
        Optional.ofNullable(payAmt).ifPresent(js->{
            nowShopSalesData.setOrderPrice(js.getBigDecimal( js.size()-1));
        });
        salesCache.addCache(nowDate,Constants.TMALL_SHOP,nowShopSalesData);
        logger.info("接受到天猫旗舰店数据,时间：{}--------->end",Instant.now().getEpochSecond());
        return Result.ofSuccessMono(null);
    }

    @Autowired
    public void setSalesCache(SalesCache salesCache) {
        this.salesCache = salesCache;
    }
}
