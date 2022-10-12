package com.yueshi.netty.example.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * EchoServer
 *
 * @author dengzihui
 * @version 1.0
 * @date 2021/5/26 10:09 PM
 */
public class EchoServer {

	public static void main(String[] args) throws InterruptedException {
		NioEventLoopGroup group = new NioEventLoopGroup();
		EchoServerHandler handler = new EchoServerHandler();
		ServerBootstrap bootstrap = new ServerBootstrap();
		try {
			bootstrap.group(group).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							ChannelPipeline pipeline = socketChannel.pipeline();
							pipeline.addLast(new LoggingHandler(LogLevel.INFO));
							pipeline.addLast(handler);
						}
					});
			ChannelFuture sync = bootstrap.bind(8090).sync();
			sync.channel().closeFuture().sync();
		}
		finally {
			group.shutdownGracefully();
		}
	}

}
