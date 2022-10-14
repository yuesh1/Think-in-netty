package com.yueshi.netty.fakewx.traditionalio.server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * server端
 *
 * @author: yuesh1 create: 2022-10-14 17:21
 */
public class IOServer {

	public static void main(String[] args) throws Exception {
		ServerSocket socket = new ServerSocket(8000);
		new Thread(() -> {
			while (true) {
				try {
					Socket accept = socket.accept();
					new Thread(() -> {
						try {
							byte[] bytes = new byte[1024];
							while (true) {
								int read = accept.getInputStream().read(bytes);
								if (read != -1) {
									System.out.println(new String(bytes, 0, read));
								}
								else {
									break;
								}
							}
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}).start();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
