package com.yueshi.netty.fakewx.client;

import com.yueshi.netty.fakewx.handler.PacketCodeC;
import com.yueshi.netty.fakewx.socket.packet.LoginRequestPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.Date;

/**
 * @author: yuesh1 create: 2022-10-21 14:06
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(new Date() + ": client is start to login");

		LoginRequestPacket packet = new LoginRequestPacket();
		packet.setPwd("123456");
		packet.setUsername("admin");
		packet.setUserId(1);

		PacketCodeC packetCodeC = new PacketCodeC();
		// ByteBuf byteBuf = packetCodeC.encode(packet);
		ByteBuf byteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), packet);
		ctx.channel().writeAndFlush(byteBuf);
	}

}
