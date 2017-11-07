package com.oxchains.themis.message.dao;

import com.oxchains.themis.message.domain.MessageText;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author luoxuri
 * @create 2017-11-06 15:00
 **/
@Repository
public interface MessageTextDao extends CrudRepository<MessageText, Long> {
}
