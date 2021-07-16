package com.hbsoo.websocket.codec;

import com.google.protobuf.GeneratedMessageV3;
import com.hbsoo.commons.MessageHeader;
import com.hbsoo.websocket.protocol.WebSocketMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

/**
 * Created by zun.wei on 2021/7/15.
 */
@ChannelHandler.Sharable
public class MyProtobufEncoder extends MessageToMessageEncoder<WebSocketMessage> {


    @Override
    protected void encode(ChannelHandlerContext ctx, WebSocketMessage msg, List<Object> out) throws Exception {
        //Parser<? extends GeneratedMessageV3> parserForType = msg.getParserForType();
        GeneratedMessageV3 protobuf = msg.getProtobuf();
        MessageHeader header = msg.getHeader();
        short magicNum = header.getMagicNum();
        short messageLength = header.getMessageLength();
        short messageType = header.getMessageType();
        byte[] bytes = protobuf.toByteArray();
        ByteBuf buffer = Unpooled.buffer(messageLength);
        buffer.writeShort(magicNum).writeShort(messageLength)
                .writeShort(messageType).writeBytes(bytes);
        BinaryWebSocketFrame binaryWebSocketFrame = new BinaryWebSocketFrame(buffer);
        out.add(binaryWebSocketFrame);
    }


}
