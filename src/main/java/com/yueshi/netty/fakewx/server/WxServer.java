package com.yueshi.netty.fakewx.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.Date;

/**
 * @author: yuesh1 create: 2022-10-21 14:29
 */
public class WxServer {

	public static void main(String[] args) {
		ServerBootstrap bootstrap = new ServerBootstrap();

		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();

		bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024).childOption(ChannelOption.SO_KEEPALIVE, true)
				.childOption(ChannelOption.TCP_NODELAY, true).childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline().addLast(new ServerHandler());
					}
				}).bind(8000).addListener(future -> {
					if (future.isSuccess()) {
						System.out.println(new Date() + ": 端口 【" + 8000 + "】绑定成功！");
					}
					else {
						System.out.println("绑定失败！");
					}
				});
	}

}
