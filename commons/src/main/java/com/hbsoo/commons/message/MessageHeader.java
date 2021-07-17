package com.hbsoo.commons.message;

import lombok.Data;

/**
 * Created by zun.wei on 2021/7/16.
 */
@Data
public final class MessageHeader {

    /**
     * 消息头长度
     */
    public static final short HEADER_LENGTH = 8;

    /**
     * 魔法数据头
     */
    private MagicNum magicNum = MagicNum.COMMON; //= byteBuf.readShort();
    /**
     * 消息长度 = 消息头 + 消息体; 大端序
     */
    private int messageLength; // = byteBuf.readInt();
    /**
     * 消息类型
     */
    private short messageType; // = byteBuf.readShort();


    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength + HEADER_LENGTH;
    }

}
