package com.hbsoo.game.service;

import com.hbsoo.game.model.User;
import io.netty.channel.Channel;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * Created by zun.wei on 2021/7/21.
 */
public interface ILoginService {

    /**
     * 登录
     *
     * @param userName
     * @param password
     * @param userConsumer
     * @throws ExecutionException
     * @throws InterruptedException
     */
    void userLogin(String userName, String password, Channel channel, Consumer<User> userConsumer)
            throws ExecutionException, InterruptedException;


    /**
     * 登出
     * @param userId
     * @throws ExecutionException
     * @throws InterruptedException
     */
    void userLogout(Long userId, Channel channel) throws ExecutionException, InterruptedException;

}
