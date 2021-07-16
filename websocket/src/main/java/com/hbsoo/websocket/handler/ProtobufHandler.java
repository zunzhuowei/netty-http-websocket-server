package com.hbsoo.websocket.handler;

import com.hbsoo.protobuf.protocol.WebSocketMessage;
import com.hbsoo.protobuf.utils.WebSocketMessageHandlerRouter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by zun.wei on 2021/7/15.
 */
@Slf4j
public class ProtobufHandler extends SimpleChannelInboundHandler<WebSocketMessage> {



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        log.info(cause.getMessage());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketMessage msg) throws Exception {
        WebSocketMessageHandlerRouter.handleMessage(ctx, msg);
    }


}
