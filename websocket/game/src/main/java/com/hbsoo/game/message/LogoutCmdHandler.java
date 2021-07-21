package com.hbsoo.game.message;

import com.hbsoo.game.protocol.GameProtocol;
import com.hbsoo.protobuf.message.IWebSocketMessageHandler;
import com.hbsoo.protobuf.protocol.WebSocketMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by zun.wei on 2021/7/20.
 * 退出登录
 */
@Slf4j
public class LogoutCmdHandler implements IWebSocketMessageHandler<GameProtocol.LogoutCmd> {


    @Override
    public void handle(ChannelHandlerContext ctx, WebSocketMessage<GameProtocol.LogoutCmd> webSocketMessage) {

    }
}
