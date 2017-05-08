package com.ly.dc.netty.clients;

import com.ly.dc.netty.CloseableEventLoopGroup;
import com.ly.dc.netty.EventLoopGroupAdapter;
import com.ly.dc.netty.handlers.TestHandler;
import com.ly.dc.netty.utils.DynamicPortFinder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

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
public class TestClient implements Runnable {
    private Logger logger = LoggerFactory.getLogger(TestClient.class);
    private final int port;

    public static void main(String[] args) {
        new Thread(new TestClient(DynamicPortFinder.getNextAvailable())).start();
    }

    public TestClient(int port) {
        this.port = port;
    }


    @Override
    public void run() {
        try (CloseableEventLoopGroup eventLoopGroup = new EventLoopGroupAdapter()) {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)

                    .remoteAddress(new InetSocketAddress("localhost", port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TestHandler());
                        }
                    });

            ChannelFuture f = bootstrap.connect().sync();              //6
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("Got an exception when client running", e);
        }
    }
}
