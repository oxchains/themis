package com.oxchains.themis.chat.websocket;
import com.oxchains.common.util.JsonUtil;
import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.service.KafkaService;
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
import com.oxchains.themis.chat.common.ChannelHandler;
import com.oxchains.themis.chat.common.ChatUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
public class TextWebSocketFrameHandler extends
		SimpleChannelInboundHandler<TextWebSocketFrame> {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private KafkaService kafkaService;
	public TextWebSocketFrameHandler(KafkaService kafkaService){
		this.kafkaService = kafkaService;
	}

	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
								TextWebSocketFrame msg) throws Exception {
		ChatContent chatContent= (ChatContent) JsonUtil.fromJson(msg.text(), ChatContent.class);

		Map<String,ChannelHandler> channelHandlerMap = ChatUtil.userChannels.get(chatContent.getSenderId()+"");
		String keyIDs = ChatUtil.getIDS(chatContent.getSenderId().toString(),chatContent.getReceiverId().toString());
		if(chatContent.getMsgType()==2){
			ChannelHandler channelHandler = channelHandlerMap.get(keyIDs);
			if(channelHandler!=null){
				channelHandler.setLastUseTime(System.currentTimeMillis());
				chatContent.setStatus("success");
			}
			else{
				chatContent.setStatus("error");
			}
			channelHandler.getChannel().writeAndFlush(new TextWebSocketFrame(JsonUtil.toJson(chatContent)));
		}else if(chatContent.getMsgType()==1){
			chatContent.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			chatContent.setChatId(keyIDs);
			String message = JsonUtil.toJson(chatContent).toString();
			kafkaService.send(message);
			ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
			channelHandlerMap = ChatUtil.userChannels.get(chatContent.getReceiverId()+"");
			if( channelHandlerMap!= null && channelHandlerMap.get(keyIDs)!=null){
				channelHandlerMap.get(keyIDs).getChannel().writeAndFlush(new TextWebSocketFrame(message));
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
