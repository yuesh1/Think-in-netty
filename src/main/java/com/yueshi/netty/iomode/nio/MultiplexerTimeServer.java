package com.yueshi.netty.iomode.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * MultiplexerTimeServerHandler
 *
 * @author dengzihui
 * @version 1.0
 * @date 2021/6/11 12:28 AM
 */
public class MultiplexerTimeServer implements Runnable {

  private Selector sel;

  private ServerSocketChannel ssc;

  private volatile boolean stop;

  public MultiplexerTimeServer(int port) {
    try {
      sel = Selector.open();
      ssc = ServerSocketChannel.open();
      ssc.configureBlocking(false);
      ssc.socket().bind(new InetSocketAddress(port), 1024);
      ssc.register(sel, SelectionKey.OP_ACCEPT);
      System.out.println("The time server is start at port :" + port);
    } catch (IOException ioException) {
      ioException.printStackTrace();
      System.exit(1);
    }
  }

  public void stop() {
    this.stop = true;
  }

  @Override
  public void run() {
    while (!stop) {
      try {
        sel.select(1000);
        Set<SelectionKey> keys = sel.selectedKeys();
        Iterator<SelectionKey> iterator = keys.iterator();
        SelectionKey key = null;
        while (iterator.hasNext()) {
          key = iterator.next();
          iterator.remove();
          try {
            handlerInput(key);
          } catch (Exception e) {
            if (key != null) {
              key.channel();
              if (key.channel() != null) {
                key.channel().close();
              }
            }
          }
        }
      } catch (Throwable t) {
        t.printStackTrace();
      }
    }
    // sel 关闭后，所有注册在其上面的Channel和Pipe都会自动 去注册 和 关闭， 所有不需要重复释放资源
    if (sel != null) {
      try {
        sel.close();
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }
    }
  }

  private void handlerInput(SelectionKey key) throws IOException {
    if (key.isValid()) {
      if (key.isAcceptable()) {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel sc = ssc.accept();
        sc.configureBlocking(false);
        sc.register(sel, SelectionKey.OP_READ);
      }
      if (key.isReadable()) {
        SocketChannel sc = (SocketChannel) key.channel();
        ByteBuffer readBuff = ByteBuffer.allocate(1024);
        int readBytes = sc.read(readBuff);
        if (readBytes > 0) {
          readBuff.flip();
          byte[] bytes = new byte[readBuff.remaining()];
          readBuff.get(bytes);
          String body = new String(bytes, StandardCharsets.UTF_8);
          System.out.println("The time server receive order :" + body);
          String currentTime = "QUERY TIME ORDER".equals(body)
              ? new Date(System.currentTimeMillis()).toString()
              : "BAD ORDER";
          doWrite(sc, currentTime);
        } else if (readBytes < 0) {
          key.channel();
          sc.close();
        } else {
          System.out.println("------> READ zero bytes!");
        }
      }
    }
  }

  private void doWrite(SocketChannel sc, String response) throws IOException {
    if (response != null && response.trim().length() > 0) {
      byte[] bytes = response.getBytes();
      ByteBuffer writeBuff = ByteBuffer.allocate(bytes.length);
      writeBuff.put(bytes);
      writeBuff.flip();
      sc.write(writeBuff);
    }
  }
}
