package com.yueshi.netty.fakewx.client;

import com.yueshi.netty.fakewx.protocol.PacketCodeC;
import com.yueshi.netty.fakewx.protocol.Spliter;
import com.yueshi.netty.fakewx.protocol.request.MessageRequestPacket;
import com.yueshi.netty.fakewx.server.PacketDecoder;
import com.yueshi.netty.fakewx.server.PacketEncoder;
import com.yueshi.netty.fakewx.util.LoginUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.Date;
import java.util.Scanner;

/**
 * @author: yuesh1 create: 2022-10-21 14:05
 */
public class WXClient {

	public static void main(String[] args) {
		Bootstrap bootstrap = new Bootstrap();
		NioEventLoopGroup executors = new NioEventLoopGroup();

		bootstrap.group(executors).channel(NioSocketChannel.class).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
				.option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new Spliter());
						ch.pipeline().addLast(new PacketDecoder());
						ch.pipeline().addLast(new LoginResponseHandler());
						ch.pipeline().addLast(new MessageResponseHandler());
						ch.pipeline().addLast(new PacketEncoder());
					}
				});
		connect(bootstrap, "127.0.0.1", 8000, 1);

	}

	/**
	 * 连接
	 * @param bootstrap 引导
	 * @param host 宿主
	 * @param port 港口
	 * @param retry 重试
	 */
	private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
		bootstrap.connect(host, port).addListener(future -> {
			if (future.isSuccess()) {
				System.out.println(new Date() + ": 连接成功，启动控制台线程……");
				Channel channel = ((ChannelFuture) future).channel();
				startConsoleThread(channel);
			}
			else {
				System.out.println(new Date() + ": connect failure!");
			}
		});
	}

	private static void startConsoleThread(Channel channel) {
		new Thread(() -> {
			while (!Thread.interrupted()) {
				if (LoginUtil.hasLogin(channel)) {
					System.out.println("Please input message to server");
					Scanner sc = new Scanner(System.in);
					String line = sc.nextLine();

					for (int i = 0; i < 1000; i++) {
						MessageRequestPacket packet = new MessageRequestPacket();
						packet.setMessage(line);
						ByteBuf encode = PacketCodeC.INSTANCE.encode(channel.alloc(), packet);
						channel.writeAndFlush(encode);
					}
				}
			}
		}).start();
	}

}
