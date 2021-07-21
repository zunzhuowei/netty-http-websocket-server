package com.hbsoo.game.service;

import com.hbsoo.game.model.User;
import com.hbsoo.game.protocol.GameProtocol;
import com.hbsoo.game.utils.MessageUtils;
import com.hbsoo.protobuf.async.AsyncOperation;
import com.hbsoo.protobuf.exception.GlobalExceptionProcessor;
import io.netty.channel.Channel;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * Created by zun.wei on 2021/7/21.
 *  登录业务层
 */
public class LoginService implements ILoginService {



    @Override
    public void userLogin(String userName, String password, Channel channel, Consumer<User> userConsumer) {
        new AsyncOperation<User>(userConsumer) {
            @Override
            public int getBindId() {
                return userName.charAt(userName.length() - 1);
            }

            @Override
            public void execute() {
                // 异步线程执行db查询
                User user = new User();
                user.setName("db user");
                user.setId(110L);
                this.setResult(user);
            }

            @Override
            public void finish() throws Exception {
                // 拿到db查询结果，主线程处理后续逻辑
                final User result = this.getResult();
                userConsumer.accept(result);
            }
        }.process(channel);
    }

    @Override
    public void userLogout(Long userId, Channel channel) throws ExecutionException, InterruptedException {
        new AsyncOperation<User>() {
            @Override
            public int getBindId() {
                return userId.intValue();
            }

            @Override
            public void execute() {
                // 异步线程执行db查询
                User user = new User();
                this.setResult(user);
            }
        }.process(channel);
    }
}
