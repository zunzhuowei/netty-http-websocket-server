package com.hbsoo.game.scenes;

import com.hbsoo.game.model.User;

import java.io.Serializable;
import java.util.*;

/**
 *  主场景
 * Created by zun.wei on 2021/7/18.
 */
public class MainScene implements Serializable {

    /**
     * 公告
     */
    public LinkedList<String> notices = new LinkedList<>();


    /**
     * 在该场景的玩家
     */
    public Set<User> users = new HashSet<>();


    /**
     * 创建房间
     */
    public boolean createRoom(User user, String roomName) {
        return user.createRoom(roomName);
    }

    /**
     * 加入房间
     */
    public void joinRoom(User user) {
        user.joinRoom();
    }


}
