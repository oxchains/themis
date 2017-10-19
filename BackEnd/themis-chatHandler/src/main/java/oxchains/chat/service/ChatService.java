package oxchains.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

            String username  = userRepo.findOne(chatContent.getBid()).getUsername();
            String dusername  = userRepo.findOne(chatContent.getDid()).getUsername();
            List<ChatContent> list = mongoRepo.findChatContentByChatId(String.valueOf((chatContent.getBid()+chatContent.getDid()) * (chatContent.getBid()* chatContent.getDid())));
            for (ChatContent content:list) {
                if(content.getBid().longValue()==chatContent.getBid().longValue())
                {
                    content.setUsername(username);
                }
                else{content.setUsername(dusername);}

                System.out.println(content);
            }
            return mongoRepo.findChatContentByChatId(String.valueOf((chatContent.getBid()+chatContent.getDid()) * (chatContent.getBid()* chatContent.getDid())));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
