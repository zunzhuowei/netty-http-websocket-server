package com.hbsoo.websocket.handler;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Parser;
import com.hbsoo.websocket.protocol.ProtoBufMessage;
import com.hbsoo.websocket.protocol.ServerMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

/**
 * Created by zun.wei on 2021/7/15.
 */
@Slf4j
public class ProtobufHandler extends SimpleChannelInboundHandler<ServerMessage> {



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        log.info(cause.getMessage());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ServerMessage msg) throws Exception {
        //Parser<? extends GeneratedMessageV3> parserForType = msg.getParserForType();
        System.out.println("msg = " + msg);

        ProtoBufMessage.UserResp.Builder builder = ProtoBufMessage.UserResp.newBuilder();
        builder.setResult(ProtoBufMessage.CommonResp.newBuilder().setCode(ProtoBufMessage.RespCode.fail).setMessage("ok").build());
        msg.setProtobuf(builder.build());
        msg.setMessageType(ProtoBufMessage.MessageType.userResp_VALUE);
        ctx.channel().writeAndFlush(msg);
    }


}
