package com.oxchains.themis.user.dao;

import com.oxchains.themis.user.domain.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author ccl
 * @Time 2017-10-17 11:27
 * @Name P2SHTransactionDao
 * @Desc:
 */
@Repository
public interface OrderDao extends CrudRepository<Order,Integer> {
    Order findByRecvAddress(String recvAddress);
}
