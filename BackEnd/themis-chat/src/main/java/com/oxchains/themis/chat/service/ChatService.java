package com.oxchains.themis.chat.service;

import com.alibaba.fastjson.JSONObject;
import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.repo.MongoRepo;
import com.oxchains.themis.common.constant.ThemisUserAddress;
import com.oxchains.themis.repo.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.oxchains.themis.chat.websocket.ChatUtil;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * create by huohuo
 * @author huohuo
 */
@Service
public class ChatService {
    @Resource
    private MongoRepo mongoRepo;
    @Resource
    RestTemplate restTemplate;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    public List<ChatContent> getChatHistroy(ChatContent chatContent){
        try{

            String username  = this.getUserById(chatContent.getSenderId().longValue()).getLoginname();
            String dusername  = this.getUserById(chatContent.getReceiverId().longValue()).getLoginname();

            String keyIDs = ChatUtil.getIDS(chatContent.getSenderId().toString(),chatContent.getReceiverId().toString());
            List<ChatContent> list = mongoRepo.findChatContentByChatIdAndOrderId(keyIDs,chatContent.getOrderId());
            for (ChatContent content:list) {
                if(content.getSenderId().longValue()==chatContent.getSenderId().longValue())
                {
                    content.setSenderName(username);
                }
                else{content.setSenderName(dusername);}
            }
            return list;
        }
        catch (Exception e){
            LOG.error("faild get chat history : {}",e.getMessage(),e);
        }
        return null;
    }
    private User getUserById(Long userId){
        User user = null;
        try {
            JSONObject str = restTemplate.getForObject(ThemisUserAddress.GET_USER+userId, JSONObject.class);
            if(null != str){
                Integer status = (Integer) str.get("status");
                if(status == 1){
                    user = (User) str.get("data");
                }
            }
            return user;
        } catch (Exception e) {
            LOG.error("get user by id from themis-user faild : {}",e.getMessage(),e);
        }
        return null;
    }
}
