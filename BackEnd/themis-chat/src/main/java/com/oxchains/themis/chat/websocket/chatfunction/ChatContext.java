package com.oxchains.themis.chat.websocket.chatfunction;

import com.oxchains.themis.chat.entity.ChatContent;

/**
 * Created by xuqi on 2017/11/6.
 */
public class ChatContext {
    private InfoStrategy infoStrategy;
    public ChatContext(InfoStrategy infoStrategy){
        this.infoStrategy = infoStrategy;
    }
    public void disposeInfo(ChatContent chatContext){
        infoStrategy.disposeInfo(chatContext);
    }
}
