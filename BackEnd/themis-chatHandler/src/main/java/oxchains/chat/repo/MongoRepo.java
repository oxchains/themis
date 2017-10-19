package oxchains.chat.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import oxchains.chat.entity.ChatContent;

import java.util.List;

/**
 * Created by xuqi on 2017/10/18.
 */
@Repository
public interface MongoRepo extends MongoRepository<ChatContent, String> {

    List<ChatContent> findChatContentByChatId(String chatId);
}
