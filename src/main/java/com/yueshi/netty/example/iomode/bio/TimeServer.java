package com.yueshi.netty.example.iomode.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TimeServer
 *
 * @author dengzihui
 * @version 1.0
 * @date 2021/5/11 9:27 PM
 */
public class TimeServer {

	public static void main(String[] args) throws IOException {
		int port = 8181;
		if (null != args && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			}
			catch (NumberFormatException e) {
				// 不处理
			}
		}
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			System.out.println("Time Server is start at port : " + port);
			Socket socket = null;
			while (true) {
				socket = server.accept();
				new Thread(new TimeServerHandler(socket)).start();
			}
		}
		finally {
			if (null != server) {
				System.out.println("The Time server close");
				server.close();
				server = null;
			}
		}
	}

}
