package com.hbsoo.websocket.protocol;

import com.google.protobuf.GeneratedMessageV3;
import com.hbsoo.websocket.message.IWebSocketMessageHandler;
import com.hbsoo.websocket.message.UserReqMessageHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by zun.wei on 2021/7/16.
 */
@Slf4j
public final class MessageTypeMapping {

    /**
     * 消息字典
     */
    public static final Map<ProtoBufMessage.MessageType, GeneratedMessageV3> msgMapping = new HashMap<>();
    /**
     * 消息路由字典
     */
    public static final Map<Class<? extends GeneratedMessageV3>, IWebSocketMessageHandler> msgRouter = new HashMap<>();

    /**
     * 初始化消息映射关系
     */
    public static void init() {
        /** 解码器消息类型注册 */
        msgMapping.put(ProtoBufMessage.MessageType.userReq, ProtoBufMessage.UserReq.getDefaultInstance());
        msgMapping.put(ProtoBufMessage.MessageType.userResp, ProtoBufMessage.UserResp.getDefaultInstance());

        /** 消息处理器路由注册 */
        msgRouter.put(ProtoBufMessage.UserReq.class, new UserReqMessageHandler());
    }

}
