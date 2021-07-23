package com.hbsoo.groovy.message

import com.hbsoo.game.protocol.GameProtocol
import com.hbsoo.game.utils.MessageUtils
import com.hbsoo.protobuf.message.IWebSocketMessageHandler
import com.hbsoo.protobuf.protocol.WebSocketMessage
import com.hbsoo.protobuf.utils.Broadcaster
import io.netty.channel.ChannelHandlerContext

/**
 * Created by zun.wei on 2021/7/23.
 *
 */
class LogoutCmdHandler implements IWebSocketMessageHandler<GameProtocol.LogoutCmd> {


    LogoutCmdHandler() {
        println "------------------------- groovy logoutCmdHandler -------------------------"
    }

    @Override
    void handle(ChannelHandlerContext ctx, WebSocketMessage<GameProtocol.LogoutCmd> webSocketMessage) {
        println "LogoutCmdHandler groovy handle -- ::" + webSocketMessage
        def message = WebSocketMessage.newGameMessage(GameProtocol.LogoutCmdResp.class)
                .setProtobuf(
                        GameProtocol.LogoutCmdResp.newBuilder()
                                .setResult(
                                        MessageUtils.commonResp(GameProtocol.RespCode.SUCCESS, "ok")
                                ).build()
                )
        Broadcaster.broadcastMessage(message, ctx.channel())
    }

}
