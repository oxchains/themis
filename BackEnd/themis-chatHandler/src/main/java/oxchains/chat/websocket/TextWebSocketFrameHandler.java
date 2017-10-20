package com.oxchains.chat.websocket;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oxchains.chat.common.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class TextWebSocketFrameHandler extends
        SimpleChannelInboundHandler<TextWebSocketFrame> {
	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			TextWebSocketFrame msg) throws Exception {
		ChatContent chatContent= (ChatContent) JsonUtil.fromJson(msg.text(), ChatContent.class);
		chatContent.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		String message = JsonUtil.toJson(chatContent).toString();
		KafkaUtil.send(message);
		ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
		Map map = JwtService.userChannels.get(chatContent.getDid()+"");
		String ids = JwtService.getIDS(chatContent.getBid().toString(),chatContent.getDid().toString());
		if( map!= null && map.get(ids)!=null){
			((ChannelHandler)(map.get(ids))).getChannel().writeAndFlush(new TextWebSocketFrame(message));
		}
	}
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
			ctx.pipeline().remove(HttpRequestHandler.class);
			channels.add(ctx.channel());
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}
	@Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		channels.add(incoming);
	}
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        incoming.closeFuture();
		channels.remove(incoming);

    }
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
    	Channel incoming = ctx.channel();
        cause.printStackTrace();
        ctx.close();
	}

}
