package com.hbsoo.game.holder;

import com.hbsoo.game.model.GameRoom;
import com.hbsoo.game.model.Player;
import com.hbsoo.game.protocol.GameProtocol;
import com.hbsoo.game.utils.MessageUtils;
import com.hbsoo.protobuf.exception.GlobalExceptionProcessor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by zun.wei on 2021/7/18.
 * 房间大厅
 */
public final class GameRoomHallHolder {

    /**
     * 房间列表，key 房间编号，value 房间
     */
    private static final Map<Long, GameRoom> gameRooms = new ConcurrentHashMap<>();

    /**
     * 玩家id与房间编号映射字段，key 玩家id，value 房间编号
     */
    private static final Map<Long, Long> playerIdRoomIdMap = new ConcurrentHashMap<>();

    /**
     * 创建房间
     *
     * @param gameRoom 房间
     * @return 创建成功与否
     */
    public static boolean createGameRoom(GameRoom gameRoom) {
        final Long no = gameRoom.getNo();
        final boolean b = gameRooms.containsKey(no);
        if (b) {
            throw GlobalExceptionProcessor.getInstance
                    (MessageUtils.commonWebSocketResp(GameProtocol.RespCode.FAIL, "房间已经存在了！"));
        }
        GameRoomHallHolder.gameRooms.put(no, gameRoom);
        return true;
    }

    /**
     * 加入房间
     *
     * @param roomNo 房间编号
     * @param player 玩家
     * @return
     */
    public static boolean joinGameRoom(Long roomNo, Player player) {
        final GameRoom gameRoom = GameRoomHallHolder.gameRooms.get(roomNo);
        // 判断房间是否存在
        if (Objects.isNull(gameRoom)) {
            throw GlobalExceptionProcessor.getInstance
                    (MessageUtils.commonWebSocketResp(GameProtocol.RespCode.FAIL, "房间不存在"));
        }
        //判断玩家是否已经再其他房间中
        final Long id = player.getId();
        final GameRoom room = getGameRoomFromHallByPlayerId(id);
        if (Objects.isNull(room)) {
            boolean b = gameRoom.addPlayer(player);
            playerIdRoomIdMap.put(player.getId(), roomNo);
            return true;
        }
        if (Objects.equals(room.getNo(), roomNo)) {
            throw GlobalExceptionProcessor.getInstance
                    (MessageUtils.commonWebSocketResp(GameProtocol.RespCode.FAIL, "您已经在房间中了"));
        } else {
            throw GlobalExceptionProcessor.getInstance
                    (MessageUtils.commonWebSocketResp(GameProtocol.RespCode.FAIL, "您已经在其他房间中了"));
        }
    }


    /**
     * 解散房间
     * @param roomNo 房间编号
     * @return
     */
    public static boolean releaseRoom(Long roomNo) {
        final List<Long> removeKeys = playerIdRoomIdMap.entrySet().parallelStream()
                .filter(entry -> {
                    final Long value = entry.getValue();
                    return Objects.equals(value, roomNo);
                }).map(Map.Entry::getKey).collect(Collectors.toList());
        for (Long removeKey : removeKeys) {
            playerIdRoomIdMap.remove(removeKey);
        }
        gameRooms.remove(roomNo);
        return true;
    }

    /**
     * 根据玩家查询玩家所属游戏大厅中的房间
     * @param player 玩家
     * @return 游戏房间；null 表示不再房间中
     */
    public static GameRoom getGameRoomFromHallByPlayer(Player player) {
        if (Objects.isNull(player)) {
            return null;
        }
        final Long id = player.getId();
        return getGameRoomFromHallByPlayerId(id);
    }

    /**
     * 根据玩家查询玩家所属游戏大厅中的房间
     * @param playerId 玩家Id
     * @return 游戏房间；null 表示不再房间中
     */
    public static GameRoom getGameRoomFromHallByPlayerId(Long playerId) {
        final Long roomId = playerIdRoomIdMap.get(playerId);
        if (Objects.isNull(roomId)) {
            return null;
        }
        return GameRoomHallHolder.gameRooms.get(roomId);
    }

}
