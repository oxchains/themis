package com.oxchains.themis.chat.rest;

import com.oxchains.common.model.RestResp;
import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.service.ChatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
