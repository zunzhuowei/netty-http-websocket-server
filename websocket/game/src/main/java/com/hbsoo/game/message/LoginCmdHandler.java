package com.hbsoo.game.message;

import com.hbsoo.game.holder.SceneMainHolder;
import com.hbsoo.game.model.User;
import com.hbsoo.game.protocol.GameProtocol;
import com.hbsoo.game.service.ILoginService;
import com.hbsoo.game.service.LoginService;
import com.hbsoo.game.utils.ChannelAttrUtil;
import com.hbsoo.game.utils.MessageUtils;
import com.hbsoo.protobuf.exception.GlobalExceptionProcessor;
import com.hbsoo.protobuf.message.IWebSocketMessageHandler;
import com.hbsoo.protobuf.protocol.WebSocketMessage;
import com.hbsoo.protobuf.utils.Broadcaster;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * Created by zun.wei on 2021/7/16.
 * 登录命令处理器
 */
@Slf4j
public class LoginCmdHandler implements IWebSocketMessageHandler<GameProtocol.LoginCmd> {


    @Override
    public void handle(ChannelHandlerContext ctx, WebSocketMessage<GameProtocol.LoginCmd> webSocketMessage) {
        GameProtocol.LoginCmd loginCmd = webSocketMessage.getProtobuf();
        String sessionKey = loginCmd.getSessionKey();
        long uid = loginCmd.getUid();
        String userName = loginCmd.getUserName();
        final Channel channel = ctx.channel();

        // 测试登录
        ILoginService loginService = new LoginService();
        try {
            loginService.userLogin(userName, userName, channel, user -> {
                final Long id = user.getId();
                final String name = user.getName();

                // 进入主场景
                SceneMainHolder.joinScene(new User().setChannel(channel).setId(uid).setName(userName).setSessionKey(sessionKey));

                WebSocketMessage<GameProtocol.LoginCmdResp> resp = WebSocketMessage.newGameMessage(GameProtocol.LoginCmdResp.class);
                GameProtocol.LoginCmdResp.Builder builder = GameProtocol.LoginCmdResp.newBuilder();
                builder.setResult(GameProtocol.CommonResp.newBuilder().setCode(GameProtocol.RespCode.SUCCESS).setMessage("ok").build());
                resp.setProtobuf(builder.build());

                // 往管道中写入用户id属性
                ChannelAttrUtil.saveUserId2Channel(uid, channel);
                // 广播消息回用户
                Broadcaster.broadcastMessage(resp, channel);
            });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw GlobalExceptionProcessor.getInstance
                    (MessageUtils.commonWebSocketResp(GameProtocol.RespCode.FAIL, "登录失败", e.getMessage()));
        }

    }


}
