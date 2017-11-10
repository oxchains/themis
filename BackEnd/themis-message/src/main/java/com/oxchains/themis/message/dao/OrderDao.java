package com.oxchains.themis.message.dao;

import com.oxchains.themis.message.domain.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by huohuo on 2017/10/23.
 * @author huohuo
 */
@Repository
public interface OrderDao extends CrudRepository<Orders,String>{
    @Query(value = " select  s from Orders as s where  (s.buyerId = :id or s.sellerId = :ids ) and s.orderStatus in ( :status) ")
    Page<Orders> findOrdersByBuyerIdOrSellerIdAndOrderStatus(@Param("id") Long id, @Param("ids") Long ids, @Param("status") List<Long> status, Pageable pageable);
    @Query(value = " select  s from Orders as s where  (s.buyerId = :id or s.sellerId = :ids ) and s.orderStatus not in ( :status)")
    Page<Orders> findOrdersByBuyerIdOrSellerIdAndOrderStatusIsNotIn(@Param("id") Long id, @Param("ids") Long ids, @Param("status") List<Long> status, Pageable pageable);
    List<Orders> findOrdersByNoticeIdAndOrderStatus(Long id, Long status);
    List<Orders> findOrdersByOrderStatus(Long status);
    Integer countByBuyerIdAndOrderStatus(Long userId, Long status);
    Integer countBySellerIdAndOrderStatus(Long userId, Long status);
}
