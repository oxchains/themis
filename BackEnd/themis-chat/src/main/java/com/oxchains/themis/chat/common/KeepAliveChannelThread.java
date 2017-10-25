package com.oxchains.themis.chat.common;

import com.oxchains.themis.chat.websocket.TextWebSocketFrameHandler;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuqi on 2017/10/19.
 */
public class KeepAliveChannelThread implements Runnable {
    private ScheduledExecutorService keepAliveScheduler;
    private long keepTime;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    public KeepAliveChannelThread(ScheduledExecutorService keepAliveScheduler, long keepTime) {
        this.keepAliveScheduler = keepAliveScheduler;
        this.keepTime = keepTime;
    }
    @Override
    public void run() {
        try {
        for (String s : ChatUtil.userChannels.keySet()) {
            for (String s1 : ChatUtil.userChannels.get(s).keySet()){
                if (System.currentTimeMillis() - ChatUtil.userChannels.get(s).get(s1).getLastUseTime()>(15*1000)){
                    ChannelFuture cf =  ChatUtil.userChannels.get(s).get(s1).getChannel().closeFuture();
                        cf.channel().close().sync();
                        ChatUtil.userChannels.get(s).remove(s1);
                        TextWebSocketFrameHandler.channels.remove(cf.channel());
                }
            }
        }
        }catch (Exception e){
            LOG.debug("Keep Alive websocket channel faild :",e.getMessage());
        }
        this.keepAliveScheduler.schedule(this,keepTime, TimeUnit.SECONDS);


    }
}
