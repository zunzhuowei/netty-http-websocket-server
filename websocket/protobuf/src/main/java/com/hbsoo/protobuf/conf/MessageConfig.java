package com.hbsoo.protobuf.conf;

import com.google.protobuf.ProtocolMessageEnum;
import com.hbsoo.commons.message.MagicNum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 *  消息配置
 */
@Data
@Accessors(chain = true)
public final class MessageConfig<ProtoBuf, ProtoMsgType extends ProtocolMessageEnum> {

    /**
     * protobuf 消息类class
     */
    private Class<ProtoBuf> protoBufClazz;
    /**
     * protobuf 消息类中定义的枚举消息类型的 .values() 函数
     */
    private Supplier<ProtoMsgType[]> protoMsgTypesValuesFunction;
    /**
     * protobuf 消息映射到消息处理器所在包的路径
     */
    private String[] scanMessageHandlerPackagePath;
    /**
     * 处理消息协议魔法头,每个 protobuf 消息类应该对应不同的魔法头
     */
    private MagicNum magicNum;
    /**
     * protobuf 消息类中定义的枚举消息类型的 .forNumber(int value) 函数
     */
    private Function<Integer, ProtoMsgType> protoMsgTypesForNumberFunction;


    public MessageConfig<ProtoBuf, ProtoMsgType> setScanMessageHandlerPackagePath(String... scanMessageHandlerPackagePath) {
        this.scanMessageHandlerPackagePath = scanMessageHandlerPackagePath;
        return this;
    }

}
