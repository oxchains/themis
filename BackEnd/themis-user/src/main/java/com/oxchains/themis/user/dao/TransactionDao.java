package com.oxchains.themis.user.dao;

import com.oxchains.themis.user.domain.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author ccl
 * @Time 2017-10-17 11:27
 * @Name TransactionDao
 * @Desc:
 */
@Repository
public interface TransactionDao extends CrudRepository<Transaction,Integer> {
    Transaction findByRecvAddress(String recvAddress);
}
