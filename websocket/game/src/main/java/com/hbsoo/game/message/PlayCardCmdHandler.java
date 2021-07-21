package com.hbsoo.game.message;

import com.google.protobuf.ProtocolStringList;
import com.hbsoo.game.protocol.GameProtocol;
import com.hbsoo.protobuf.message.IWebSocketMessageHandler;
import com.hbsoo.protobuf.protocol.WebSocketMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by zun.wei on 2021/7/20.
 * 出牌
 */
@Slf4j
public class PlayCardCmdHandler implements IWebSocketMessageHandler<GameProtocol.PlayCardCmd> {


    @Override
    public void handle(ChannelHandlerContext ctx, WebSocketMessage<GameProtocol.PlayCardCmd> webSocketMessage) {
        final GameProtocol.PlayCardCmd playCardCmd = webSocketMessage.getProtobuf();
        final long roomId = playCardCmd.getRoomId();
        final ProtocolStringList cardsList = playCardCmd.getCardsList();

    }
}
