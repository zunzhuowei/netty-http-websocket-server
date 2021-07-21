package com.hbsoo.client.sender;

import com.google.protobuf.GeneratedMessageV3;
import com.hbsoo.client.WebSocketClient;
import com.hbsoo.client.test.ProtobufTestMsg;
import com.hbsoo.commons.message.MagicNum;
import com.hbsoo.protobuf.protocol.WebSocketMessage;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zun.wei on 2021/7/16.
 */
public final class SendMessage {


    private final ScheduledThreadPoolExecutor executor;
    private Channel channel;
    private final WebSocketClient.Client oldClient;

    public SendMessage(Channel channel, WebSocketClient.Client client) {
        this.oldClient = client;
        this.channel = channel;
        this.executor = new ScheduledThreadPoolExecutor(1);
        // heartbeat
        this.executor.scheduleAtFixedRate(this::heartbeat, 3, 3, TimeUnit.SECONDS);
    }

    /**
     * 关闭管道
     */
    public void closeChannel() throws InterruptedException {
        channel.writeAndFlush(new CloseWebSocketFrame());
        channel.closeFuture().sync();
    }

    /**
     * 心跳
     */
    public void heartbeat() {
        WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[]{8, 1, 8, 1}));
        channel.writeAndFlush(frame).addListener(new ChannelFutureListener(){
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                // 发送到服务器是否成功，可以判断管道是否还可用
                boolean success = channelFuture.isSuccess();
                if (!success) {
                    final EventLoop loop = channelFuture.channel().eventLoop();
                    loop.schedule(reconnectServer(), 0, TimeUnit.SECONDS);
                }
            }
        });
    }

    /**
     * 重连服务器
     */
    public Runnable reconnectServer() {
        return () -> {
            try {
                // 关闭旧链接
                closeChannel();
                // 打开新链接
                WebSocketClient.Client client = new WebSocketClient.Client();
                client.connect();
                // 创建新管道成功则关闭旧客户端
                oldClient.shutdown();
                // 关闭线程池
                executor.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    /**
     * 发送文本消息
     *
     * @param msg 文本消息
     */
    public void sendTextMsg(String msg) {
        WebSocketFrame frame = new TextWebSocketFrame(msg);
        channel.writeAndFlush(frame);
    }

    /**
     * 发送 protobuf 消息
     *
     * @param protobufMsg protobuf 消息
     * @param magicNum    protobuf 消息类型
     * @param <T>         消息类型
     */
    public <T extends GeneratedMessageV3> void sendProtobufMsg(T protobufMsg, MagicNum magicNum) {
        WebSocketMessage<T> webSocketMessage;
        switch (magicNum) {
            case COMMON: {
                webSocketMessage = WebSocketMessage.newCommonMessage();
                break;
            }
            case GAME: {
                webSocketMessage = WebSocketMessage.newGameMessage();
                break;
            }
            default:
                webSocketMessage = WebSocketMessage.newCommonMessage();
                break;
        }
        webSocketMessage.setProtobuf(protobufMsg);
        channel.writeAndFlush(webSocketMessage);
    }

    /**
     * 发送消息到服务端
     */
    public void sendMsg2Server() {
        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                try {
                    String msg = console.readLine();
                    if (msg == null || msg.trim().equals("")) {
                        break;
                    } else if ("bye".equals(msg.toLowerCase())) {
                        closeChannel();
                        break;
                    } else if ("ping".equals(msg.toLowerCase())) {
                        heartbeat();
                    } else if (msg.toLowerCase().startsWith("text")) {
                        sendTextMsg(msg);
                    } else {
                        GeneratedMessageV3 protobuf = ProtobufTestMsg.protobuf(msg);
                        sendProtobufMsg(protobuf, MagicNum.GAME);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            reconnectServer().run();
        }
    }

}
