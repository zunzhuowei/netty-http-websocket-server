package com.hbsoo.client.test;

import com.google.protobuf.GeneratedMessageV3;
import com.hbsoo.commons.message.MagicNum;
import com.hbsoo.game.protocol.GameProtocol;
import com.hbsoo.protobuf.protocol.WebSocketMessage;
import com.hbsoo.websocket.protocol.ProtoBufMessage;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by zun.wei on 2021/7/16.
 */
public class SendMessage {


    /**
     * 发送消息到服务端
     * @param ch 管道
     */
    public static void sendMsg2Server(Channel ch) throws IOException, InterruptedException {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String msg = console.readLine();
            if (msg == null) {
                break;
            } else if ("bye".equals(msg.toLowerCase())) {
                ch.writeAndFlush(new CloseWebSocketFrame());
                ch.closeFuture().sync();
                break;
            } else if ("ping".equals(msg.toLowerCase())) {
                WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[] { 8, 1, 8, 1 }));
                ch.writeAndFlush(frame);
            } else if (msg.toLowerCase().startsWith("bin")) {
                WebSocketMessage<ProtoBufMessage.UserReq> webSocketMessage = WebSocketMessage.newCommonMessage(ProtoBufMessage.UserReq.class);
                ProtoBufMessage.UserReq.Builder builder = ProtoBufMessage.UserReq.newBuilder();
                builder.setUid(111).setJob("coder");
                ProtoBufMessage.UserReq userReq = builder.build();
                webSocketMessage.setProtobuf(userReq);
                ch.writeAndFlush(webSocketMessage);

                WebSocketMessage<GameProtocol.LoginCmd> webSocketMessage2 = WebSocketMessage.newGameMessage(GameProtocol.LoginCmd.class);
                GameProtocol.LoginCmd.Builder builder1 = GameProtocol.LoginCmd.newBuilder();
                builder1.setUid(123).setUserName("张三").setSessionKey("zzzzzzzzzzzz");
                GameProtocol.LoginCmd build = builder1.build();
                webSocketMessage2.setProtobuf(build);
                ch.writeAndFlush(webSocketMessage2);
            } else {
                WebSocketFrame frame = new TextWebSocketFrame(msg);
                ch.writeAndFlush(frame);
            }
        }
    }

}
