package com.hbsoo.protobuf.utils;

import com.google.protobuf.GeneratedMessageV3;
import com.hbsoo.protobuf.protocol.WebSocketMessage;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.DefaultEventExecutor;

import java.util.Arrays;
import java.util.Objects;

/**
 * 广播器
 * Created by zun.wei on 2021/7/16.
 */
public final class Broadcaster {

    /** 全服管道 */
    private static final ChannelGroup allChannel = new DefaultChannelGroup(new DefaultEventExecutor());

    /**
     * 订阅全服广播
     * @param channels 管道
     */
    public static void subscribe(Channel... channels) {
        allChannel.addAll(Arrays.asList(channels));
    }

    /**
     * 取消订阅全服广播
     * @param channels 管道
     */
    public static void unsubscribe(Channel... channels) {
        allChannel.removeAll(Arrays.asList(channels));
    }

    /**
     *  全服广播消息
     * @param webSocketMessage 消息
     * @param <T> google protobuf 消息类型
     */
    public static <T extends GeneratedMessageV3> void broadcastMessage(WebSocketMessage<T> webSocketMessage) {
        allChannel.writeAndFlush(webSocketMessage);
    }

    /**
     * 指定消息管道 广播消息
     * @param webSocketMessage 消息
     * @param channels 发送的管道
     * @param <T> google protobuf 消息类型
     */
    public static <T extends GeneratedMessageV3> void broadcastMessage(WebSocketMessage<T> webSocketMessage, Channel... channels) {
        for (Channel channel : channels) {
            Channel ch = allChannel.find(channel.id());
            if (Objects.nonNull(ch)) {
                ch.writeAndFlush(webSocketMessage);
            }
        }
    }

}
