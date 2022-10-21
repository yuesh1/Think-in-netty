package com.yueshi.netty.example.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: yuesh1 create: 2022-10-16 14:37
 */
public class NIOServer {

	/*
	 * NIO模型中通常会有两个线程,每个线程绑定一个selector serverSelector负责轮询是否有新的连接
	 * clientSelector负责轮询连接是否有数据可读
	 *
	 * 检测到有新的连接后，不用绑定一个新线程，而是绑定到clietSelector上
	 *
	 * clientSelector是一个死循环，如果有某一时刻有多个线程可读，即可通过select(1)读取出来
	 *
	 * 数据的读写面向buffer
	 *
	 */
	public static void main(String[] args) throws IOException {
		Selector serverSelector = Selector.open();
		Selector clientSelector = Selector.open();
		// 接受连接
		new Thread(() -> {
			try {
				// 对应IO编程里的服务端启动
				ServerSocketChannel listenerChannel = ServerSocketChannel.open();
				listenerChannel.socket().bind(new InetSocketAddress(8000));
				listenerChannel.configureBlocking(false);
				listenerChannel.register(serverSelector, SelectionKey.OP_ACCEPT);

				while (true) {
					// 监听是否有新的连接， 阻塞时间为1ms
					if (serverSelector.select(1) > 0) {
						Set<SelectionKey> selectionKeys = serverSelector.selectedKeys();
						Iterator<SelectionKey> iterator = selectionKeys.iterator();
						while (iterator.hasNext()) {
							SelectionKey key = iterator.next();
							if (key.isAcceptable()) {
								try {
									// 没进来一个新连接，直接注册到clientSelector上
									SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
									clientChannel.configureBlocking(false);
									clientChannel.register(clientSelector, SelectionKey.OP_READ);
								}
								finally {
									iterator.remove();
								}
							}
						}
					}
				}
			}
			catch (IOException ignore) {
			}
		}).start();

		// 处理读写
		new Thread(() -> {
			try {
				while (true) {
					// 轮询那些连接有数据可读
					if (clientSelector.select(1) > 0) {
						Set<SelectionKey> selectionKeys = clientSelector.selectedKeys();
						Iterator<SelectionKey> iterator = selectionKeys.iterator();
						while (iterator.hasNext()) {
							SelectionKey key = iterator.next();
							if (key.isReadable()) {
								try {
									SocketChannel clientChannel = (SocketChannel) key.channel();
									ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
									// 面向buffer
									clientChannel.read(byteBuffer);
									byteBuffer.flip();
									System.out.println(
											Charset.defaultCharset().newDecoder().decode(byteBuffer).toString());
								}
								finally {
									iterator.remove();
									key.interestOps(SelectionKey.OP_READ);
								}
							}
						}
					}
				}
			}
			catch (IOException ignore) {
			}
		}).start();
	}

}
