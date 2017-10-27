package com.oxchains.themis.arbitrate.repo;

import com.oxchains.themis.arbitrate.entity.Orders;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by huohuo on 2017/10/23.
 */
@Repository
public interface OrderRepo extends CrudRepository<Orders,String> {
    @Query(value = " select  s from Orders as s where  (s.buyerId = :id or s.sellerId = :ids ) and s.orderStatus = :status ")
    List<Orders> findOrdersByBuyerIdOrSellerIdAndOrderStatus(@Param("id") Long id, @Param("ids") Long ids, @Param("status") Long status);
    @Query(value = " select  s from Orders as s where  (s.buyerId = :id or s.sellerId = :ids ) and s.orderStatus not in (:status,:staus2)  ")
    List<Orders> findOrdersByBuyerIdOrSellerIdAndOrderStatusIsNot(@Param("id") Long id, @Param("ids") Long ids, @Param("status") Long status, @Param("staus2") Long status2);
    List<Orders> findOrdersByNoticeIdAndOrderStatus(Long id, Long status);

}
