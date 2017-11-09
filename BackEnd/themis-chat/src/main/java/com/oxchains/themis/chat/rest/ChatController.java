package com.oxchains.themis.chat.rest;

import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.service.ChatService;
import com.oxchains.themis.common.model.RestResp;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * create by huohuo
 * @author huohuo
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
