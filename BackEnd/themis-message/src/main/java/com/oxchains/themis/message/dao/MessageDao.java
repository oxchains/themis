package com.oxchains.themis.message.dao;

import com.oxchains.themis.message.domain.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author luoxuri
 * @create 2017-11-06 14:59
 **/
@Repository
public interface MessageDao extends CrudRepository<Message, Long> {
}
