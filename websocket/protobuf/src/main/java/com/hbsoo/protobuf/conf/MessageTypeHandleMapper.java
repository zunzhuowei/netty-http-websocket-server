package com.hbsoo.protobuf.conf;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.ProtocolMessageEnum;
import com.hbsoo.commons.message.MagicNum;
import com.hbsoo.commons.utils.PackageUtil;
import com.hbsoo.protobuf.message.IWebSocketMessageHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by zun.wei on 2021/7/16.
 *  消息型类型与处理器映射处理类；
 *  1. 包括编解码器的消息类型映射；
 *  2. 包括消息类型处理器映射；
 */
@Slf4j
public final class MessageTypeHandleMapper {


    /**
     * 消息类型与消息字典
     */
    public static final Map<Object, GeneratedMessageV3> msgMapping = new HashMap<>();
    /**
     * 消息路由处理器字典
     */
    public static final Map<Class<? extends GeneratedMessageV3>, Set<IWebSocketMessageHandler>> msgRouter = new HashMap<>();
    /**
     * 消息魔法头与消息类型 .forNumber(Integer i) 的映射关系
     */
    public static final Map<Short, Function<Integer, ? extends ProtocolMessageEnum>> magicNumMsgTypeMap = new HashMap<>();

    /**
     * 初始化消息映射关系
     *
     */
    public static void init(MessageConfig... messageConfig) {
        if (messageConfig.length < 1) {
            log.warn("===============websocket消息未配置================");
            return;
        }
        for (MessageConfig config : messageConfig) {
            MagicNum magicNum = config.getMagicNum();
            Class protoBufClazz = config.getProtoBufClazz();
            Function protoMsgTypesForNumberFunction = config.getProtoMsgTypesForNumberFunction();
            Supplier protoMsgTypesValuesFunction = config.getProtoMsgTypesValuesFunction();
            String[] scanMessageHandlerPackagePath = config.getScanMessageHandlerPackagePath();

            /** 解码器消息类型注册 */
            initDecodeMapping(protoBufClazz, (ProtocolMessageEnum[]) protoMsgTypesValuesFunction.get());
            /** 消息处理器路由注册 */
            initMessageRouter(scanMessageHandlerPackagePath);
            magicNumMsgTypeMap.put(magicNum.magicNum, protoMsgTypesForNumberFunction);
        }
    }

    /**
     *   解码器消息类型注册
     */
    private static <ProtoBuf, ProtoMsgType extends ProtocolMessageEnum> void initDecodeMapping(
            Class<ProtoBuf> protoBufClazz, ProtoMsgType[] protoMsgTypes) {
        log.info("\n\n==== 开始扫描消息类型与消息的关联 ====");
        Class<?>[] innerClazzArray = protoBufClazz.getDeclaredClasses();

        for (Class<?> innerClazz : innerClazzArray) {
            if (null == innerClazz ||
                    !GeneratedMessageV3.class.isAssignableFrom(innerClazz)) {
                // 如果不是消息类,
                continue;
            }

            // 获取类名称并小写
            String clazzName = innerClazz.getSimpleName();
            clazzName = clazzName.toLowerCase();

            for (ProtoMsgType msgCode : protoMsgTypes) {
                if (null == msgCode) {
                    continue;
                }

                // 获取消息编码
                String strMsgCode = msgCode.toString();
                strMsgCode = strMsgCode.replaceAll("_", "");
                strMsgCode = strMsgCode.toLowerCase();

                if (!strMsgCode.equals(clazzName)) {
                    continue;
                }

                try {
                    // 相当于调用 UserEntryCmd.getDefaultInstance();
                    Object returnObj = innerClazz.getDeclaredMethod("getDefaultInstance").invoke(innerClazz);
                    log.info("{} <==> {} <==> {}", innerClazz.getName(), msgCode, msgCode.getNumber());
                    msgMapping.put(msgCode, (GeneratedMessageV3) returnObj);
                } catch (Exception ex) {
                    // 记录错误日志
                    log.error(ex.getMessage(), ex);
                }
            }
        }

        log.info("\n\n==== 开始检查消息类型与消息的关联 ====");
        for (ProtoMsgType messageType : protoMsgTypes) {
            GeneratedMessageV3 messageV3 = msgMapping.get(messageType);
            if (Objects.isNull(messageV3)) {
                log.warn("[{}] message type un mapping!", messageType);
            }
        }

        log.info("==== 结束扫描消息类型与消息的关联 ====\n\n");
    }

    /**
     * 消息处理器路由注册
     */
    private static void initMessageRouter(String... scanHandlerPackage) {
        for (String packagePath :scanHandlerPackage){
            initMessageRouter(packagePath);
        }
    }
    /**
     * 消息处理器路由注册
     */
    private static void initMessageRouter(String scanHandlerPackage) {
        log.info("\n\n==== 开始扫描消息与消息处理器的关联 ====");

        // 获取包名称
        //final String packageName = IWebSocketMessageHandler.class.getPackage().getName();
        // 获取 IWebSocketMessageHandler 所有的实现类
        Set<Class<?>> clazzSet = PackageUtil.listSubClazz(scanHandlerPackage, true, IWebSocketMessageHandler.class);

        for (Class<?> handlerClazz : clazzSet) {
            if (null == handlerClazz ||
                    0 != (handlerClazz.getModifiers() & Modifier.ABSTRACT)) {
                continue;
            }

            // 获取方法数组
            Method[] methodArray = handlerClazz.getDeclaredMethods();
            // 消息类型
            Class<?> cmdClazz = null;

            for (Method currMethod : methodArray) {
                if (null == currMethod ||
                        !currMethod.getName().equals("handle")) {
                    continue;
                }
                // 获取参数类型列表
                Type[] genericParameterTypes = currMethod.getGenericParameterTypes();
                if (genericParameterTypes.length < 2) {
                    continue;
                }
                // 获取方法中第二个参数的泛型实际参数类型列表
                Type[] actualTypeArguments = ((ParameterizedType) currMethod.getGenericParameterTypes()[1])
                        .getActualTypeArguments();
                // 比较实际泛型中第一个参数类型
                if (actualTypeArguments[0] == GeneratedMessageV3.class) {
                    continue;
                }

                // 获取泛型中第一个参数的实际类型名
                String typeName = actualTypeArguments[0].getTypeName();
                try {
                    Class<?> aClass = Class.forName(typeName);
                    cmdClazz = aClass;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                /*// 获取函数参数类型数组
                Class<?>[] paramTypeArray = currMethod.getParameterTypes();
                // 方法参数少于两个，或者第二个为GeneratedMessageV3，或者第二个参数不是GeneratedMessageV3的子类
                // 则不处理
                if (paramTypeArray.length < 2 ||
                        paramTypeArray[1] == GeneratedMessageV3.class ||
                        !GeneratedMessageV3.class.isAssignableFrom(paramTypeArray[1])) {
                    continue;
                }
                cmdClazz = paramTypeArray[1];*/
                break;
            }

            if (null == cmdClazz) {
                continue;
            }

            try {
                // 创建命令处理器实例
                IWebSocketMessageHandler<?> newHandler = (IWebSocketMessageHandler<?>) handlerClazz.newInstance();

                log.info("{} <==> {}", cmdClazz.getName(), handlerClazz.getName());
                // 设置 protobuf 消息类型class 与 protobuf消息处理器的映射关系
                Class key = (Class) cmdClazz;
                // 允许一个消息有多个处理类
                Set<IWebSocketMessageHandler> handlers = msgRouter.get(key);
                if (Objects.isNull(handlers)) {
                    handlers = new HashSet<>();
                    handlers.add(newHandler);
                    msgRouter.put(key, handlers);
                } else {
                    handlers.add(newHandler);
                }
            } catch (Exception ex) {
                // 记录错误日志
                log.error(ex.getMessage(), ex);
            }
        }

        log.info("\n\n==== 开始检查消息与消息处理器的关联 ====");
        msgMapping.forEach((k, v) -> {
            Class<? extends GeneratedMessageV3> aClass = v.getClass();
            Set<IWebSocketMessageHandler> handler = msgRouter.get(aClass);
            if (Objects.isNull(handler)) {
                log.warn("message handler mapping not fund! message type --:: [{}]", k);
            }
        });

        log.info("==== 结束扫描消息与消息处理器的关联 ====\n\n");
    }

}
