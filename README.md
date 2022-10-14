# Think-in-netty

> This is repo for learning netty.


## 1、BIO

传统的bio，是指 `jdk1.4` 之前的java的io处理模式。

具体是通过Socket进行通信，然后通过`InputStream`和`OutputStream`进行数据的读写。
其中的socker.accept()是阻塞的，也就是说，当没有客户端连接的时候，会一直阻塞在这里，直到有客户端连接进来。

核心的处理程序，就是while的空循环。