package com.ly.dc.netty.servers;

import com.ly.dc.netty.CloseableEventLoopGroup;
import com.ly.dc.netty.EventLoopGroupAdapter;
import com.ly.dc.netty.handlers.TestHandler;
import com.ly.dc.netty.utils.DynamicPortFinder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG
*/
public class TestServer implements Runnable {
    private Logger logger = LoggerFactory.getLogger(TestServer.class);

    private final int port;

    private boolean running = false;

    public TestServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new TestServer(DynamicPortFinder.getNextAvailable()));
        thread.start();
    }

    @Override
    public void run() {
        try (CloseableEventLoopGroup bossGroup = new EventLoopGroupAdapter(2);
             CloseableEventLoopGroup workerGroup = new EventLoopGroupAdapter()) {
            logger.info("Server is going to start up at port {}", port);

            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder())
                                    // todo .addLast(decoder())
                                    .addLast(new ProtobufVarint32LengthFieldPrepender())
                                    .addLast(new ProtobufEncoder())
                                    .addLast(new TestHandler());
                        }
                    });

            ChannelFuture f = serverBootstrap.bind(port).sync();
            logger.info("Server starts up");
            running = true;
            f.channel().closeFuture().sync();
            logger.info("Server is shutdown");
        } catch (Exception e) {
            logger.error("Got an exception when server running", e);
        }
    }

    public boolean isRunning() {
        return running;
    }

}
