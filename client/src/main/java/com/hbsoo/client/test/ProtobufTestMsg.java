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

    /**
     * 打印测试 json
     * @param type 消息类型
     * @param message protobuf 消息
     * @throws IOException
     */
    public static void p(String type, Message message) throws IOException {
        String s = ProtoJsonUtils.toJson(message);
        String s1 = s.replaceAll("\n", "");
        MsgTypeBuilder m = new MsgTypeBuilder().setJson(s1).setType(type);
        String string = JSON.toJSONString(m);
        System.out.println(string);
    }

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

    static {
        MsgTypeBuilder.typeBuilderMapping.put("1", ProtoBufMessage.UserReq::newBuilder);
        MsgTypeBuilder.typeBuilderMapping.put("2", GameProtocol.LoginCmd::newBuilder);
        MsgTypeBuilder.typeBuilderMapping.put("3", GameProtocol.CreateRoomCmd::newBuilder);
        MsgTypeBuilder.typeBuilderMapping.put("4", GameProtocol.JoinRoomCmd::newBuilder);
        MsgTypeBuilder.typeBuilderMapping.put("5", GameProtocol.ReadyGameCmd::newBuilder);
    }


    public static void main(String[] args) throws IOException {
        p("1", ProtoBufMessage.UserReq.newBuilder().setUid(111).setJob("coder").build());
        p("2", GameProtocol.LoginCmd.newBuilder().setUid(123).setUserName("张三").setSessionKey("zzzzzzzzzzzz").build());
        p("3", GameProtocol.CreateRoomCmd.newBuilder().setName("这个是房间名称").build());
        p("4", GameProtocol.JoinRoomCmd.newBuilder().setRoomId(110).build());

    }

    /*
{"builder":{},"json":"{\"uid\": 111,\"job\": \"coder\"}","type":"1"}
{"builder":{},"json":"{\"uid\": 123,\"userName\": \"张三\",\"sessionKey\": \"zzzzzzzzzzzz\"}","type":"2"}
{"builder":{},"json":"{\"uid\": 123,\"userName\": \"张三\",\"sessionKey\": \"zzzzzzzzzzzz\"}","type":"2"}
{"builder":{},"json":"{\"uid\": 123,\"userName\": \"张三\",\"sessionKey\": \"zzzzzzzzzzzz\"}","type":"2"}
{"builder":{},"json":"{\"name\": \"这个是房间名称\"}","type":"3"}
{"builder":{},"json":"{\"roomId\": 15882423573324766903}","type":"4"}
     */

    /*
{"builder":{},"json":"{\"uid\": 111,\"userName\": \"张三\",\"sessionKey\": \"11111111111\"}","type":"2"}
{"builder":{},"json":"{\"uid\": 222,\"userName\": \"李四\",\"sessionKey\": \"222222\"}","type":"2"}
{"builder":{},"json":"{\"uid\": 333,\"userName\": \"wangwu\",\"sessionKey\": \"333333\"}","type":"2"}
     */


}
