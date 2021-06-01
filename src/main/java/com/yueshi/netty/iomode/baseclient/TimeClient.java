package com.yueshi.netty.iomode.baseclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

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
    int finalPort = port;

    long t1 = System.currentTimeMillis();

    CompletableFuture.allOf(
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort)),
        CompletableFuture.runAsync(() -> req(finalPort))
    ).join();

    long t2 = System.currentTimeMillis();

    System.out.println("All task spend " + (t2 - t1) + " ms");
  }

  private static long req(int port) {
    long t1 = System.currentTimeMillis();
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
    long t2 = System.currentTimeMillis();
    System.out.println("This req spend time is " + (t2 - t1) + "ms");
    return t2 - t1;
  }
}

