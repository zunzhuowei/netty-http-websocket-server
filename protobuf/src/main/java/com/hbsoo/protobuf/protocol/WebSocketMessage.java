package com.hbsoo.protobuf.protocol;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.ProtocolMessageEnum;
import com.hbsoo.commons.message.MagicNum;
import com.hbsoo.commons.message.MessageHeader;
import com.hbsoo.protobuf.conf.MessageTypeHandleMapper;
import lombok.Data;

/**
 * Created by zun.wei on 2021/7/15.
 */
@Data
public class WebSocketMessage<T extends GeneratedMessageV3> {

    // 消息格式 【header + body】
    // 【header】 = magicNum(short) + messageLength(int) + messageType(short);
    // 【body】 = protobufData(byte[])
    //  messageLength = headerLength(8byte) + protobufDataLength(byte[])

    //short magicNum = 0xf9f; //= byteBuf.readShort();
    //short messageLength; // = byteBuf.readShort();
    //short messageType; // = byteBuf.readShort();
    //byte[] datas = new byte[byteBuf.readableBytes()];

    /** 消息头 */
    private final MessageHeader header;
    /** protubuf 消息体 */
    private T protobuf;

    /**
     * 使用默认魔法头数值
     */
    private WebSocketMessage() {
        this.header = new MessageHeader();
    }

    /**
     * 指定魔法头
     * @param magicNum 魔法数值
     */
    public WebSocketMessage(MagicNum magicNum) {
        this.header = new MessageHeader();
        this.header.setMagicNum(magicNum);
    }

    /**
     * 普通消息
     * @param <T> 消息类型
     * @return WebSocketMessage
     */
    public static <T extends GeneratedMessageV3> WebSocketMessage<T> newCommonMessage(Class<T> tClass) {
        return newCommonMessage();
    }
    public static <T extends GeneratedMessageV3> WebSocketMessage<T> newCommonMessage() {
        return new WebSocketMessage<>();
    }
    /**
     * 游戏消息
     * @param <T> 消息类型
     * @return WebSocketMessage
     */
    public static <T extends GeneratedMessageV3> WebSocketMessage<T> newGameMessage(Class<T> tClass) {
        return newGameMessage();
    }
    public static <T extends GeneratedMessageV3> WebSocketMessage<T> newGameMessage() {
        return new WebSocketMessage<>(MagicNum.GAME);
    }

    /**
     * 设置 protubuf 消息对象
     * @param protobufMsg protubuf 消息对象
     */
    public WebSocketMessage<T> setProtobuf(T protobufMsg) {
        this.protobuf = protobufMsg;
        // 设置消息类型
        MessageTypeHandleMapper.msgMapping.forEach((key, value) -> {
            // key protobuf消息枚举类型，value protobuf消息实例
            if (protobufMsg.getClass() == value.getClass()) {
                ProtocolMessageEnum messageEnum = (ProtocolMessageEnum) key;
                this.header.setMessageType((short) messageEnum.getNumber());
            }
        });
        this.header.setMessageLength(this.protobuf.toByteArray().length);
        return this;
    }


}
