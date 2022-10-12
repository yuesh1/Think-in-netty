package com.yueshi.netty.example.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.nio.charset.StandardCharsets;

/**
 * HttpHelloWorldServerHandler
 *
 * @author dengzihui
 * @version 1.0
 * @date 2021/5/27 11:31 PM
 */
public class HttpHelloWorldServerHandler extends SimpleChannelInboundHandler<HttpObject> {

	private static final byte[] CONTENT = "Hello World".getBytes(StandardCharsets.UTF_8);

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) {
		if (httpObject instanceof HttpRequest) {
			HttpRequest req = (HttpRequest) httpObject;
			DefaultFullHttpResponse resp = new DefaultFullHttpResponse(req.protocolVersion(), HttpResponseStatus.OK,
					Unpooled.wrappedBuffer(CONTENT));

			resp.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN)
					.setInt(HttpHeaderNames.CONTENT_LENGTH, resp.content().readableBytes());

			ChannelFuture future = channelHandlerContext.write(resp);

		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.flush();
	}

}
