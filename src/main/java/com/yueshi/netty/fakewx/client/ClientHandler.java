package com.yueshi.netty.fakewx.client;

import com.yueshi.netty.fakewx.protocol.PacketCodeC;
import com.yueshi.netty.fakewx.protocol.request.LoginRequestPacket;
import com.yueshi.netty.fakewx.protocol.response.LoginResponsePacket;
import com.yueshi.netty.fakewx.protocol.response.MessageResponsePacket;
import com.yueshi.netty.fakewx.protocol.Packet;
import com.yueshi.netty.fakewx.util.LoginUtil;
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

		ByteBuf byteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), packet);
		ctx.channel().writeAndFlush(byteBuf);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf byteBuf = (ByteBuf) msg;
		Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);
		if (packet instanceof LoginResponsePacket) {
			LoginResponsePacket p = (LoginResponsePacket) packet;
			if (p.isSuccess()) {
				System.out.println(new Date() + ": login success!");
				LoginUtil.markAsLogin(ctx.channel());
			}
			else {
				System.out.println(new Date() + ": login failure！ The reason is" + p.getReason());
			}
		}
		else if (packet instanceof MessageResponsePacket) {
			MessageResponsePacket p = (MessageResponsePacket) packet;
			System.out.println(new Date() + "receive Server message, message is [ " + p.getMessage() + " ]");
		}
	}

}
