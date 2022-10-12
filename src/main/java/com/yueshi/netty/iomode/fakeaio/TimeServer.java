package com.yueshi.netty.iomode.fakeaio;

import com.yueshi.netty.iomode.bio.TimeServerHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TimeServer
 *
 * @author dengzihui
 * @version 1.0
 * @date 2021/6/1 8:40 PM
 */
public class TimeServer {

	public static void main(String[] args) throws IOException {
		int port = 8182;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		ServerSocket server = null;

		try {
			server = new ServerSocket(port);
			System.out.println("The time server is start in port at {" + port + "}");
			Socket socket = null;
			TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(2, 3, 10);
			while (true) {
				socket = server.accept();
				singleExecutor.execute(new TimeServerHandler(socket));
			}
		}
		finally {
			if (server != null) {
				System.out.println("Time server closed!");
				server.close();
				server = null;
			}
		}
	}

}
