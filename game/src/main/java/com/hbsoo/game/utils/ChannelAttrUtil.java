package com.hbsoo.game.utils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * Created by zun.wei on 2021/7/19.
 *  管道中的属性工具类
 */
public final class ChannelAttrUtil {

    /**
     * 用户id
     */
    public static final AttributeKey<Long> ATTR_KEY_USER_ID = AttributeKey.valueOf("userId");

    /**
     *  保存用户id到 channel中
     */
    public static void saveUserId2Channel(Long userId, Channel channel) {
        channel.attr(ATTR_KEY_USER_ID).set(userId);
    }

    /**
     * 从channel 中获取用户id
     */
    public static Long getUserIdFromChannel(Channel channel) {
        return ChannelAttrUtil.getAttribute(channel, ATTR_KEY_USER_ID);
    }

    /**
     * 移除属性
     *
     * @param channel 管道
     * @param key     属性Key
     * @param <T>     类型
     */
    public static <T> void removeAttr(Channel channel, AttributeKey<T> key) {
        channel.attr(key).set(null);
    }

    /**
     * 移除属性
     */
    public static <T> void removeAttr(ChannelHandlerContext ctx, AttributeKey<T> key) {
        removeAttr(ctx.channel(), key);
    }

    /**
     * 获取channel attr 属性
     */
    public static <T> T getAttribute(Channel channel, AttributeKey<T> key) {
        Attribute<T> attr = channel.attr(key);
        return attr.get();
    }

}
