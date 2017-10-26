package com.oxchains.themis.notice.dao;

import com.oxchains.themis.notice.domain.HomeTrust;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * 测试dao
 *
 * @author luoxuri
 * @create 2017-10-25 17:47
 **/
@Repository
public interface HomeTrustDao extends CrudRepository<HomeTrust, Long> {

}
