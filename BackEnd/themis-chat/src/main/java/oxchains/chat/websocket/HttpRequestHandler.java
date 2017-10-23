package oxchains.chat.websocket;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oxchains.chat.common.ChannelHandler;
import oxchains.chat.common.ChatUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }
    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String requestUri =  request.getUri().toString();
        if (requestUri.length()>5 && requestUri.contains(wsUri)){
            String message = requestUri.substring(requestUri.lastIndexOf("?")+1);
            String token = message.substring(0,message.lastIndexOf("_"));
            String id = ChatUtil.parse(token).getId()+"";
            String receiverId = message.substring(message.lastIndexOf("_")+1);
            if(ChatUtil.userChannels.get(id) == null){
                ChatUtil.userChannels.put(id,new ConcurrentHashMap<String ,ChannelHandler>());
            }
            String keyIds = ChatUtil.getIDS(id,receiverId);
            Map<String,ChannelHandler> channelHandlerMap =  ChatUtil.userChannels.get(id);
            if(channelHandlerMap.get(keyIds) != null){
                channelHandlerMap.get(keyIds).close();
                channelHandlerMap.remove(keyIds);
            }
            channelHandlerMap.put(keyIds,new ChannelHandler(ctx.channel(),System.currentTimeMillis()));
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
