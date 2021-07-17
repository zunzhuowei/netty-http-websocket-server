package com.hbsoo.commons.message;

/**
 * Created by zun.wei on 2021/7/17.
 */
public enum MagicNum {

    COMMON(Short.MAX_VALUE),
    GAME((short) 32766),
    ;


    public short magicNum;

    MagicNum(short magicNum) {
        this.magicNum = magicNum;
    }

    public static MagicNum getMagicNum(short magicNum) {
        if (GAME.magicNum == magicNum) {
            return GAME;
        } else {
            return COMMON;
        }
    }

}
