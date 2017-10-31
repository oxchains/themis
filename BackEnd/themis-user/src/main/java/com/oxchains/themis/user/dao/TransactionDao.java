package com.oxchains.themis.user.dao;

import com.oxchains.themis.user.domain.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ccl
 * @time 2017-10-17 11:27
 * @name TransactionDao
 * @desc:
 */
@Repository
public interface TransactionDao extends CrudRepository<Transaction,Integer> {
    Transaction findByRecvAddress(String recvAddress);
    Transaction findByOrderId(String orderId);
}
