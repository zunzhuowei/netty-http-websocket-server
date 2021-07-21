package com.hbsoo.game.message;

import com.hbsoo.game.protocol.GameProtocol;
import com.hbsoo.protobuf.message.IWebSocketMessageHandler;
import com.hbsoo.protobuf.protocol.WebSocketMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by zun.wei on 2021/7/20.
 * 准备游戏
 */
@Slf4j
public class ReadyGameCmdHandler implements IWebSocketMessageHandler<GameProtocol.ReadyGameCmd> {


    @Override
    public void handle(ChannelHandlerContext ctx, WebSocketMessage<GameProtocol.ReadyGameCmd> webSocketMessage) {
        final GameProtocol.ReadyGameCmd readyGameCmd = webSocketMessage.getProtobuf();
        final long roomId = readyGameCmd.getRoomId();

    }
}
