package com.oxchains.themisuser.service;

import com.oxchains.themisuser.dao.P2SHTransactionDao;
import com.oxchains.themisuser.domain.P2SHTransaction;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * @Author ccl
 * @Time 2017-10-17 11:27
 * @Name P2SHTransactionService
 * @Desc:
 */
@Transactional
@Service
public class P2SHTransactionService {
    @Resource
    private P2SHTransactionDao p2SHTransactionDao;

    P2SHTransaction findByRecvAddress(String recvAddress){
        return p2SHTransactionDao.findByRecvAddress(recvAddress);
    }
}
