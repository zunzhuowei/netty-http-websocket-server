package com.hbsoo.websocket.utils;

import com.google.protobuf.GeneratedMessageV3;
import com.hbsoo.websocket.message.IWebSocketMessageHandler;
import com.hbsoo.websocket.conf.MessageTypeHandleMapper;
import com.hbsoo.websocket.protocol.WebSocketMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Created by zun.wei on 2021/7/16.
 */
@Slf4j
public final class WebSocketMessageHandlerRouter {


    /**
     *  路由处理消息
     */
    public static <T extends GeneratedMessageV3> void
    handleMessage(ChannelHandlerContext ctx, WebSocketMessage<T> webSocketMessage) {
        T protobuf = webSocketMessage.getProtobuf();
        IWebSocketMessageHandler messageHandler = MessageTypeHandleMapper.msgRouter.get(protobuf.getClass());
        if (Objects.isNull(messageHandler)) {
            log.warn("protobuf message class for {} message handler is not exist!", protobuf.getClass());
            return;
        }
        log.debug("handleMessage webSocketMessage --::{}", webSocketMessage);
        messageHandler.handle(ctx, webSocketMessage);
    }

}
