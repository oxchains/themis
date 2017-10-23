package com.oxchains.themisuser.service;

import com.oxchains.themisuser.dao.OrderDao;
import com.oxchains.themisuser.domain.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * @Author ccl
 * @Time 2017-10-17 11:27
 * @Name OrderService
 * @Desc:
 */
@Transactional
@Service
public class OrderService {
    @Resource
    private OrderDao orderDao;

    Order findByRecvAddress(String recvAddress) {
        return orderDao.findByRecvAddress(recvAddress);
    }
}
