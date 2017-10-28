package com.oxchains.themis.arbitrate.repo;

import com.oxchains.themis.arbitrate.entity.OrderArbitrate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by huohuo on 2017/10/25.
 */
@Repository
public interface OrderArbitrateRepo extends CrudRepository<OrderArbitrate,Long> {
    List<OrderArbitrate> findOrderArbitrateByUserIdAndAndStatus(Long userId, Integer status);
    OrderArbitrate findOrderArbitrateByUserIdAndOrderId(Long id, String orderId);
    List<OrderArbitrate> findOrderArbitrateByOrderId(String orderId);
}
