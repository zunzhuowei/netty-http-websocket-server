package com.hbsoo.game.message;

import com.hbsoo.game.holder.SceneMainHolder;
import com.hbsoo.game.model.User;
import com.hbsoo.game.protocol.GameProtocol;
import com.hbsoo.game.utils.ChannelAttrUtil;
import com.hbsoo.game.utils.MessageUtils;
import com.hbsoo.protobuf.message.IWebSocketMessageHandler;
import com.hbsoo.protobuf.protocol.WebSocketMessage;
import com.hbsoo.protobuf.utils.Broadcaster;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by zun.wei on 2021/7/19.
 * 创建房间
 */
@Slf4j
public class CreateRoomCmdHandler implements IWebSocketMessageHandler<GameProtocol.CreateRoomCmd> {


    @Override
    public void handle(ChannelHandlerContext ctx, WebSocketMessage<GameProtocol.CreateRoomCmd> webSocketMessage) {
        final GameProtocol.CreateRoomCmd createRoomCmd = webSocketMessage.getProtobuf();
        final String name = createRoomCmd.getName();
        final Channel channel = ctx.channel();
        final Long userId = ChannelAttrUtil.getUserIdFromChannel(channel);

        final Optional<User> userOptional = SceneMainHolder.getUserFromScene(channel);
        if (!userOptional.isPresent()) {
            // 广播消息回用户
            Broadcaster.broadcastMessage(getFailResp("用户不在主场景"), channel);
            return;
        }
        final User user = userOptional.get();
        final Long roomNo = user.createRoom(name);
        if (Objects.isNull(roomNo)) {
            // 广播消息回用户
            Broadcaster.broadcastMessage(getFailResp("创建失败"), channel);
            return;
        }

        // 创建者加入房间
        user.joinRoom(roomNo);

        // 返回创建者
        final WebSocketMessage<GameProtocol.CreateRoomCmdResp> resp
                = MessageUtils.resp(GameProtocol.CreateRoomCmdResp.class,
                GameProtocol.CreateRoomCmdResp.newBuilder(),
                builder -> builder.setResult(MessageUtils.commonResp(GameProtocol.RespCode.SUCCESS))
                        .setRoomId(roomNo).setName(name).setRoomer(userId).build());

        // 广播消息回用户
        Broadcaster.broadcastMessage(resp, channel);
        // 退出主场景
        SceneMainHolder.removeScene(user);

        return;
    }


    /*
    message CreateRoomCmdResp{
  CommonResp result = 1;
  uint64 roomId = 2;
  string name = 3;
  string roomer = 4;
}
     */

    /**
     * 获取失败返回值
     * @param reason 错误理由
     */
    private WebSocketMessage<GameProtocol.CreateRoomCmdResp> getFailResp(String reason) {
        final WebSocketMessage<GameProtocol.CreateRoomCmdResp> message = WebSocketMessage.newGameMessage(GameProtocol.CreateRoomCmdResp.class);
        message.setProtobuf(
                GameProtocol.CreateRoomCmdResp.newBuilder().setResult(
                        GameProtocol.CommonResp.newBuilder().setCode(GameProtocol.RespCode.FAIL)
                                .setMessage(reason)
                                .build()
                ).build()
        );
        return message;
    }

}
