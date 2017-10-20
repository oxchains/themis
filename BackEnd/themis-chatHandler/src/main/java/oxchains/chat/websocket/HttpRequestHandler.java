package com.oxchains.chat.websocket;

import com.oxchains.chat.common.ChannelHandler;
import com.oxchains.chat.common.JwtService;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;

import java.util.concurrent.ConcurrentHashMap;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }
    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String requestUri =  request.getUri().toString();
        if (requestUri.length()>5 && requestUri.contains(wsUri)){
            String message = requestUri.substring(requestUri.lastIndexOf("?")+1);
            String token = message.substring(0,message.lastIndexOf("_"));
            String id = JwtService.parse(token).getId()+"";
            String did = message.substring(message.lastIndexOf("_")+1);
            if(JwtService.userChannels.get(id) == null){
                JwtService.userChannels.put(id,new ConcurrentHashMap<String ,ChannelHandler>());
            }
            JwtService.userChannels.get(id).put(JwtService.getIDS(id,did),new ChannelHandler(ctx.channel(),System.currentTimeMillis()));
            ctx.fireChannelRead(request.retain());
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
    private String findUserIdByUri(String uri) {// 通过Uid获取用户Id--uri中包含userId
        String userId = "";
        try {
            userId = uri.substring(uri.indexOf("userId") + 7);
            if (userId != null && userId.trim() != null && userId.trim().length() > 0) {
                userId = userId.trim();
            }
        } catch (Exception e) {
        }
        return userId;
    }


}
