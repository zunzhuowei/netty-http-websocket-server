package com.hbsoo.protobuf.codec;

import com.google.protobuf.*;
import com.hbsoo.commons.message.MagicNum;
import com.hbsoo.commons.message.MessageHeader;
import com.hbsoo.protobuf.conf.MessageTypeHandleMapper;
import com.hbsoo.protobuf.protocol.WebSocketMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Created by zun.wei on 2021/7/15.
 */
@Slf4j
@ChannelHandler.Sharable
public class MyProtobufDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {

    //ProtobufDecoder;
    //ProtobufEncoder;

    @Override
    protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = msg.content();
        int readableBytes = byteBuf.readableBytes();
        //判断可读消息长度
        if (readableBytes < MessageHeader.HEADER_LENGTH) {
            log.warn("decode message exception, ByteBuf readableBytes is [{}],there is less than header length[{}]",
                    readableBytes, MessageHeader.HEADER_LENGTH);
            return;
        }
        short magicNum = byteBuf.getShort(0);//magicNum
        int messageLength = byteBuf.getInt(2);//messageLength
        short messageType = byteBuf.getShort(6);//messageType
        //TODO不同的魔法头使用不同的 protobuf 消息类型
        Function<Integer, ? extends ProtocolMessageEnum> messageTypeEnumForNumberFunction
                = MessageTypeHandleMapper.magicNumMsgTypeMap.get(magicNum);
        if (Objects.isNull(messageTypeEnumForNumberFunction)) {
            log.warn("decode message exception,unknow magic number [{}]", magicNum);
            ctx.channel().closeFuture().sync();
            return;
        }
        if (messageLength < 0) {
            log.warn("decode message exception,protoBuf message length less than 0");
            ctx.channel().closeFuture().sync();
            return;
        }
        int protoBufLength = messageLength - MessageHeader.HEADER_LENGTH;
        if (readableBytes < protoBufLength) {
            log.warn("decode message exception,ByteBuf readableBytes is [{}],there is less than protoBuf message length [{}]",
                    readableBytes, protoBufLength);
            return;
        }

        // 查询映射到的枚举消息类型
        ProtocolMessageEnum type = messageTypeEnumForNumberFunction.apply((int) messageType);
        //ProtoBufMessage.MessageType type = ProtoBufMessage.MessageType.forNumber(messageType);
        if (Objects.isNull(type)) {
            log.warn("decode message exception,protoBuf message type [{}] not found", messageType);
            ctx.channel().closeFuture().sync();
            return;
        }
        // 读出缓冲区中的消息头
        byteBuf.readShort();//magicNum
        byteBuf.readInt();//messageLength
        byteBuf.readShort();//messageType

        // 读出 protobuf 消息体
        byte[] datas = new byte[protoBufLength];
        byteBuf.readBytes(datas);

        // 获取消息类型映射到的消息实例
        GeneratedMessageV3 instance = MessageTypeHandleMapper.msgMapping.get(type);
        if (Objects.isNull(instance)) {
            log.warn("ProtoBufMessage.MessageType [{}] is not register", type);
            return;
        }
        // 获取类型解析器
        Parser<? extends GeneratedMessageV3> parserForType = instance.getParserForType();
        // 解析 protobuf 消息体
        GeneratedMessageV3 messageV31 = parserForType.parseFrom(datas);
        WebSocketMessage webSocketMessage = new WebSocketMessage(MagicNum.getMagicNum(magicNum));
        webSocketMessage.setProtobuf(messageV31);
        out.add(webSocketMessage);

    }


}
