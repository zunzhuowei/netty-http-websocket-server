package com.hbsoo.game.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by zun.wei on 2021/7/18.
 */
@Data
@Accessors(chain = true)
public class Card implements Serializable {

    /**
     * 卡片名称
     */
    private String name;

    /**
     * 卡片名称描述
     */
    private String desc;

    /**
     * 牌型大小排序
     */
    private byte order;


    private Card(){}

    public Card(String name) {
        this.name = name;
        this.desc = name;
    }


    public static Card card(String name, String desc) {
        return new Card().setName(name).setDesc(desc);
    }

    public static Card card(String name) {
        return new Card().setName(name).setDesc(name);
    }

    public static Card card(String name, int order) {
        return new Card().setName(name).setDesc(name).setOrder((byte) order);
    }

}
