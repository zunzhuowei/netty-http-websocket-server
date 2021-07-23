package com.hbsoo.game.utils;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.hbsoo.game.protocol.GameProtocol;
import com.hbsoo.protobuf.protocol.WebSocketMessage;

import java.util.function.Function;

/**
 * Created by zun.wei on 2021/7/19.
 */
public final class MessageUtils {

    /**
     * 获取返回值
     * @param protobufClass protobuf 消息类型
     * @param builder protobuf 消息类型 builder
     * @param buildMessageFun builder 构建函数
     * @param <T> protobuf 消息类型
     * @param <B> protobuf 构建消息类型
     * @return WebSocketMessage<T>
     */
    public static <T extends GeneratedMessageV3, B extends Message.Builder> WebSocketMessage<T>
    resp(Class<T> protobufClass, B builder, Function<B, T> buildMessageFun) {
        final WebSocketMessage<T> message = WebSocketMessage.newGameMessage(protobufClass);
        final T apply = buildMessageFun.apply(builder);
        message.setProtobuf(apply);
        return message;
    }

    /**
     * 获取公共返回值
     * @param code 返回状态码
     * @param reason 理由
     * @return GameProtocol.CommonResp
     */
    public static GameProtocol.CommonResp commonResp(GameProtocol.RespCode code, String... reason) {
        if (reason.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : reason) {
                sb.append(s);
            }
            return GameProtocol.CommonResp.newBuilder().setCode(code)
                    .setMessage(sb.toString())
                    .build();
        }
        return GameProtocol.CommonResp.newBuilder().setCode(code).build();
    }

    /**
     * 公共返回值
     * @param code 返回状态码
     * @param reason 理由
     * @return WebSocketMessage<GameProtocol.CommonResp>
     */
    public static WebSocketMessage<GameProtocol.CommonResp> commonWebSocketResp(GameProtocol.RespCode code, String... reason) {
        final GameProtocol.CommonResp resp = commonResp(code, reason);
        final WebSocketMessage<GameProtocol.CommonResp> message = WebSocketMessage.newGameMessage(GameProtocol.CommonResp.class);
        message.setProtobuf(resp);
        return message;
    }


}
