package com.yueshi.netty.iomode.nio.client;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TimeClient
 *
 * @author dengzihui
 * @version 1.0
 * @date 2021/6/11 1:07 AM
 */
public class TimeClient {

  public static void main(String[] args) {
    int port = 8183;
    if (args != null && args.length > 0) {
      try {
        port = Integer.parseInt(args[0]);
      } catch (NumberFormatException e) {
        // ignore
      }
    }

    TimeClientHandler handler = new TimeClientHandler("127.0.0.1", port);

    ThreadPoolExecutor executor = new ThreadPoolExecutor(
        2,
        5,
        120L,
        TimeUnit.SECONDS,
        new ArrayBlockingQueue<Runnable>(1024),
        r -> new Thread(r, "NIO-TimeClientHandler-" + r.hashCode()),
        new ThreadPoolExecutor.DiscardOldestPolicy());
    executor.execute(handler);

  }

}
