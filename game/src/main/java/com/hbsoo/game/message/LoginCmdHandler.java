package com.hbsoo.game.message;

import com.hbsoo.game.holder.SceneHolder;
import com.hbsoo.game.model.User;
import com.hbsoo.game.protocol.GameProtocol;
import com.hbsoo.protobuf.message.IWebSocketMessageHandler;
import com.hbsoo.protobuf.protocol.WebSocketMessage;
import com.hbsoo.protobuf.utils.Broadcaster;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

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
        // 进入主场景
        SceneHolder.joinMainScene(new User().setChannel(ctx.channel()).setId(uid).setName(userName).setSessionKey(sessionKey));

        WebSocketMessage<GameProtocol.LoginCmdResp> resp = WebSocketMessage.newGameMessage(GameProtocol.LoginCmdResp.class);
        GameProtocol.LoginCmdResp.Builder builder = GameProtocol.LoginCmdResp.newBuilder();
        builder.setResult(GameProtocol.CommonResp.newBuilder().setCode(GameProtocol.RespCode.SUCCESS).setMessage("ok").build());
        resp.setProtobuf(builder.build());

        // 广播消息回用户
        Broadcaster.broadcastMessage(resp, ctx.channel());
    }


}
