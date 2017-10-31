package com.oxchains.themis.order.repo;

import com.oxchains.themis.order.entity.OrderTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ccl
 * @time 2017-10-17 11:27
 * @nameTransactionDao
 * @desc:
 */
@Repository
public interface OrderTransactionRepo extends CrudRepository<OrderTransaction,Integer> {
    OrderTransaction findByRecvAddress(String recvAddress);
    OrderTransaction findByOrderId(String orderId);
}
