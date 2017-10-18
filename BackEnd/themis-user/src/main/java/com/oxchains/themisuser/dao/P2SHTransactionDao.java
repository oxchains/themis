package com.oxchains.themisuser.dao;

import com.oxchains.themisuser.domain.P2SHTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author ccl
 * @Time 2017-10-17 11:27
 * @Name P2SHTransactionDao
 * @Desc:
 */
@Repository
public interface P2SHTransactionDao extends CrudRepository<P2SHTransaction,Integer> {
    P2SHTransaction findByRecvAddress(String recvAddress);
}
