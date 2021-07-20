package com.hbsoo.protobuf.handler;

import com.google.protobuf.GeneratedMessageV3;
import com.hbsoo.protobuf.exception.GlobalExceptionProcessor;
import com.hbsoo.protobuf.protocol.WebSocketMessage;
import com.hbsoo.protobuf.utils.WebSocketMessageHandlerRouter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Created by zun.wei on 2021/7/15.
 */
@Slf4j
public class ProtobufHandler extends SimpleChannelInboundHandler<WebSocketMessage> {



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof GlobalExceptionProcessor) {
            GlobalExceptionProcessor exception = (GlobalExceptionProcessor) cause;
            final WebSocketMessage<? extends GeneratedMessageV3> message = exception.getWebSocketMessage();
            if (Objects.isNull(message)) {
                return;
            }
            ctx.channel().writeAndFlush(message);
            return;
        }
        cause.printStackTrace();
        log.info(cause.getMessage());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketMessage msg) throws Exception {
        WebSocketMessageHandlerRouter.handleMessage(ctx, msg);
    }


}
