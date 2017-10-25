package com.oxchains.themis.chat.websocket;
import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.service.KafkaService;
import com.oxchains.themis.common.util.JsonUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
<<<<<<< Updated upstream:BackEnd/themis-chat/src/main/java/com/oxchains/themis/chat/websocket/TextWebSocketFrameHandler.java
<<<<<<< HEAD:BackEnd/themis-chat/src/main/java/oxchains/chat/websocket/TextWebSocketFrameHandler.java
=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
>>>>>>> Stashed changes:BackEnd/themis-chat/src/main/java/oxchains/chat/websocket/TextWebSocketFrameHandler.java
import oxchains.chat.common.ChannelHandler;
import oxchains.chat.common.JsonUtil;
import oxchains.chat.common.JwtService;
import oxchains.chat.entity.ChatContent;
<<<<<<< Updated upstream:BackEnd/themis-chat/src/main/java/com/oxchains/themis/chat/websocket/TextWebSocketFrameHandler.java
=======
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.oxchains.themis.chat.common.ChannelHandler;
import com.oxchains.themis.chat.common.ChatUtil;

>>>>>>> b54ef991ebf23b343ec4f70ab27edc8e081f0b78:BackEnd/themis-chat/src/main/java/com/oxchains/themis/chat/websocket/TextWebSocketFrameHandler.java
=======
import oxchains.chat.service.KafkaService;

>>>>>>> Stashed changes:BackEnd/themis-chat/src/main/java/oxchains/chat/websocket/TextWebSocketFrameHandler.java
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
@Component
public class TextWebSocketFrameHandler extends
		SimpleChannelInboundHandler<TextWebSocketFrame> {
<<<<<<< Updated upstream:BackEnd/themis-chat/src/main/java/com/oxchains/themis/chat/websocket/TextWebSocketFrameHandler.java
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private KafkaService kafkaService;
	public TextWebSocketFrameHandler(KafkaService kafkaService){
		this.kafkaService = kafkaService;
	}

=======
	private KafkaService kafkaService;
	public TextWebSocketFrameHandler(KafkaService kafkaService){
    this.kafkaService = kafkaService;
	}
>>>>>>> Stashed changes:BackEnd/themis-chat/src/main/java/oxchains/chat/websocket/TextWebSocketFrameHandler.java
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
<<<<<<< Updated upstream:BackEnd/themis-chat/src/main/java/com/oxchains/themis/chat/websocket/TextWebSocketFrameHandler.java
			channelHandlerMap = ChatUtil.userChannels.get(chatContent.getReceiverId()+"");
=======
			channelHandlerMap = JwtService.userChannels.get(chatContent.getReceiverId()+"");
>>>>>>> Stashed changes:BackEnd/themis-chat/src/main/java/oxchains/chat/websocket/TextWebSocketFrameHandler.java
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
