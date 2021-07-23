package com.hbsoo.game.message;

import com.hbsoo.game.protocol.GameProtocol;
import com.hbsoo.game.utils.MessageUtils;
import com.hbsoo.protobuf.exception.GlobalExceptionProcessor;
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


    public LogoutCmdHandler() {
        System.out.println("------------------------- java logoutCmdHandler -------------------------");
    }

    @Override
    public void handle(ChannelHandlerContext ctx, WebSocketMessage<GameProtocol.LogoutCmd> webSocketMessage) {
        log.debug("LogoutCmdHandler java handle --::{}", webSocketMessage);
        throw GlobalExceptionProcessor.getInstance
                (MessageUtils.commonWebSocketResp(GameProtocol.RespCode.FAIL, "please use groovy logoutCmdHandler"));
    }
}
