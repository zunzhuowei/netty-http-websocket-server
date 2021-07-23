package com.hbsoo.protobuf.codec;

import com.google.protobuf.GeneratedMessageV3;
import com.hbsoo.commons.message.MessageHeader;
import com.hbsoo.protobuf.protocol.WebSocketMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by zun.wei on 2021/7/15.
 */
@Slf4j
@ChannelHandler.Sharable
public class MyProtobufEncoder extends MessageToMessageEncoder<WebSocketMessage> {


    @Override
    protected void encode(ChannelHandlerContext ctx, WebSocketMessage msg, List<Object> out) throws Exception {
        GeneratedMessageV3 protobuf = msg.getProtobuf();
        MessageHeader header = msg.getHeader();
        short magicNum = header.getMagicNum().magicNum;
        int messageLength = header.getMessageLength();
        short messageType = header.getMessageType();
        byte[] bytes = protobuf.toByteArray();
        ByteBuf buffer = Unpooled.buffer(messageLength);
        buffer.writeShort(magicNum).writeInt(messageLength)
                .writeShort(messageType).writeBytes(bytes);
        BinaryWebSocketFrame binaryWebSocketFrame = new BinaryWebSocketFrame(buffer);
        log.trace("MyProtobufEncoder encode --::{}", binaryWebSocketFrame);
        out.add(binaryWebSocketFrame);
    }


}
