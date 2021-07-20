package com.hbsoo.game.model;

import com.hbsoo.game.holder.GameRoomHallHolder;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

/**
 * Created by zun.wei on 2021/7/18.
 * 游戏用户
 */
@Data
@Accessors(chain = true)
public class User implements Serializable {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 用户头像
     */
    private String icon;

    /**
     * 用户手机号码
     */
    private String phone;

    /**
     * 用户的消息通信管道
     */
    private Channel channel;

    /**
     * 会话秘钥
     */
    private String sessionKey;

    /**
     * 金币数量
     */
    private Long goldNum;

    /**
     * 创建房间
     */
    public Long createRoom(String roomName) {
        long no = new Random().nextLong();
        GameRoom gameRoom = new GameRoom(no, roomName);
        gameRoom.setOwnerId(this.id);
        boolean b = GameRoomHallHolder.createGameRoom(gameRoom);
        if (b) {
            return no;
        }
        return null;
    }

    /**
     * 加入房间
     */
    public void joinRoom(Long roomNo) {
        Player player = new Player(this.id);
        player.setChannel(this.channel)
                .setIcon(this.icon).setName(this.name)
                .setGoldNum(100L);
        final boolean b = GameRoomHallHolder.joinGameRoom(roomNo, player);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
