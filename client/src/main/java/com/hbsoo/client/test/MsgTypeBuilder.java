package com.hbsoo.client.test;

import com.google.protobuf.Message;
import com.hbsoo.game.protocol.GameProtocol;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by zun.wei on 2021/7/18.
 */
@Data
@Accessors(chain = true)
public final class MsgTypeBuilder {

    /**
     * 消息类型与Protobuf 消息构建者函数字典
     */
    public static final Map<String, Supplier<? extends Message.Builder>> typeBuilderMapping = new HashMap<>();

    static {
        //typeBuilderMapping.put("1", GameProtocol.LoginCmd::newBuilder);
        //typeBuilderMapping.put("2", GameProtocol.LoginCmdResp::newBuilder);
    }

    /**
     * 消息类型
     */
    private String type;

    /**
     * protobuf json消息
     */
    private String json;


    public Supplier<? extends Message.Builder> getBuilder() {
        return typeBuilderMapping.get(this.type);
    }

}
