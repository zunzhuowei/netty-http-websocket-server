package com.hbsoo.websocket.codec;

import com.google.protobuf.GeneratedMessageV3;
import com.hbsoo.websocket.protocol.ProtoBufMessage;
import com.hbsoo.websocket.protocol.ServerMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

/**
 * Created by zun.wei on 2021/7/15.
 */
public class MyProtobufDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {


    @Override
    protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = msg.content();
        short magicNum = byteBuf.readShort();
        short messageLength = byteBuf.readShort();
        short messageType = byteBuf.readShort();

        byte[] datas = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(datas);

        GeneratedMessageV3 messageV3 = null;
        switch (messageType) {
            case ProtoBufMessage.MessageType.userReq_VALUE:
                messageV3 = ProtoBufMessage.UserReq.parseFrom(datas);
                break;
            case ProtoBufMessage.MessageType.userResp_VALUE:
                messageV3 = ProtoBufMessage.UserResp.parseFrom(datas);
                break;
        }
        ServerMessage serverMessage = new ServerMessage();
        serverMessage.setMagicNum(magicNum);
        serverMessage.setMessageLength(messageLength);
        serverMessage.setMessageType(messageType);
        serverMessage.setProtobuf(messageV3);
        out.add(serverMessage);

    }


}
