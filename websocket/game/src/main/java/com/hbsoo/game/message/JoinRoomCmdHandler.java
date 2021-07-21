package com.hbsoo.game.message;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.Message;
import com.hbsoo.game.holder.GameRoomHallHolder;
import com.hbsoo.game.holder.SceneMainHolder;
import com.hbsoo.game.model.GameRoom;
import com.hbsoo.game.model.Player;
import com.hbsoo.game.model.User;
import com.hbsoo.game.protocol.GameProtocol;
import com.hbsoo.game.utils.MessageUtils;
import com.hbsoo.protobuf.exception.GlobalExceptionProcessor;
import com.hbsoo.protobuf.message.IWebSocketMessageHandler;
import com.hbsoo.protobuf.protocol.WebSocketMessage;
import com.hbsoo.protobuf.utils.Broadcaster;
import com.hbsoo.protobuf.utils.ProtoJsonUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

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

        // 广播消息给房间中的玩家
        final GameRoom gameRoom = GameRoomHallHolder.getGameRoomFromHallByPlayerId(user.getId());
        if (Objects.nonNull(gameRoom)) {
            final Map<Long, Player> players = gameRoom.getPlayers();
            final List<Channel> channels = players.values().parallelStream().map(Player::getChannel)
                    .collect(Collectors.toList());
            final Channel[] channels1 = channels.toArray(new Channel[0]);

            final GameProtocol.JoinRoomCmdResp.Builder builder = GameProtocol.JoinRoomCmdResp.newBuilder();
            final Collection<Player> playerCollection = gameRoom.getPlayers().values();
            List<GameProtocol.Player> playerList = new ArrayList<>(5);
            for (Player player : playerCollection) {
                final String json = JSON.toJSONString(player);
                final GameProtocol.Player message = ProtoJsonUtils.toProtoBean(GameProtocol.Player.newBuilder(), json);
                playerList.add(message);
            }

            final WebSocketMessage<GameProtocol.JoinRoomCmdResp> msg = WebSocketMessage.newGameMessage(GameProtocol.JoinRoomCmdResp.class)
                    .setProtobuf(
                            builder.setResult(MessageUtils.commonResp(GameProtocol.RespCode.SUCCESS, "ok"))
                                    .setRoomId(roomId)
                                    .setName(gameRoom.getName())
                                    .setRoomer(gameRoom.getOwnerId())
                                    .addAllPlayers(playerList)
                                    .build()
                    );
            Broadcaster.broadcastMessage(msg, channels1);
        }
    }

}
