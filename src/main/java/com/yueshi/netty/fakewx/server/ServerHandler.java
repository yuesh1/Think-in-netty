package com.yueshi.netty.fakewx.server;

import com.yueshi.netty.fakewx.protocol.PacketCodeC;
import com.yueshi.netty.fakewx.protocol.request.LoginRequestPacket;
import com.yueshi.netty.fakewx.protocol.response.LoginResponsePacket;
import com.yueshi.netty.fakewx.protocol.request.MessageRequestPacket;
import com.yueshi.netty.fakewx.protocol.response.MessageResponsePacket;
import com.yueshi.netty.fakewx.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.Date;

/**
 * @author: yuesh1 create: 2022-10-21 14:33
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf byteBuf = (ByteBuf) msg;
		Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);
		if (packet instanceof LoginRequestPacket) {
			System.out.println(new Date() + ": 收到客户端登录请求……");
			LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;

			LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
			loginResponsePacket.setVersion(packet.getVersion());

			if (valid(loginRequestPacket)) {
				loginResponsePacket.setSuccess(true);
				System.out.println("[√√√√] - login success!");
			}
			else {
				loginResponsePacket.setSuccess(false);
				loginResponsePacket.setReason("pwd is wrong!");
				System.out.println("[xxxx] - login failure！");
			}
			ByteBuf encode = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginResponsePacket);
			ctx.channel().writeAndFlush(encode);
		}
		else if (packet instanceof MessageRequestPacket) {
			MessageRequestPacket p = (MessageRequestPacket) packet;
			System.out.println(new Date() + "receive client message : " + p.getMessage());

			MessageResponsePacket resp = new MessageResponsePacket();
			System.out.println(new Date() + ": 收到客户端消息: " + p.getMessage());
			resp.setMessage("server receive message: [" + p.getMessage() + " ]");
			ByteBuf encode = PacketCodeC.INSTANCE.encode(ctx.alloc(), resp);
			ctx.channel().writeAndFlush(encode);
		}
	}

	private boolean valid(LoginRequestPacket loginRequestPacket) {
		return true;
	}

}
