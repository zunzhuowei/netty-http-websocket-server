package com.hbsoo.protobuf.message;


import com.google.protobuf.GeneratedMessageV3;
import com.hbsoo.protobuf.protocol.WebSocketMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by zun.wei on 2021/7/16.
 */
public interface IWebSocketMessageHandler<T extends GeneratedMessageV3> {


    /**
     * websocket 消息处理
     *
     * @param webSocketMessage 消息
     */
    void handle(ChannelHandlerContext ctx, WebSocketMessage<T> webSocketMessage);


}
