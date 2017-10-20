package oxchains.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oxchains.chat.common.JwtService;
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

    @Resource
    private UserRepo userRepo;

    public List<ChatContent> getChatHistroy(ChatContent chatContent){
        try{

            String username  = userRepo.findOne(chatContent.getSenderId()).getUsername();
            String dusername  = userRepo.findOne(chatContent.getReceiverId()).getUsername();
            String keyIDs = JwtService.getIDS(chatContent.getSenderId().toString(),chatContent.getReceiverId().toString());
            List<ChatContent> list = mongoRepo.findChatContentByChatId(keyIDs);
            for (ChatContent content:list) {
                if(content.getSenderId().longValue()==chatContent.getSenderId().longValue())
                {
                    content.setSenderName(username);
                }
                else{content.setSenderName(dusername);}

                System.out.println(content);
            }
            return mongoRepo.findChatContentByChatId(keyIDs);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
