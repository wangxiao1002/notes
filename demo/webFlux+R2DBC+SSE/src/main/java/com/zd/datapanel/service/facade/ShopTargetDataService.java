package com.zd.datapanel.service.facade;

import com.zd.datapanel.entities.ShopTargetData;
import reactor.core.publisher.Flux;

/**
 * @author wang xiao
 * @date Created in 11:13 2021/1/27
 */
public interface ShopTargetDataService {

    /**
     *  查询今年目标
     * @author wangxiao
     * @date 11:35 2021/1/27
     * @return reactor.core.publisher.Flux<com.zd.datapanel.entities.ShopTargetData>
     */
    Flux<ShopTargetData> queryNowYear();
}
