package com.oxchains.themis.arbitrate.repo;

import com.oxchains.themis.arbitrate.entity.UserTxDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by huohuo on 2017/10/26.
 * @author huohuo
 */
@Repository
public interface UserTxDetailRepo extends CrudRepository<UserTxDetails,Long> {
    UserTxDetails findByUserId(Long id);
}
