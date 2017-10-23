package oxchains.chat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oxchains.chat.common.ChatUtil;
import oxchains.chat.entity.ChatContent;
import oxchains.chat.repo.MongoRepo;
import oxchains.chat.repo.UserRepo;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xuqi on 2017/10/18.
 */
@Service
public class ChatService {
    @Autowired
    private MongoRepo mongoRepo;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Resource
    private UserRepo userRepo;

    public List<ChatContent> getChatHistroy(ChatContent chatContent){
        try{

            String username  = userRepo.findOne(chatContent.getSenderId()).getUsername();
            String dusername  = userRepo.findOne(chatContent.getReceiverId()).getUsername();
            String keyIDs = ChatUtil.getIDS(chatContent.getSenderId().toString(),chatContent.getReceiverId().toString());
            List<ChatContent> list = mongoRepo.findChatContentByChatId(keyIDs);
            for (ChatContent content:list) {
                if(content.getSenderId().longValue()==chatContent.getSenderId().longValue())
                {
                    content.setSenderName(username);
                }
                else{content.setSenderName(dusername);}

            }
            return mongoRepo.findChatContentByChatId(keyIDs);
        }
        catch (Exception e){
            LOG.debug("faild get chat history :",e.getMessage());
        }
        return null;
    }
}
