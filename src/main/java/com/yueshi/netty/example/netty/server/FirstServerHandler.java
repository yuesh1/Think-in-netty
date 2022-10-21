package com.yueshi.netty.example.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author: yuesh1 create: 2022-10-20 12:45
 */
public class FirstServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel active!!!");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf byteBuf = (ByteBuf) msg;
		System.out.println(new Date() + ": Server read data -> " + byteBuf.toString(Charset.forName("utf-8")));
		ctx.channel().writeAndFlush(getByteBuf(ctx));
	}

	private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
		byte[] bytes = "Hello, this is server's reply msg for the client !".getBytes(Charset.forName("utf-8"));
		ByteBuf buffer = ctx.alloc().buffer();
		buffer.writeBytes(bytes);
		return buffer;
	}

}
