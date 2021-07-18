package com.hbsoo.game.holder;

import com.hbsoo.game.model.User;
import com.hbsoo.game.scenes.MainScene;

import java.nio.channels.Channel;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 *  场景操作
 * Created by zun.wei on 2021/7/18.
 */
public final class SceneHolder {

    /** 主场景 */
    private static final MainScene mainScene = new MainScene();

    /**
     * 加入主场景
     * @param users 用户
     */
    public static void joinMainScene(User... users) {
        List<User> list = Arrays.asList(users);
        SceneHolder.mainScene.users.removeAll(list);
        SceneHolder.mainScene.users.addAll(list);
    }

    /**
     * 移除主场景
     * @param users 用户
     */
    public static boolean removeMainScene(User... users) {
        return SceneHolder.mainScene.users.removeAll(Arrays.asList(users));
    }

    /**
     * 从主场景中获取用户
     * @param channel 消息管道
     */
    public static Optional<User> getUserFromMainScene(Channel channel) {
        for (User user : SceneHolder.mainScene.users) {
            if (user.getChannel() == channel) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

}
