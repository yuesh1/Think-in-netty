package com.yueshi.netty.fakewx.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author: yuesh1 create: 2022-10-16 19:29
 */
public class NettyServer {

	public static void main(String[] args) {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		// boss group 负责创建新连接
		// worker 负责读取数据的线程，主要用于读取数据以及业务逻辑处理
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline().addLast(new StringDecoder());
						ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
							@Override
							protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
								System.out.println(msg);
							}
						});
					}
				}).bind(8000);
	}

}
