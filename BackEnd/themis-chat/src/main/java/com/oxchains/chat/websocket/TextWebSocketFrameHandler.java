package com.oxchains.chat.websocket;


import com.oxchains.chat.common.ChatContent;
import com.oxchains.chat.common.JsonUtil;
import com.oxchains.chat.common.JwtService;
import com.oxchains.chat.common.KafkaUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TextWebSocketFrameHandler extends
		SimpleChannelInboundHandler<TextWebSocketFrame> {
	private Logger LOG = LoggerFactory.getLogger(this.getClass());
	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			TextWebSocketFrame msg) throws Exception {

		ChatContent chatContent= (ChatContent) JsonUtil.fromJson(msg.text(), ChatContent.class);
		if(chatContent.getId()!=null){
			JwtService.userChannels.get(chatContent.getId()).setLastUseTime(System.currentTimeMillis());
		}else{
			chatContent.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			String message = JsonUtil.toJson(chatContent).toString();
			KafkaUtil.send(message);
			ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
			if(JwtService.userChannels.get(chatContent.getDid()+"") != null){
				JwtService.userChannels.get(chatContent.getDid()+"").getChannel().writeAndFlush(new TextWebSocketFrame(message));
			}
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
