package com.oxchains.themis.repo.dao;

import com.oxchains.themis.repo.entity.UserTxDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author ccl
 * @Time 2017-10-30 19:00
 * @Name UserTxDetailDao
 * @Desc:
 */
@Repository
public interface UserTxDetailDao extends CrudRepository<UserTxDetail,Long>{
    UserTxDetail findByUserId(Long userId);
}
