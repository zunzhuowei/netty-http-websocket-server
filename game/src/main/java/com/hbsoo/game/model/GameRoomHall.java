package com.hbsoo.game.model;

import io.netty.util.internal.ConcurrentSet;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;
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
    private static final Set<GameRoom> gameRooms = ConcurrentHashMap.newKeySet();


    /**
     * 创建房间
     * @param gameRoom 房间
     * @return 创建成功与否
     */
    public static boolean createGameRoom(GameRoom gameRoom) {
        boolean contains = GameRoomHall.gameRooms.contains(gameRoom);
        if (contains) {
            return false;
        }
        return GameRoomHall.gameRooms.add(gameRoom);
    }


}
