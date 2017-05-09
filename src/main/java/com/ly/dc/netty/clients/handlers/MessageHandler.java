package com.ly.dc.netty.clients.handlers;

import com.ly.dc.netty.protobuf.ProtobufClass;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * com.ly.dc.netty.clients.handlers.MessageHandler
 * Created by ymh09658 on 2017/5/9.
 */
public class MessageHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtobufClass.Messages message = (ProtobufClass.Messages) msg;
        if (message.hasResponse1()) {
            String uuid = message.getResponse1().getUuid();
            ProtobufClass.Messages.SecondRequest request = ProtobufClass.Messages.SecondRequest.newBuilder()
                .setMessage("hello").setUuid(uuid).build();
            ctx.writeAndFlush(ProtobufClass.Messages.newBuilder().setRequest2(request).build());
        } else {
            logger.info("收到了message{}", message.getRequest2().getMessage());
        }

    }
}
