package com.oxchains.chat.common;

import io.netty.channel.ChannelFuture;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuqi on 2017/10/19.
 */
public class KeepAliveChannelThread implements Runnable {
    private ScheduledExecutorService keepAliveScheduler;
    private long keepTime;

    public KeepAliveChannelThread(ScheduledExecutorService keepAliveScheduler, long keepTime) {
        this.keepAliveScheduler = keepAliveScheduler;
        this.keepTime = keepTime;
    }

    @Override
    public void run() {
        for (String s : JwtService.userChannels.keySet()) {
                if (System.currentTimeMillis() - JwtService.userChannels.get(s).getLastUseTime()>100){
                    ChannelFuture cf =  JwtService.userChannels.get(s).getChannel().closeFuture();
                    try {
                        cf.channel().close().sync();
                        JwtService.userChannels.remove(s);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }
        this.keepAliveScheduler.schedule(this,keepTime, TimeUnit.SECONDS);


    }
}
