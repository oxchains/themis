package oxchains.chat.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oxchains.chat.entity.ChatContent;
import oxchains.chat.service.ChatService;

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
