package com.oxchains.themis.chat.websocket.chatfunction.function;

import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.websocket.ChannelHandler;
import com.oxchains.themis.chat.websocket.ChatUtil;
import com.oxchains.themis.chat.websocket.chatfunction.InfoStrategy;
import com.oxchains.themis.common.util.JsonUtil;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Map;

/**
 * Created by xuqi on 2017/11/7.
 */
public class HealthCheck implements InfoStrategy{
    @Override
    public void disposeInfo(ChatContent chatContent) {
        Map<String,ChannelHandler> channelHandlerMap = ChatUtil.userChannels.get(chatContent.getSenderId()+"");
        String keyIDs = ChatUtil.getIDS(chatContent.getSenderId().toString(),chatContent.getReceiverId().toString());
        ChannelHandler channelHandler = channelHandlerMap.get(keyIDs);
        if(channelHandler!=null){
            channelHandler.setLastUseTime(System.currentTimeMillis());
            chatContent.setStatus("success");
        }
        else{
            chatContent.setStatus("error");
        }
        channelHandler.getChannel().writeAndFlush(new TextWebSocketFrame(JsonUtil.toJson(chatContent)));
    }
}
