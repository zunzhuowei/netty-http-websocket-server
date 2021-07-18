package com.hbsoo.game.model;

import io.netty.channel.Channel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by zun.wei on 2021/7/18.
 * 游戏用户
 */
@Data
@Accessors(chain = true)
public class User implements Serializable {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 用户头像
     */
    private String icon;

    /**
     * 用户手机号码
     */
    private String phone;

    /**
     * 用户的消息通信管道
     */
    private Channel channel;


    /**
     * 创建房间
     */
    public void createRoom() {

    }

    /**
     * 加入房间
     */
    public void joinRoom() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(icon, user.icon) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(channel, user.channel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, icon, phone, channel);
    }


}
