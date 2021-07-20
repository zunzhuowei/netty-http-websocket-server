package com.hbsoo.protobuf.utils;

import com.google.protobuf.GeneratedMessageV3;
import com.hbsoo.protobuf.conf.MessageTypeHandleMapper;
import com.hbsoo.protobuf.message.IWebSocketMessageHandler;
import com.hbsoo.protobuf.protocol.WebSocketMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * Created by zun.wei on 2021/7/16.
 */
@Slf4j
public final class WebSocketMessageHandlerRouter {

    /**
     * 业务处理线程池，用单线程处理，保证线程安全
     */
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
        final Thread thread = new Thread(r);
        thread.setName("handler-thread");
        thread.setUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
        });
        return thread;
    });

    /**
     *  路由处理消息
     */
    public static <T extends GeneratedMessageV3> void handleMessage
    (ChannelHandlerContext ctx, WebSocketMessage<T> webSocketMessage) throws ExecutionException, InterruptedException {
        log.debug("handleMessage webSocketMessage --::{}", webSocketMessage);
        T protobuf = webSocketMessage.getProtobuf();
        IWebSocketMessageHandler messageHandler = MessageTypeHandleMapper.msgRouter.get(protobuf.getClass());
        if (Objects.isNull(messageHandler)) {
            log.warn("protobuf message class for {} message handler is not exist!", protobuf.getClass());
            return;
        }
        final Future<?> future = executorService.submit(() -> messageHandler.handle(ctx, webSocketMessage));
        // 用于抛出异常
        future.get();
    }

}
