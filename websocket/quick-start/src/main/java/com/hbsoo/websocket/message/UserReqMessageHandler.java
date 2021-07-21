package com.hbsoo.websocket.message;

import com.hbsoo.protobuf.message.IWebSocketMessageHandler;
import com.hbsoo.protobuf.protocol.WebSocketMessage;
import com.hbsoo.websocket.protocol.ProtoBufMessage;
import com.hbsoo.protobuf.utils.Broadcaster;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by zun.wei on 2021/7/16.
 */
public class UserReqMessageHandler implements IWebSocketMessageHandler<ProtoBufMessage.UserReq> {


    @Override
    public void handle(ChannelHandlerContext ctx, WebSocketMessage<ProtoBufMessage.UserReq> webSocketMessage) {
        ProtoBufMessage.UserReq protobuf = webSocketMessage.getProtobuf();

        WebSocketMessage<ProtoBufMessage.UserResp> resp = WebSocketMessage.newCommonMessage(ProtoBufMessage.UserResp.class);
        ProtoBufMessage.UserResp.Builder builder = ProtoBufMessage.UserResp.newBuilder();
        builder.setResult(ProtoBufMessage.CommonResp.newBuilder().setCode(ProtoBufMessage.RespCode.success).setMessage("ok").build());
        resp.setProtobuf(builder.build());
        //ctx.channel().writeAndFlush(resp);
        Broadcaster.broadcastMessage(resp, ctx.channel());
    }


}
