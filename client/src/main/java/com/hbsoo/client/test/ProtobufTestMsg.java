package com.hbsoo.client.test;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.hbsoo.client.model.MessageDefinition;
import com.hbsoo.game.protocol.GameProtocol;
import com.hbsoo.protobuf.utils.ProtoJsonUtils;
import com.hbsoo.websocket.protocol.ProtoBufMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by zun.wei on 2021/7/18.
 */
public final class ProtobufTestMsg {

//    public static final List<MessageDefinition> messages = new ArrayList<>();
//
//    public static void addMsgs(MessageDefinition... messageDefinitions) {
//        messages.addAll(Arrays.asList(messageDefinitions));
//    }
//
//    public static <T extends GeneratedMessageV3> MessageDefinition<T> msg(String msgNo, T t) {
//        return new MessageDefinition().setMsgNo(msgNo).setMessage(t);
//    }

    /**
     * 获取 protobuf消息
     * @param msg
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T extends GeneratedMessageV3> T protobuf(String msg) throws IOException {
        MsgTypeBuilder msgTypeBuilder = JSON.parseObject(msg, MsgTypeBuilder.class);
        Supplier<? extends Message.Builder> builder = msgTypeBuilder.getBuilder();
        return ProtoJsonUtils.toProtoBean(builder, msgTypeBuilder.getJson());
    }

//    static {
//        addMsgs(
//                msg("1", ),
//                msg("2", )
//        );
//    }

    public static void main(String[] args) throws IOException {
        p("1", ProtoBufMessage.UserReq.newBuilder().setUid(111).setJob("coder").build());
        p("2", GameProtocol.LoginCmd.newBuilder().setUid(123).setUserName("张三").setSessionKey("zzzzzzzzzzzz").build());

    }

    /*
    {"builder":{},"json":"{  \"uid\": \"111\",  \"job\": \"coder\"}","type":"1"}
    {"builder":{},"json":"{  \"uid\": \"123\",  \"userName\": \"张三\",  \"sessionKey\": \"zzzzzzzzzzzz\"}","type":"2"}
     */

    public static void p(String type, Message message) throws IOException {
        String s = ProtoJsonUtils.toJson(message);
        String s1 = s.replaceAll("\n", "");
        MsgTypeBuilder m = new MsgTypeBuilder().setJson(s1).setType(type);
        String string = JSON.toJSONString(m);
        System.out.println(string);
    }


}
