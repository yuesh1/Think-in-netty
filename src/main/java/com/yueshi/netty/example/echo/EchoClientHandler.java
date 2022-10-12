package com.yueshi.netty.example.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * EchoClientHandler
 *
 * @author dengzihui
 * @version 1.0
 * @date 2021/5/26 9:52 PM
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

	private final ByteBuf firstMessage;

	public EchoClientHandler() {
		firstMessage = Unpooled.wrappedBuffer("I am echo message from client".getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(firstMessage);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ctx.write(firstMessage);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		TimeUnit.SECONDS.sleep(3);
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
