package com.oxchains.themis.chat.rest;

import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.service.ChatService;
import com.oxchains.themis.common.model.RestResp;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
<<<<<<< HEAD:BackEnd/themis-chat/src/main/java/oxchains/chat/rest/ChatController.java
import oxchains.chat.entity.ChatContent;
import oxchains.chat.service.ChatService;
=======
>>>>>>> b54ef991ebf23b343ec4f70ab27edc8e081f0b78:BackEnd/themis-chat/src/main/java/com/oxchains/themis/chat/rest/ChatController.java

import javax.annotation.Resource;

/**
 * Created by xuqi on 2017/10/18.
 */
@RestController
public class ChatController {
    @Resource
    private ChatService chatService;
    @RequestMapping("/chat/getChatHistroy")
    public RestResp getChatHistroy(ChatContent chatContent){
      return RestResp.success(chatService.getChatHistroy(chatContent));
    }

}
