package com.yueshi.netty.example.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * EchoClient
 *
 * @author dengzihui
 * @version 1.0
 * @date 2021/5/26 9:47 PM
 */
public class EchoClient {

	public static void main(String[] args) throws InterruptedException {
		NioEventLoopGroup env = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(env).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							ChannelPipeline pipeline = socketChannel.pipeline();
							pipeline.addLast(new LoggingHandler(LogLevel.INFO));
							pipeline.addLast(new EchoClientHandler());
						}
					});

			ChannelFuture connect = bootstrap.connect("127.0.0.1", 8090);
			connect.channel().closeFuture().sync();
		}
		finally {
			env.shutdownGracefully();
		}
	}

}
