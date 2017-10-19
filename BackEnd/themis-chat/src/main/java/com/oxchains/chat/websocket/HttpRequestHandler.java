package com.oxchains.chat.websocket;

import com.oxchains.chat.common.*;
import com.oxchains.chat.common.ChannelHandler;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;
    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }
    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String requestUri =  request.getUri().toString();
        if (requestUri.length()>5 && requestUri.contains(wsUri)){
            String token = requestUri.substring(requestUri.lastIndexOf("?")+1);
            JwtService.userChannels.put(JwtService.parse(token).getId()+"",new ChannelHandler(ctx.channel(),System.currentTimeMillis()));
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
