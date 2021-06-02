package com.zd.datapanel.Repositories;

import com.zd.datapanel.entities.ShopTargetData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * @author wang xiao
 * @date Created in 17:42 2021/1/26
 */
public interface ShopTargetDataRepository extends ReactiveCrudRepository<ShopTargetData,Long> {
}
