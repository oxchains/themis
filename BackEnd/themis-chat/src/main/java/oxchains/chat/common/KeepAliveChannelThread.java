package oxchains.chat.common;

import io.netty.channel.ChannelFuture;
import oxchains.chat.websocket.TextWebSocketFrameHandler;

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
        for (String s : ChatUtil.userChannels.keySet()) {
            for (String s1 : ChatUtil.userChannels.get(s).keySet()){
                if (System.currentTimeMillis() - ChatUtil.userChannels.get(s).get(s1).getLastUseTime()>(15*1000)){
                    ChannelFuture cf =  ChatUtil.userChannels.get(s).get(s1).getChannel().closeFuture();
                    try {
                        cf.channel().close().sync();
                        ChatUtil.userChannels.get(s).remove(s1);
                        System.out.println("消灭.......................");
                        TextWebSocketFrameHandler.channels.remove(cf.channel());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        this.keepAliveScheduler.schedule(this,keepTime, TimeUnit.SECONDS);


    }
}
