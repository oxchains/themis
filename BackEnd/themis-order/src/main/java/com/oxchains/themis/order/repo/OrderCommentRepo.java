package com.oxchains.themis.order.repo;

import com.oxchains.themis.order.entity.OrderComment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by huohuo on 2017/10/28.
 */
@Repository
public interface OrderCommentRepo extends CrudRepository<OrderComment,Long>{
    OrderComment findOrderCommentByOrderId(String orderId);
}
