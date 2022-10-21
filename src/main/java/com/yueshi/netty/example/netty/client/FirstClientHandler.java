package com.yueshi.netty.example.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author: yuesh1 create: 2022-10-20 12:38
 */
public class FirstClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(new Date() + ": 客户端写出数据");
		ByteBuf byteBuf = getByteBuf(ctx);
		ctx.channel().writeAndFlush(byteBuf);
	}

	private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
		// 获取二进制抽象 ByteBuf
		ByteBuf buffer = ctx.alloc().buffer();

		byte[] bytes = "Hello world".getBytes(Charset.forName("utf-8"));
		buffer.writeBytes(bytes);
		return buffer;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf byteBuf = (ByteBuf) msg;
		System.out.println(new Date() + ": Server reply data -> " + byteBuf.toString(Charset.forName("utf-8")));
	}

}
