package com.hbsoo.game.model;

/**
 * 角色
 * Created by zun.wei on 2021/7/18.
 */
public enum Role {

    /**
     * 农民
     */
    FARMER(0),
    /**
     * 地址
     */
    LANDLORD(1),
    ;

    public byte type;

    Role(int type) {
        this.type = (byte) type;
    }

}
