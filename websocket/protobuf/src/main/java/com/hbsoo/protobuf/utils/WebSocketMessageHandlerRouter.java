package com.hbsoo.protobuf.utils;

import com.google.protobuf.GeneratedMessageV3;
import com.hbsoo.protobuf.conf.MessageTypeHandleMapper;
import com.hbsoo.protobuf.exception.GlobalExceptionWriter;
import com.hbsoo.protobuf.message.IWebSocketMessageHandler;
import com.hbsoo.protobuf.processor.HandlerThreadProcessor;
import com.hbsoo.protobuf.protocol.WebSocketMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by zun.wei on 2021/7/16.
 */
@Slf4j
public final class WebSocketMessageHandlerRouter {



    /**
     *  路由处理消息
     */
    public static <T extends GeneratedMessageV3> void handleMessage
    (ChannelHandlerContext ctx, WebSocketMessage<T> webSocketMessage) throws ExecutionException, InterruptedException {
        log.debug("handleMessage webSocketMessage --::{}", webSocketMessage);
        T protobuf = webSocketMessage.getProtobuf();
        Set<IWebSocketMessageHandler> messageHandlers = MessageTypeHandleMapper.msgRouter.get(protobuf.getClass());
        if (Objects.isNull(messageHandlers)) {
            log.warn("protobuf message class for {} message handler is not exist!", protobuf.getClass());
            return;
        }
        HandlerThreadProcessor.process(() -> {
            for (IWebSocketMessageHandler messageHandler : messageHandlers) {
                try {
                    messageHandler.handle(ctx, webSocketMessage);
                } catch (Exception e) {
                    GlobalExceptionWriter.getProcessor().writeGlobalException(ctx, e);
                }
            }
        });
    }

}
