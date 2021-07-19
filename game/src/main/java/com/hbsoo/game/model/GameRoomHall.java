package com.hbsoo.game.model;

import io.netty.util.internal.ConcurrentSet;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zun.wei on 2021/7/18.
 *  房间大厅
 */
@Data
@Accessors(chain = true)
public class GameRoomHall {

    /**
     * 房间列表
     */
    private static final Map<Long, GameRoom> gameRooms = new ConcurrentHashMap<>();


    /**
     * 创建房间
     * @param gameRoom 房间
     * @return 创建成功与否
     */
    public static boolean createGameRoom(GameRoom gameRoom) {
        final Long no = gameRoom.getNo();
        final boolean b = gameRooms.containsKey(no);
        if (b) {
            return false;
        }
        GameRoomHall.gameRooms.put(no, gameRoom);
        return true;
    }

    /**
     * 加入房间
     * @param roomNo 房间编号
     * @param player 玩家
     * @return
     */
    public static boolean joinGameRoom(Long roomNo, Player player) {
        final GameRoom gameRoom = GameRoomHall.gameRooms.get(roomNo);
        if (Objects.isNull(gameRoom)) {
            return false;
        }
        final byte b = gameRoom.addPlayer(player);
        if (b != 0x00) {
            return  false;
        }
        return true;
    }


}
