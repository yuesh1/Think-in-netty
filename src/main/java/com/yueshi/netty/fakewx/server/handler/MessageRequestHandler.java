package com.yueshi.netty.fakewx.server.handler;

import com.yueshi.netty.fakewx.codec.PacketCodeC;
import com.yueshi.netty.fakewx.protocol.request.MessageRequestPacket;
import com.yueshi.netty.fakewx.protocol.response.MessageResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Date;

/**
 * @author: yuesh1 create: 2022-10-24 15:28
 */
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket msg) throws Exception {
		MessageResponsePacket responsePacket = receiveRequest(msg);
		ByteBuf encode = PacketCodeC.INSTANCE.encode(ctx.alloc(), responsePacket);
		ctx.channel().writeAndFlush(encode);
	}

	private MessageResponsePacket receiveRequest(MessageRequestPacket msg) {
		System.out.println(new Date() + "receive client message : " + msg.getMessage());

		MessageResponsePacket resp = new MessageResponsePacket();
		System.out.println(new Date() + ": 收到客户端消息: " + msg.getMessage());
		resp.setMessage("server receive message: [" + msg.getMessage() + " ]");
		return resp;
	}

}
