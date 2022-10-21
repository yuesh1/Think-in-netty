package com.yueshi.netty.fakewx.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * @author: yuesh1 create: 2022-10-21 14:05
 */
public class WXClient {

	public static void main(String[] args) {
		Bootstrap bootstrap = new Bootstrap();
		NioEventLoopGroup executors = new NioEventLoopGroup();

		bootstrap.group(executors).channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast(new ClientHandler());
			}
		}).connect("127.0.0.1", 8000).addListener(new GenericFutureListener<Future<? super Void>>() {
			@Override
			public void operationComplete(Future<? super Void> future) throws Exception {
				if (future.isSuccess()) {
					System.out.println("connect success!");
				}
				else {
					System.out.println("connect error!");
				}
			}
		});

	}

}
