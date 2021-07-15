package com.hbsoo.websocket.codec;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Parser;
import com.hbsoo.websocket.protocol.ServerMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

/**
 * Created by zun.wei on 2021/7/15.
 */
public class MyProtobufEncoder extends MessageToMessageEncoder<ServerMessage> {


    @Override
    protected void encode(ChannelHandlerContext ctx, ServerMessage msg, List<Object> out) throws Exception {
        //Parser<? extends GeneratedMessageV3> parserForType = msg.getParserForType();
        GeneratedMessageV3 protobuf = msg.getProtobuf();
        short magicNum = msg.getMagicNum();
        short messageLength = msg.getMessageLength();
        short messageType = msg.getMessageType();
        byte[] bytes = protobuf.toByteArray();
        ByteBuf buffer = Unpooled.buffer(messageLength);
        buffer.writeShort(magicNum).writeShort(buffer.capacity())
                .writeShort(messageType).writeBytes(bytes);
        BinaryWebSocketFrame binaryWebSocketFrame = new BinaryWebSocketFrame(buffer);
        out.add(binaryWebSocketFrame);
    }


}
