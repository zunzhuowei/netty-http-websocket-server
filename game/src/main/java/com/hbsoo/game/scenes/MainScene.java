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
    private LinkedList<String> notices = new LinkedList<>();


    /**
     * 在该场景的玩家
     */
    private Set<User> users = new HashSet<>();

    /**
     * 加入主场景
     * @param users 用户
     */
    public void join(User... users) {
        this.users.addAll(Arrays.asList(users));
    }

    /**
     * 移除主场景
     * @param users 用户
     */
    public boolean remove(User... users) {
        return this.users.removeAll(Arrays.asList(users));
    }

}
