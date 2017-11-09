package com.oxchains.themis.chat.websocket.chatfunction.function;

import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.service.KafkaService;
import com.oxchains.themis.chat.service.MessageService;
import com.oxchains.themis.chat.websocket.ChannelHandler;
import com.oxchains.themis.chat.websocket.ChatUtil;
import com.oxchains.themis.chat.websocket.chatfunction.InfoStrategy;
import com.oxchains.themis.common.util.DateUtil;
import com.oxchains.themis.common.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by xuqi on 2017/11/7.
 * @author huohuo
 */
@Service
public class UserChat implements InfoStrategy{
    private KafkaService kafkaService;
    private ChannelHandlerContext ctx;
    @Resource
    private MessageService messageService;
    public UserChat(KafkaService kafkaService, ChannelHandlerContext ctx){
        this.kafkaService = kafkaService;
        this.ctx = ctx;
    }

    public UserChat() {
    }

    @Override
    public void disposeInfo(ChatContent chatContent) {
        //接收到消息后先给自己转发一份
        String keyIDs = ChatUtil.getIDS(chatContent.getSenderId().toString(),chatContent.getReceiverId().toString());
        chatContent.setCreateTime(DateUtil.getPresentDate());
        chatContent.setChatId(keyIDs);
        String message = JsonUtil.toJson(chatContent).toString();
        kafkaService.send(message);
        ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
        //再给对方转发
        Map<String,ChannelHandler> channelHandlerMap = ChatUtil.userChannels.get(chatContent.getReceiverId().toString());
            if( channelHandlerMap!= null && channelHandlerMap.get(keyIDs)!=null){
                channelHandlerMap.get(keyIDs).getChannel().writeAndFlush(new TextWebSocketFrame(message));
            }
            else{
                //messageService.postPriChatMessage(chatContent);
            }
    }
}
