package com.ly.dc.netty.servers.handlers;

import com.ly.dc.netty.protobuf.ProtobufClass;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * com.ly.dc.netty.servers.handlers.MessageHandler
 * Created by ymh09658 on 2017/5/9.
 */
public class MessageHandler extends ChannelInboundHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    private final Cache<String, String> userCache = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES)
        .build();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtobufClass.Messages req = (ProtobufClass.Messages) msg;
        if (req.hasRequest1()) {
            ProtobufClass.Messages.FirstRequest request1 = req.getRequest1();
            logger.info("{} is login", request1.getUsername());
            String uuid = UUID.randomUUID().toString();
            userCache.put(uuid, request1.getUsername());
            ProtobufClass.Messages.FirstResponse response1 = ProtobufClass.Messages.FirstResponse.newBuilder()
                .setUuid(uuid).build();

            ctx.writeAndFlush(ProtobufClass.Messages.newBuilder().setResponse1(response1).build());
        } else if (req.hasRequest2()) {
            ProtobufClass.Messages.SecondRequest request2 = req.getRequest2();
            String user = userCache.getIfPresent(request2.getUuid());
            ProtobufClass.Messages.SecondResponse response2;
            if (user == null) {
                response2 = ProtobufClass.Messages.SecondResponse.newBuilder()
                    .setMessage("you are dead!").build();

            } else {
                response2 = ProtobufClass.Messages.SecondResponse.newBuilder()
                    .setMessage("hello " + user).build();
            }
            ctx.writeAndFlush(ProtobufClass.Messages.newBuilder().setResponse2(response2).build());
        } else {
            ctx.writeAndFlush(
                ProtobufClass.Messages.newBuilder().setResponse2(ProtobufClass.Messages.SecondResponse.newBuilder()
                    .setMessage("not supported!").build()).build());
        }

    }

}
