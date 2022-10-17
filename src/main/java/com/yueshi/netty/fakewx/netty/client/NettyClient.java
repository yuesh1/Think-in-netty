package com.yueshi.netty.fakewx.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author: yuesh1 create: 2022-10-16 19:37
 */
public class NettyClient {

	public static final int MAX_RETRY = 5;

	public static void main(String[] args) throws InterruptedException {
		Bootstrap bootstrap = new Bootstrap();
		NioEventLoopGroup group = new NioEventLoopGroup();

		// 指定线程模型
		bootstrap.group(group)
				// 指定IO模型
				.channel(NioSocketChannel.class)
				// IO处理逻辑
				.handler(new ChannelInitializer<Channel>() {
					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast(new StringEncoder());
					}
				});

		// 建立连接
		Channel channel = connect(bootstrap, "127.0.0.1", 8000, MAX_RETRY);

		while (true) {
			channel.writeAndFlush(new Date() + ": hello world!");
			Thread.sleep(2000);
		}
	}

	private static Channel connect(final Bootstrap bootstrap, final String host, final int port, int retry)
			throws InterruptedException {
		ChannelFuture f = bootstrap.connect(host, port).addListener(future -> {
			if (future.isSuccess()) {
				System.out.println("连接成功！");
			}
			else if (retry == 0) {
				System.out.println("重试次数消耗完毕1");
			}
			else {
				int order = (MAX_RETRY - retry) + 1;
				int delay = 1 << order;
				System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
				bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay,
						TimeUnit.SECONDS);
			}
		});
		return f.channel();
	}

}
