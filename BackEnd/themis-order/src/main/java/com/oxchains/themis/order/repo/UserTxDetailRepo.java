package com.oxchains.themis.order.repo;

import com.oxchains.themis.order.entity.UserTxDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by huohuo on 2017/10/26.
 */
@Repository
public interface UserTxDetailRepo extends CrudRepository<UserTxDetail,Long>{
}
