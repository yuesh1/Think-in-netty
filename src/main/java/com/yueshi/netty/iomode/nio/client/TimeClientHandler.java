package com.yueshi.netty.iomode.nio.client;

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
 * TimeClientHandler
 *
 * @author dengzihui
 * @version 1.0
 * @date 2021/6/11 1:08 AM
 */
public class TimeClientHandler implements Runnable {

  private final String host;

  private final int port;

  private Selector sel;

  private SocketChannel sc;

  private volatile boolean stop;

  public TimeClientHandler(String host, int port) {
    this.host = host == null ? "127.0.0.1" : host;
    this.port = port;
    try {
      sel = Selector.open();
      sc = SocketChannel.open();
      sc.configureBlocking(false);
      System.out.println("The time client is start at port :" + port);
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
    try {
      doConnect();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

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
      SocketChannel sc = (SocketChannel) key.channel();
      if (key.isConnectable()) {
        if (sc.finishConnect()) {
          sc.register(sel, SelectionKey.OP_READ);
          doWrite(sc);
        } else {
          System.exit(1);
        }
      }
      if (key.isReadable()) {
        ByteBuffer readBuff = ByteBuffer.allocate(1024);
        int readBytes = sc.read(readBuff);
        if (readBytes > 0) {
          readBuff.flip();
          byte[] bytes = new byte[readBuff.remaining()];
          readBuff.get(bytes);
          String body = new String(bytes, StandardCharsets.UTF_8);
          System.out.println("Now is :" + body);
          stop();
        } else if (readBytes < 0) {
          key.channel();
          sc.close();
        } else {
          System.out.println("------> READ zero bytes!");
        }
      }
    }
  }


  public void doConnect() throws IOException {
    if (sc.connect(new InetSocketAddress(host, port))) {
      sc.register(sel, SelectionKey.OP_READ);
      doWrite(sc);
    } else {
      sc.register(sel, SelectionKey.OP_CONNECT);
    }
  }

  private void doWrite(SocketChannel sc) throws IOException {
    byte[] req = "QUERY TIME ORDER".getBytes();
    ByteBuffer writeByteBuff = ByteBuffer.allocate(req.length);
    writeByteBuff.put(req);
    writeByteBuff.flip();
    sc.write(writeByteBuff);
    if (!writeByteBuff.hasRemaining()) {
      System.out.println("Send order 2 server success");
    }
  }
}
