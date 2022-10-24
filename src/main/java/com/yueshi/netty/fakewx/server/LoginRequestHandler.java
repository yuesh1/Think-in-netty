package com.yueshi.netty.fakewx.server;

import com.yueshi.netty.fakewx.protocol.PacketCodeC;
import com.yueshi.netty.fakewx.protocol.request.LoginRequestPacket;
import com.yueshi.netty.fakewx.protocol.response.LoginResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Date;

/**
 * @author: yuesh1 create: 2022-10-24 15:27
 */
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) throws Exception {
		LoginResponsePacket responsePacket = login(msg);
		ByteBuf encode = PacketCodeC.INSTANCE.encode(ctx.alloc(), responsePacket);
		ctx.channel().writeAndFlush(encode);
	}

	private LoginResponsePacket login(LoginRequestPacket msg) {
		System.out.println(new Date() + ": 收到客户端登录请求……");
		LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
		loginResponsePacket.setVersion(msg.getVersion());

		if (valid(msg)) {
			loginResponsePacket.setSuccess(true);
			System.out.println("[√√√√] - login success!");
		}
		else {
			loginResponsePacket.setSuccess(false);
			loginResponsePacket.setReason("pwd is wrong!");
			System.out.println("[xxxx] - login failure！");
		}
		return loginResponsePacket;
	}


	private boolean valid(LoginRequestPacket loginRequestPacket) {
		return true;
	}
	}
