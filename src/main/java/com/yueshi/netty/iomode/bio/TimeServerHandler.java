package com.yueshi.netty.iomode.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * handler
 *
 * @author dengzihui
 * @version 1.0
 * @date 2021/5/11 9:36 PM
 */
public class TimeServerHandler implements Runnable {

	private Socket socket;

	public TimeServerHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			out = new PrintWriter(this.socket.getOutputStream(), true);
			String currentTime = null;
			String body = null;
			while (true) {
				body = in.readLine();
				if (body == null) {
					break;
				}
				Thread.sleep(3000);
				System.out.println("The Time server receive order :" + body);
				currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)
						? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
				out.println(currentTime);
			}
		}
		catch (IOException | InterruptedException e) {
			if (in != null) {
				try {
					in.close();
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}
				in = null;
			}
			if (out != null) {
				out.close();
				out = null;
			}
			if (this.socket != null) {
				try {
					this.socket.close();
				}
				catch (IOException e2) {
					e2.printStackTrace();
				}
				this.socket = null;
			}
		}
	}

}
