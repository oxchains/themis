package oxchains.chat.websocket;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import oxchains.chat.common.CustomThreadFactory;
import oxchains.chat.common.KeepAliveChannelThread;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newScheduledThreadPool;

public class WebSocketServer implements Runnable{
    private  Integer port = 9999;
    public WebSocketServer() {}
    protected ScheduledExecutorService keepAliveScheduler = null;
    /*
    * running websocket
    * */

    @Override
    public void run(){
        this.keepAliveScheduler =newScheduledThreadPool(5,new CustomThreadFactory("keep-alive-channel",true));
        this.keepAliveScheduler.schedule(new KeepAliveChannelThread(this.keepAliveScheduler,20),5, TimeUnit.SECONDS);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WebsocketChatServerInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
