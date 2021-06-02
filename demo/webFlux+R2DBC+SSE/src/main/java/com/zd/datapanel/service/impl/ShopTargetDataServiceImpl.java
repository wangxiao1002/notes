package com.zd.datapanel.service.impl;

import com.zd.datapanel.Repositories.ShopTargetDataRepository;
import com.zd.datapanel.entities.ShopTargetData;
import com.zd.datapanel.service.facade.ShopTargetDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author wang xiao
 * @date Created in 11:13 2021/1/27
 */
@Service
public class ShopTargetDataServiceImpl implements ShopTargetDataService {

    private ShopTargetDataRepository shopTargetDataRepository;

    @Override
    public Flux<ShopTargetData> queryNowYear() {
        return shopTargetDataRepository.findAll();
    }

    @Autowired
    public void setShopTargetDataRepository(ShopTargetDataRepository shopTargetDataRepository) {
        this.shopTargetDataRepository = shopTargetDataRepository;
    }
}
