package com.oxchains.themis.order.repo;

import com.oxchains.themis.order.entity.UserTxDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by huohuo on 2017/10/26.
 * @author huohuo
 */
@Repository
public interface UserTxDetailRepo extends CrudRepository<UserTxDetail,Long>{
    UserTxDetail findByUserId(Long id);
}
