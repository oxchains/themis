package com.oxchains.themis.message.dao;

import com.oxchains.themis.message.domain.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author luoxuri
 * @create 2017-11-06 14:59
 **/
@Repository
public interface MessageDao extends CrudRepository<Message, Long> {

    List<Message> findByReceiverIdAndReadStatus(Long receiverId, Integer readStatus);

    List<Message> findByReceiverId(Long receiverId);

    List<Message> findByMessageTextId(Long messageTextId);

    List<Message> findByMessageTextIdAndReceiverId(Long messageTextId, Long receiverId);
}