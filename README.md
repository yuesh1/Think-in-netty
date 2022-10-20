# Think-in-netty

> This is repo for learning netty.

## 1、BIO

传统的bio，是指 `jdk1.4` 之前的java的io处理模式。

具体是通过Socket进行通信，然后通过`InputStream`和`OutputStream`进行数据的读写。
其中的socker.accept()是阻塞的，也就是说，当没有客户端连接的时候，会一直阻塞在这里，直到有客户端连接进来。

核心的处理程序，就是while的空循环。

详见 `com.yueshi.netty.fakewx.traditionalio`
一个链接需要一个线程来维护。

1. 线程资源受限
2. 线程切换效率低下
3. 数据的读写都是以字节流为单位

## 2、NIO

> New IO or Non-Block IO

关键是引入了Channel和Buffer。
核心的就是selector，selector是一个多路复用器，
可以监听多个channel的事件，比如连接事件，读写事件等等。
epoll是linux下的多路复用器，select是windows下的多路复用器。
jdk nio 使用 epoll 实现selector。

### 应对线程资源受限
使用selector，一个线程来对应多个链接
一条连接来了之后，现在不创建一个 while 死循环去监听是否有数据可读了，而是直接把这条连接注册到 selector
上，然后，通过检查这个 selector，就可以批量监测出有数据可读的连接，进而读取数据

> 在一家幼儿园里，小朋友有上厕所的需求，小朋友都太小以至于你要问他要不要上厕所，他才会告诉你。幼儿园一共有
100 个小朋友，有两种方案可以解决小朋友上厕所的问题：

> 每个小朋友配一个老师。每个老师隔段时间询问小朋友是否要上厕所，如果要上，就领他去厕所，100 个小朋友就需要
100 个老师来询问，并且每个小朋友上厕所的时候都需要一个老师领着他去上，这就是IO模型，一个连接对应一个线程。

> 所有的小朋友都配同一个老师。这个老师隔段时间询问所有的小朋友是否有人要上厕所，然后每一时刻把所有要上厕所的小朋友批量领到厕所，这就是
NIO 模型，所有小朋友都注册到同一个老师，对应的就是所有的连接都注册到一个线程，然后批量轮询

### 应对线程切换效率低下

IO读是基于字节流，只能读一次，需要自己缓存数据
NIO是面向Buffer的，可以读多次，不需要自己缓存数据，这一切只需要地洞读写指针即可

> 一个线程对应多个连接，那么，这个线程就可以同时处理多个连接的请求，这样就不需要频繁的切换线程了，从而提高了效率。

### 为什么不直接用NIO进行开发
1. NIO过于复杂，编程模型不友好，BUG较多
2. 线程模型需要自己实现 (自定义协议拆包都要自己实现)
3. nio的epoll空轮询会导致cpu100%

## 3、Netty简易介绍
1. Netty底层IO模型随意切换，小小的改动，Netty可以直接从NIO模型变身为IO模型
2. Netty自带的拆包解包，异常检测等机制
3. Netty帮助解决了JDK的NIO的很多BUG
4. Netty底层对线程，selector做了很多的优化，reactor线程模型做到非常高效的并发处理
5. Netty自带各种协议栈，处理各种通用协议不用亲自动手
6. Netty社区活跃
7. Netty经历各大RPC框架，消息中间件，分布式通信中间件的广泛验证，健壮性较好

启动一个Netty服务端，需要具备以下最小三类：
- 线程模型
- IO模型
- 连接处理逻辑

### Netty的核心组件
- Channel/ServerChannel
- EventLoop/EventLoopGroup
- BootStrap/ServerBootStrap
- Feature/ChannelFeature