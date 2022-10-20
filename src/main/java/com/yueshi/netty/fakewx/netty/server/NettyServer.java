package com.yueshi.netty.fakewx.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * @author: yuesh1 create: 2022-10-16 19:29
 */
public class NettyServer {

	/**
	 *
	 * 启动一个Netty服务端，需要具备以下最小三类： 1、线程模型 2、IO模型 3、连接处理逻辑
	 */
	public static void main(String[] args) {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		// boss group 负责创建新连接 accept新连接的线程组
		// worker 负责读取数据的线程，主要用于读取数据以及业务逻辑处理
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		serverBootstrap.group(bossGroup, workerGroup)
				// 指定线程组模型为NIO
				.channel(NioServerSocketChannel.class)
				// 定义后续每条连接的数据读写，业务处理逻辑
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						// ch.pipeline().addLast(new StringDecoder());
						// ch.pipeline().addLast(new SimpleChannelInboundHandler<String>()
						// {
						// @Override
						// protected void channelRead0(ChannelHandlerContext ctx, String
						// msg) throws Exception {
						// System.out.println(msg);
						// }
						// });
						ch.pipeline().addLast(new FirstServerHandler());
					}
				});
		// serverBootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
		// @Override
		// protected void initChannel(NioSocketChannel ch) throws Exception {
		// System.out.println("启动过程中的的处理程序！");
		// }
		// });
		// 给服务端的Channel（NioServerSocketChannel）制定一些自定义属性，然后可以通过channel的attr()取出这个属性
		serverBootstrap.attr(AttributeKey.newInstance("serverName"), "NettyServer");
		// 给每一条连接指定自定义属性，后续可通过channel.attr()取出该属性
		serverBootstrap.childAttr(AttributeKey.newInstance("clientKey"), "clientValue");
		// 可以给每一条连接设置一些TCP底层相关的属性
		// SO_KEEPALIVE - tcp底层心跳机制，true开启
		// TCP_NODELAY - 表示是否开启Nagle算法，true表示关闭，false表示开启
		// 通俗地说，如果要求高实时性，有数据发送时就马上发送，就关闭，如果需要减少发送次数减少网络交互，就开启。
		serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true).childOption(ChannelOption.TCP_NODELAY, true);
		// 给服务端channel设置一些属性
		// 表示系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
		serverBootstrap.option(ChannelOption.SO_BACKLOG, 8000);
		bind(serverBootstrap, 8000);
	}

	/**
	 * 绑定指定端口，如果失败port递增
	 * @param bootstrap
	 * @param port
	 */
	private static void bind(final ServerBootstrap bootstrap, final int port) {
		bootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
			@Override
			public void operationComplete(Future<? super Void> future) throws Exception {
				if (future.isSuccess()) {
					System.out.println("端口绑定成功！port:" + port);
				}
				else {
					bind(bootstrap, port + 1);
					System.out.println("端口绑定失败！port:" + port);
				}
			}
		});
	}

}
