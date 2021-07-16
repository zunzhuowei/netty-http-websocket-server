package com.hbsoo.websocket.codec;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Parser;
import com.hbsoo.commons.MessageHeader;
import com.hbsoo.websocket.protocol.MessageTypeMapping;
import com.hbsoo.websocket.protocol.ProtoBufMessage;
import com.hbsoo.websocket.protocol.WebSocketMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * Created by zun.wei on 2021/7/15.
 */
@Slf4j
@ChannelHandler.Sharable
public class MyProtobufDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {

    static {
        MessageTypeMapping.init();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = msg.content();
        int readableBytes = byteBuf.readableBytes();
        if (readableBytes < MessageHeader.HeaderLength) {
            log.warn("error message");
            ctx.channel().closeFuture().sync();
            return;
        }
        byteBuf.getShort(0);//magicNum
        short messageLength = byteBuf.getShort(2);//messageLength
        short messageType = byteBuf.getShort(4);//messageType
        int protoBufLength = messageLength - MessageHeader.HeaderLength;
        if (readableBytes < protoBufLength || messageLength < 0) {
            log.warn("error message 2");
            ctx.channel().closeFuture().sync();
            return;
        }
        ProtoBufMessage.MessageType type = ProtoBufMessage.MessageType.forNumber(messageType);
        if (Objects.isNull(type)) {
            log.warn("error message 3");
            return;
        }
        byteBuf.readShort();//magicNum
        byteBuf.readShort();//messageLength
        byteBuf.readShort();//messageType

        //ProtobufDecoder;
        //ProtobufEncoder;
        byte[] datas = new byte[protoBufLength];
        byteBuf.readBytes(datas);

        GeneratedMessageV3 instance = MessageTypeMapping.msgMapping.get(type);
        if (Objects.isNull(instance)) {
            log.warn("ProtoBufMessage.MessageType [{}] is not register", type);
            msg.release();
            return;
        }
        Parser<? extends GeneratedMessageV3> parserForType = instance.getParserForType();
        GeneratedMessageV3 messageV31 = parserForType.parseFrom(datas);
        WebSocketMessage webSocketMessage = new WebSocketMessage();
        //webSocketMessage.setMessageType(messageType);
        webSocketMessage.setProtobuf(messageV31);
        out.add(webSocketMessage);

    }


}
