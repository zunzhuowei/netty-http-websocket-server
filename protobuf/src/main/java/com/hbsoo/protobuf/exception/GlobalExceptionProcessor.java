package com.hbsoo.protobuf.exception;

import com.google.protobuf.GeneratedMessageV3;
import com.hbsoo.protobuf.protocol.WebSocketMessage;

/**
 * Created by zun.wei on 2021/7/20.
 */
public class GlobalExceptionProcessor extends RuntimeException {


    private WebSocketMessage<? extends GeneratedMessageV3> webSocketMessage;

    public WebSocketMessage<? extends GeneratedMessageV3> getWebSocketMessage() {
        return webSocketMessage;
    }

    public void setWebSocketMessage(WebSocketMessage<? extends GeneratedMessageV3> message) {
        this.webSocketMessage = message;
    }

    /**
     * 获取实例
     * @param webSocketMessage 消息
     * @param <T> protobuf类型
     * @return 全局异常处理类
     */
    public static <T extends GeneratedMessageV3> GlobalExceptionProcessor getInstance(WebSocketMessage<T> webSocketMessage) {
        final GlobalExceptionProcessor processor = new GlobalExceptionProcessor();
        processor.setWebSocketMessage(webSocketMessage);
        return processor;
    }


}
