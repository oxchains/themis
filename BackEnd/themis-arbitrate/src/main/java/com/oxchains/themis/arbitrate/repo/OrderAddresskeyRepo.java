package com.oxchains.themis.arbitrate.repo;

import com.oxchains.themis.arbitrate.entity.OrderAddresskeys;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by huohuo on 2017/10/25.
 */
@Repository
public interface OrderAddresskeyRepo extends CrudRepository<OrderAddresskeys,Long> {
    OrderAddresskeys findOrderAddresskeysByOrderId(String id);
}
