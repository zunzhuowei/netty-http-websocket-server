package com.hbsoo.game.model;

import io.netty.channel.Channel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 玩家
 * Created by zun.wei on 2021/7/18.
 */
@Data
@Accessors(chain = true)
public class Player implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 昵称
     */
    private String name;

    /**
     * 头像
     */
    private String icon;

    /**
     * 通信管道
     */
    private Channel channel;

    /**
     * 金币数量
     */
    private Long goldNum;

    /**
     * 手上的牌
     */
    private List<Card> handCards;

    /**
     * 已打出的牌
     */
    private List<Card> discardCards;


    /**
     * 出牌
     */
    public void playCard(Card card) {

    }

    /**
     * 托管
     */
    public void trusteeshipCard() {

    }

    /**
     * 摸牌
     */
    public void moCard() {

    }

    /**
     * 退出房间
     */
    public void exitRoom() {

    }

    /**
     * 投票退出房间
     */
    public void voteExitRoom() {

    }

    /**
     * 关闭房间
     */
    public void closeRoom() {

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id) &&
                Objects.equals(name, player.name) &&
                Objects.equals(icon, player.icon) &&
                Objects.equals(channel, player.channel) &&
                Objects.equals(goldNum, player.goldNum) &&
                Objects.equals(handCards, player.handCards) &&
                Objects.equals(discardCards, player.discardCards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, icon, channel, goldNum, handCards, discardCards);
    }


}
