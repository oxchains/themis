package com.oxchains.themis.chat.service;

import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.repo.MongoRepo;
import com.oxchains.themis.chat.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.oxchains.themis.chat.common.ChatUtil;

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

            String username  = userRepo.findOne(chatContent.getSenderId().longValue()).getUsername();
            String dusername  = userRepo.findOne(chatContent.getReceiverId().longValue()).getUsername();
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
            LOG.error("faild get chat history : {}",e.getMessage(),e);
        }
        return null;
    }
}
