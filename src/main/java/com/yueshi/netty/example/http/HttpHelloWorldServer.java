package com.yueshi.netty.example.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * HttpHelloServer
 *
 * @author dengzihui
 * @version 1.0
 * @date 2021/5/27 11:30 PM
 */
public class HttpHelloWorldServer {

  public static void main(String[] args) throws InterruptedException {
    NioEventLoopGroup baseGroup = new NioEventLoopGroup(1);
    NioEventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap.group(baseGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .handler(new LoggingHandler(LogLevel.INFO))
          .childHandler(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
              ChannelPipeline pipeline = channel.pipeline();
              pipeline.addLast(new HttpServerCodec());
              pipeline.addLast(new HttpServerExpectContinueHandler());
              pipeline.addLast(new HttpHelloWorldServerHandler());
            }
          });
      Channel channel = serverBootstrap.bind(8090).sync().channel();
      System.err.println("Open you web browser and navigate to http://127.0.0.1:8090");

      channel.closeFuture().sync();
    } finally {
      baseGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();

    }
  }

}
