package com.yueshi.netty.example.traditionalio.client;

import java.net.Socket;

/**
 * ioclient
 *
 * @author: yuesh1 create: 2022-10-14 17:44
 * @author yueshi
 * @date 2022/10/14
 */
public class IOClient {

	public static void main(String[] args) throws Exception {
		new Thread(() -> {
			try {
				Socket socket = new Socket("127.0.0.1", 8000);
				while (true) {
					try {
						socket.getOutputStream().write("hello world".getBytes());
						Thread.sleep(2000);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			catch (Exception e) {

			}
		}).start();
	}

}
