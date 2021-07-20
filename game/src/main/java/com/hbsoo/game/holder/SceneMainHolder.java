package com.hbsoo.game.holder;

import com.hbsoo.game.model.User;
import com.hbsoo.game.scenes.MainScene;
import io.netty.channel.Channel;

import java.util.*;

/**
 *  场景操作
 * Created by zun.wei on 2021/7/18.
 */
public final class SceneMainHolder {

    /** 主场景 */
    private static final MainScene mainScene = new MainScene();

    /**
     * 加入主场景
     * @param users 用户
     */
    public static void joinScene(User... users) {
        if (Objects.isNull(users) || users.length < 1) {
            return;
        }
        for (User user : users) {
            final boolean aNull = Objects.isNull(user);
            if (aNull) {
                continue;
            }
            SceneMainHolder.mainScene.users.remove(user.getId(), user);
        }
        for (User user : users) {
            final boolean aNull = Objects.isNull(user);
            if (aNull) {
                continue;
            }
            SceneMainHolder.mainScene.users.put(user.getId(), user);
        }

    }

    /**
     * 移除主场景
     * @param users 用户
     */
    public static boolean removeScene(User... users) {
        for (User user : users) {
            SceneMainHolder.mainScene.users.remove(user.getId(), user);
        }
        return true;
    }

    /**
     * 从主场景中获取用户
     * @param channel 消息管道
     */
    public static Optional<User> getUserFromScene(Channel channel) {
        final Collection<User> values = SceneMainHolder.mainScene.users.values();
        return values.parallelStream().filter(user -> user.getChannel() == channel)
                .findFirst();
    }

    /**
     * 从主场景中获取用户
     * @param userId 用户id
     */
    public static Optional<User> getUserFromScene(Long userId) {
        final User user = SceneMainHolder.mainScene.users.get(userId);
        if (Objects.isNull(user)) {
            return Optional.empty();
        }
        return Optional.of(user);
    }


}
