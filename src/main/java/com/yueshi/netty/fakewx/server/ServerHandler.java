package com.yueshi.netty.fakewx.server;

import com.yueshi.netty.fakewx.handler.PacketCodeC;
import com.yueshi.netty.fakewx.socket.packet.LoginRequestPacket;
import com.yueshi.netty.fakewx.socket.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: yuesh1 create: 2022-10-21 14:33
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf byteBuf = (ByteBuf) msg;
		Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);
		if (packet instanceof LoginRequestPacket) {
			LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
			if (vaild(loginRequestPacket)) {
				System.out.println("[√√√√] - login success!");
			}
			else {
				System.out.println("[xxxx] - login failure！");
			}
		}
	}

	private boolean vaild(LoginRequestPacket loginRequestPacket) {
		return true;
	}

}
