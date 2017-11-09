package com.oxchains.themis.arbitrate.repo;

import com.oxchains.themis.arbitrate.entity.Orders;
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
public interface OrderRepo extends CrudRepository<Orders,String>{
    List<Orders> findByArbitrate(Integer status);

}
