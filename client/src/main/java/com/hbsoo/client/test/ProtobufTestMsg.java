package com.hbsoo.client.test;

import com.google.protobuf.GeneratedMessageV3;
import com.hbsoo.client.model.MessageDefinition;
import com.hbsoo.game.protocol.GameProtocol;
import com.hbsoo.websocket.protocol.ProtoBufMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zun.wei on 2021/7/18.
 */
public final class ProtobufTestMsg {

    public static final List<MessageDefinition> messages = new ArrayList<>();

    public static void addMsgs(MessageDefinition... messageDefinitions) {
        messages.addAll(Arrays.asList(messageDefinitions));
    }

    public static <T extends GeneratedMessageV3> MessageDefinition<T> msg(String msgNo, T t) {
        return new MessageDefinition().setMsgNo(msgNo).setMessage(t);
    }

    public static <T extends GeneratedMessageV3> List<T> protobuf(String msgNo) {
        return messages.stream().filter(e -> e.getMsgNo().equals(msgNo.trim()))
                .map(e -> (T) e.getMessage())
                .collect(Collectors.toList());
    }

    static {
        addMsgs(
                msg("1", ProtoBufMessage.UserReq.newBuilder().setUid(111).setJob("coder").build()),
                msg("2", GameProtocol.LoginCmd.newBuilder().setUid(123).setUserName("张三").setSessionKey("zzzzzzzzzzzz").build())
        );
    }




}
