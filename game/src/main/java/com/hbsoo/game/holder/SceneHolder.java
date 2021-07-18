package com.hbsoo.game.holder;

import com.hbsoo.game.model.User;
import com.hbsoo.game.scenes.MainScene;

/**
 *  场景操作
 * Created by zun.wei on 2021/7/18.
 */
public final class SceneHolder {

    /** 主场景 */
    private static final MainScene mainScene = new MainScene();

    /**
     *  加入主场景
     * @param user 用户
     */
    public static void joinMainScene(User user) {
        mainScene.join(user);
    }

    /**
     * 移除主场景
     * @param user 用户
     * @return
     */
    public static boolean removeMainScene(User user) {
        return mainScene.remove(user);
    }



}
