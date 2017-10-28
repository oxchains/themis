package com.oxchains.themis.notice.dao;

import com.oxchains.themis.notice.domain.UserTxDetail;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 *
 * @author luoxuri
 * @create 2017-10-26 21:06
 **/
public interface UserTxDetailDao extends CrudRepository<UserTxDetail, Long> {

}
