package com.oxchains.themis.chat.repo;

import com.oxchains.themis.chat.entity.ChatContent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xuqi on 2017/10/18.
 */
@Repository
public interface MongoRepo extends MongoRepository<ChatContent, String> {
    List<ChatContent> findChatContentByChatId(String chatId);
}
