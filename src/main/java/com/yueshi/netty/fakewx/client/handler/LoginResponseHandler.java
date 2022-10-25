package com.yueshi.netty.fakewx.client.handler;

import com.yueshi.netty.fakewx.protocol.request.LoginRequestPacket;
import com.yueshi.netty.fakewx.protocol.response.LoginResponsePacket;
import com.yueshi.netty.fakewx.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Date;

/**
 * @author: yuesh1 create: 2022-10-24 15:47
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(new Date() + ": client is start to login");

		LoginRequestPacket packet = new LoginRequestPacket();
		packet.setPwd("123456");
		packet.setUsername("admin");
		packet.setUserId(1);

		ctx.channel().writeAndFlush(packet);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket msg) throws Exception {
		if (msg.isSuccess()) {
			System.out.println(new Date() + ": login success!");
			LoginUtil.markAsLogin(ctx.channel());
		}
		else {
			System.out.println(new Date() + ": login failure！ The reason is" + msg.getReason());
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("客户端链接被关闭");
	}

}
