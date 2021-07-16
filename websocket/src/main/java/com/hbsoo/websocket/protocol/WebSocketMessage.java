package com.hbsoo.websocket.protocol;

import com.google.protobuf.GeneratedMessageV3;
import com.hbsoo.commons.message.MessageHeader;
import com.hbsoo.websocket.conf.MessageTypeHandleMapper;
import lombok.Data;

/**
 * Created by zun.wei on 2021/7/15.
 */
@Data
public class WebSocketMessage<T extends GeneratedMessageV3> {

    //short magicNum = 0xf9f; //= byteBuf.readShort();
    //short messageLength; // = byteBuf.readShort();
    //short messageType; // = byteBuf.readShort();

    private final MessageHeader header;
    T protobuf;

    //byte[] datas = new byte[byteBuf.readableBytes()];

    public WebSocketMessage() {
        this.header = new MessageHeader();
    }

    /*public void setMessageType(ProtoBufMessage.MessageType messageType) {
        this.header.setMessageType((short) messageType.getNumber());
    }*/

    private void setMessageLength(short messageLength) {}

    public void setProtobuf(T protobufMsg) {
        this.protobuf = protobufMsg;
        MessageTypeHandleMapper.msgMapping.forEach((key, value) -> {
            if (protobufMsg.getClass() == value.getClass()) {
                this.header.setMessageType((short) key.getNumber());
            }
        });
        this.header.setMessageLength((short) (this.protobuf.toByteArray().length));
    }


}
