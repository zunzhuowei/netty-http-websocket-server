package com.hbsoo.websocket.protocol;

import com.google.protobuf.GeneratedMessageV3;
import lombok.Data;

/**
 * Created by zun.wei on 2021/7/15.
 */
@Data
public class ServerMessage {

    short magicNum = 0xf9f; //= byteBuf.readShort();
    short messageLength; // = byteBuf.readShort();
    short messageType; // = byteBuf.readShort();
    GeneratedMessageV3 protobuf;

    //byte[] datas = new byte[byteBuf.readableBytes()];

    public void setMessageType(int messageType) {
        this.messageType = (short) messageType;
    }

    public void setProtobuf(GeneratedMessageV3 messageV3) {
        this.protobuf = messageV3;
        this.messageLength = (short) (this.protobuf.toByteArray().length + 6);
    }


}
