package com.oxchains.themis.chat.websocket;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * create by huohuo
 * @author huohuo
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }
    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String requestUri =  request.getUri().toString();
        if (requestUri.contains(wsUri)){
            String id = null;
            String receiverId = null;
            String message = requestUri.substring(requestUri.lastIndexOf("?")+1);
            String[] ids = message.split("_");
            if(ids.length>=2){
                id = ids[0];
                receiverId = ids[1];
            }
            if(id != null && receiverId != null){
                //判断当前用户的channel分区是否已经创建，如未创建 则创建之
                if(ChatUtil.userChannels.get(id) == null){
                    ChatUtil.userChannels.put(id,new ConcurrentHashMap<String ,ChannelHandler>());
                }
                Map<String,ChannelHandler> channelHandlerMap =  ChatUtil.userChannels.get(id);
                String keyIds = ChatUtil.getIDS(id,receiverId);
                //如果连接存在 则把以前的连接关闭掉 建立新的连接
                if(channelHandlerMap.get(keyIds) != null){
                    channelHandlerMap.get(keyIds).close();
                    channelHandlerMap.remove(keyIds);
                }
                channelHandlerMap.put(keyIds,new ChannelHandler(ctx.channel(),System.currentTimeMillis()));
                ctx.fireChannelRead(request.retain());
            }
        }
        else {
            HttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
                boolean keepAlive = HttpHeaders.isKeepAlive(request);
            if (keepAlive) {
                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }
            ctx.write(response);
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }

        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    private void getIdAndReceiverId(String id,String receiverid,String requestUri){


    }
}
