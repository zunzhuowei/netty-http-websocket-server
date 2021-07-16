package com.hbsoo.commons;

import lombok.Data;

/**
 * Created by zun.wei on 2021/7/16.
 */
@Data
public final class MessageHeader {

    public static final short HeaderLength = 6;

    private short magicNum = 0xf9f; //= byteBuf.readShort();
    private short messageLength; // = byteBuf.readShort();
    private short messageType; // = byteBuf.readShort();

    public short getMessageLength() {
        return (short) (this.messageLength + HeaderLength);
    }

}
