package com.yueshi.netty.discardserver.server;

import com.yueshi.netty.discardserver.handler.DiscardServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 调用程序
 *
 * @author: yuesh1 create: 2022-10-14 15:11
 */
public class DiscardServer {

	private int port;

	public DiscardServer(int port) {
		this.port = port;
	}

	/**
	 * 运行
	 * @throws Exception 异常
	 */
	public void run() throws Exception {
		// NioEventLoopGroup 是用来处理I/O操作的多线程事件循环器，Netty 提供了许多不同的 EventLoopGroup
		// 的实现用来处理不同的传输。在这个例子中我们实现了一个服务端的应用，因此会有2个 NioEventLoopGroup
		// 会被使用。第一个被叫做‘boss’，用来接收进来的连接。第二个被叫做‘worker’，用来处理已经被接收的连接，
		// 一旦‘boss’接收到连接，就会把连接信息注册到‘worker’上。如何知道多少个线程已经被使用，如何映射到已经创建的
		// Channel上都需要依赖于 EventLoopGroup 的实现，并且可以通过构造函数来配置他们的关系。
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			// ServerBootstrap 是一个启动 NIO 服务的辅助启动类。你可以在这个服务中直接使用
			// Channel，但是这会是一个复杂的处理过程，在很多情况下你并不需要这样做。
			ServerBootstrap bootstrap = new ServerBootstrap();
			// 这里我们指定使用 NioServerSocketChannel 类来举例说明一个新的 Channel 如何接收进来的连接。
			bootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
					// 这里的事件处理类经常会被用来处理一个最近的已经接收的 Channel。
					// ChannelInitializer是一个特殊的处理类，他的目的是帮助使用者配置一个新的Channel。
					// 也许你想通过增加一些处理类比如DiscardServerHandler 来配置一个新的 Channel
					// 或者其对应的ChannelPipeline 来实现你的网络程序。
					// 当你的程序变的复杂时，可能你会增加更多的处理类到 pipeline上，然后提取这些匿名类到最顶层的类上。
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) {
							ch.pipeline().addLast(new DiscardServerHandler());
						}
					})
					// 你可以设置这里指定的 Channel 实现的配置参数。我们正在写一个TCP/IP 的服务端，因此我们被允许设置 socket
					// 的参数选项比如tcpNoDelay 和 keepAlive。请参考 ChannelOption 和详细的 ChannelConfig
					// 实现的接口文档以此可以对ChannelOption 的有一个大概的认识。
					.option(ChannelOption.SO_BACKLOG, 128)
					// 你关注过 option() 和 childOption() 吗？
					// option()是提供给NioServerSocketChannel 用来接收进来的连接。
					// childOption() 是提供给由父管
					// ServerChannel 接收到的连接，在这个例子中也是 NioServerSocketChannel。
					.childOption(ChannelOption.SO_KEEPALIVE, true);
			// 绑定端口，开始接收进来的连接
			// 这里我们在机器上绑定了机器所有网卡上的 8080 端口。当然现在你可以多次调用 bind() 方法(基于不同绑定地址)。
			ChannelFuture f = bootstrap.bind(port);
			// 等待服务器 socket 关闭 。
			// 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
			f.channel().closeFuture().sync();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			workGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		else {
			port = 8080;
		}
		try {
			new DiscardServer(port).run();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
