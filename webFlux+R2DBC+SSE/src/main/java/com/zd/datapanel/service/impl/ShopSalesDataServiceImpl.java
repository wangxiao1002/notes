package com.zd.datapanel.service.impl;

import com.zd.datapanel.Repositories.ShopSalesDataRepository;
import com.zd.datapanel.entities.ShopSalesData;
import com.zd.datapanel.service.facade.ShopSalesDataService;
import com.zd.datapanel.support.PriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 10:47 2021/1/27
 */
@Service
public class ShopSalesDataServiceImpl implements ShopSalesDataService {

    private ShopSalesDataRepository shopSalesDataRepository;

    @Override
    public void savaBatch(List<ShopSalesData> entities) {
        shopSalesDataRepository.saveAll(entities);
    }


    @Override
    public Flux<ShopSalesData> queryByDate(String lastDay) {
        return shopSalesDataRepository.findAllByOrderDate(lastDay);
    }


    @Override
    public Flux<PriceDTO> queryYearPrice(String startDate) {
        return shopSalesDataRepository.sumYearPriceGroupShopId(startDate);
    }

    @Override
    public Flux<PriceDTO> queryMonthPrice(String startDate, String endDate) {
        return shopSalesDataRepository.sumMonthPriceGroupShopId(startDate,endDate);
    }

    @Override
    public Flux<ShopSalesData> queryByOrderDateBetween(String startDate, String endDate) {
        return shopSalesDataRepository.findByOrderDateBetween(startDate,endDate);
    }

    @Override
    public Flux<ShopSalesData> findAll(String beginDate) {
        return shopSalesDataRepository.findByOrderDateGreaterThanEqual(beginDate);
    }

    @Autowired
    public void setShopSalesDataRepository(ShopSalesDataRepository shopSalesDataRepository) {
        this.shopSalesDataRepository = shopSalesDataRepository;
    }
}
