package com.oxchains.themis.order.repo;

import com.oxchains.themis.order.entity.OrderTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author ccl
 * @Time 2017-10-17 11:27
 * @Name TransactionDao
 * @Desc:
 */
@Repository
public interface OrderTransactionRepo extends CrudRepository<OrderTransaction,Integer> {
    OrderTransaction findByRecvAddress(String recvAddress);
    OrderTransaction findByOrderId(String orderId);
}
