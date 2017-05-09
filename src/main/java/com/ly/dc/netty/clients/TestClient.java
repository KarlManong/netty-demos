package com.ly.dc.netty.clients;

import com.ly.dc.netty.CloseableEventLoopGroup;
import com.ly.dc.netty.EventLoopGroupAdapter;
import com.ly.dc.netty.clients.handlers.MessageHandler;
import com.ly.dc.netty.protobuf.ProtobufClass;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
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
    private final int port;
    private Logger logger = LoggerFactory.getLogger(TestClient.class);

    private Channel channel;

    public TestClient(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        TestClient client = new TestClient(51143);
        new Thread(client).start();
        Thread.sleep(3000);

        client.login("test123", "test1234");

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
                        ch.pipeline().addLast(new ProtobufVarint32FrameDecoder())
                            .addLast(new ProtobufDecoder(ProtobufClass.Messages.getDefaultInstance()))
                            .addLast(new ProtobufVarint32LengthFieldPrepender())
                            .addLast(new ProtobufEncoder())
                            .addLast(new MessageHandler());
                    }
                });
            ChannelFuture f = bootstrap.connect().sync();
            channel = f.channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            logger.error("Got an exception when client running", e);
        }
    }

    public void login(String username, String password) {
        ProtobufClass.Messages.FirstRequest req = ProtobufClass.Messages.FirstRequest.newBuilder().setUsername(username)
            .setPassword(password).build();
        channel.writeAndFlush(
            ProtobufClass.Messages.newBuilder().setRequest1(req).build()
        );
    }
}
