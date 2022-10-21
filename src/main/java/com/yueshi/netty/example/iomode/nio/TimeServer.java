package com.yueshi.netty.example.iomode.nio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TimeServer
 *
 * @author dengzihui
 * @version 1.0
 * @date 2021/6/11 12:25 AM
 */
public class TimeServer {

	public static void main(String[] args) {
		int port = 8183;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			}
			catch (NumberFormatException e) {
				// ignore
			}
		}

		MultiplexerTimeServer handler = new MultiplexerTimeServer(port);
		new Thread(handler, "NIO-MultiplexerTimeServer-001").start();

		ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 5, 120L, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(1024),
				r -> new Thread(r, "NIO-MultiplexerTimeServerHandler-" + r.hashCode()),
				new ThreadPoolExecutor.DiscardOldestPolicy());
		// executor.execute(handler);

	}

}
