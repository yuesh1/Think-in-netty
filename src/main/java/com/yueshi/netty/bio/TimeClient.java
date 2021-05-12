package com.yueshi.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * TimeClient
 *
 * @author dengzihui
 * @version 1.0
 * @date 2021/5/12 8:02 PM
 */
public class TimeClient {

  public static void main(String[] args) {
    int port = 8182;
    if (args != null && args.length > 0) {
      try {
        port = Integer.parseInt(args[0]);
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
    }
    Socket socket = null;
    BufferedReader in = null;
    PrintWriter out = null;
    try {
      socket = new Socket("127.0.0.1", port);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(), true);
      out.println("QUERY TIME ORDER");
      System.out.println("Send order 2 server succeed.");
      String resp = in.readLine();
      System.out.println("Now is " + resp);
    } catch (Exception e) {

    } finally {
      if (out != null) {
        out.close();
        out = null;
      }
      if (in != null) {
        try {
          in.close();
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
        in = null;
      }
      if (socket != null) {
        try {
          socket.close();
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
        socket = null;
      }
    }
  }
}

