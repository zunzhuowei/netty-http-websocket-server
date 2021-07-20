package com.hbsoo.game.message;

import com.hbsoo.game.holder.SceneMainHolder;
import com.hbsoo.game.model.User;
import com.hbsoo.game.protocol.GameProtocol;
import com.hbsoo.game.utils.MessageUtils;
import com.hbsoo.protobuf.exception.GlobalExceptionProcessor;
import com.hbsoo.protobuf.message.IWebSocketMessageHandler;
import com.hbsoo.protobuf.protocol.WebSocketMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Created by zun.wei on 2021/7/20.
 * 加入房间
 */
@Slf4j
public class JoinRoomCmdHandler implements IWebSocketMessageHandler<GameProtocol.JoinRoomCmd> {


    @Override
    public void handle(ChannelHandlerContext ctx, WebSocketMessage<GameProtocol.JoinRoomCmd> webSocketMessage) {
        final GameProtocol.JoinRoomCmd joinRoomCmd = webSocketMessage.getProtobuf();
        final long roomId = joinRoomCmd.getRoomId();
        final Channel channel = ctx.channel();
        final Optional<User> userOptional = SceneMainHolder.getUserFromScene(channel);
        if (!userOptional.isPresent()) {
            throw GlobalExceptionProcessor.getInstance
                    (MessageUtils.commonWebSocketResp(GameProtocol.RespCode.FAIL, "用户不在主场景"));
        }

        final User user = userOptional.get();
        // 创建者加入房间
        user.joinRoom(roomId);

    }

}
