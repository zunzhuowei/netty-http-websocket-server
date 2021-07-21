package com.hbsoo.protobuf.exception;

import com.google.protobuf.GeneratedMessageV3;
import com.hbsoo.protobuf.protocol.WebSocketMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Created by zun.wei on 2021/7/21.
 */
@Slf4j
public final class GlobalExceptionWriter {

    private static final GlobalExceptionWriter writer = new GlobalExceptionWriter();

    public static GlobalExceptionWriter getProcessor() {
        return writer;
    }

    /**
     * 全局异常
     * @param ctx ChannelHandlerContext
     * @param cause 异常
     */
    public void writeGlobalException(ChannelHandlerContext ctx, Throwable cause) {
        writeGlobalException(ctx.channel(), cause);
    }

    /**
     * 全局异常
     * @param channel Channel
     * @param cause 异常
     */
    public void writeGlobalException(Channel channel, Throwable cause) {
        if (cause instanceof GlobalExceptionProcessor) {
            writeGlobalException(channel, (GlobalExceptionProcessor) cause);
            return;
        } else if (cause.getCause() instanceof GlobalExceptionProcessor) {
            writeGlobalException(channel, (GlobalExceptionProcessor) cause.getCause());
            return;
        } else {
            cause.printStackTrace();
        }
    }

    /**
     * 全部异常
     * @param channel
     * @param exception
     */
    private void writeGlobalException(Channel channel, GlobalExceptionProcessor exception) {
        final WebSocketMessage<? extends GeneratedMessageV3> message = exception.getWebSocketMessage();
        if (Objects.isNull(message)) {
            return;
        }
        if (Objects.nonNull(channel)) {
            channel.writeAndFlush(message);
        } else {
            log.warn("writeGlobalException but channel is null! message --::{}", message);
        }
    }


}
