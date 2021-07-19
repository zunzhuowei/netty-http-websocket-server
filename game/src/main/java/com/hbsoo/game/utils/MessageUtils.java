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
     * @param protobufClass
     * @param builder
     * @param buildMessageFun
     * @param <T>
     * @param <B>
     * @return
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
     * @param code
     * @param reason
     * @return
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


}
