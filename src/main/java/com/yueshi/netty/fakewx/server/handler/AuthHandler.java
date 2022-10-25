package com.yueshi.netty.fakewx.server.handler;

import com.yueshi.netty.fakewx.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: yuesh1 create: 2022-10-25 11:33
 */
public class AuthHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (!LoginUtil.hasLogin(ctx.channel())) {
			System.out.println("没有登陆，直接关闭链接！");
			ctx.channel().close();
		}
		else {
			ctx.pipeline().remove(this);
			super.channelRead(ctx, msg);
		}
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		if (LoginUtil.hasLogin(ctx.channel())) {
			System.out.println("当前连接登录验证完毕，无需再次验证, AuthHandler 被移除");
			// super.handlerRemoved(ctx);
		}
		else {
			System.out.println("无登录验证，强制关闭连接!");
		}
	}

}
