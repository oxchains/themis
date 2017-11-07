package com.oxchains.themis.order.repo;

import com.oxchains.themis.order.entity.OrderArbitrate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by huohuo on 2017/10/25.
 * @author huohuo
 */
@Repository
public interface OrderArbitrateRepo extends CrudRepository<OrderArbitrate,Long>{
    Page<OrderArbitrate> findOrderArbitrateByUserIdAndAndStatusIsNot(Long userId, Integer status, Pageable pageable);
    OrderArbitrate findOrderArbitrateByUserIdAndOrderId(Long id,String orderId);
    List<OrderArbitrate> findOrderArbitrateByOrderId(String orderId);
    OrderArbitrate findByOrOrderIdAndStatus(String id,Integer status);

}
