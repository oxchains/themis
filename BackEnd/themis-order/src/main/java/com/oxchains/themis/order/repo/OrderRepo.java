package com.oxchains.themis.order.repo;

import com.oxchains.themis.order.entity.Orders;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by xuqi on 2017/10/23.
 */
@Repository
public interface OrderRepo extends CrudRepository<Orders,String>{
}
