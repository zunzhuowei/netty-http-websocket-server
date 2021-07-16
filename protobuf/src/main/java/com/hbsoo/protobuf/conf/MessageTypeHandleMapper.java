package com.hbsoo.protobuf.conf;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.ProtocolMessageEnum;
import com.hbsoo.commons.utils.PackageUtil;
import com.hbsoo.protobuf.message.IWebSocketMessageHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Supplier;

/**
 * Created by zun.wei on 2021/7/16.
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
    public static final Map<Class<? extends GeneratedMessageV3>, IWebSocketMessageHandler> msgRouter = new HashMap<>();

    /**
     * 初始化消息映射关系
     *
     * @param protoBufClazz         protoBuf 协议类 class
     * @param protoMsgTypesFunction protoBuf 协议枚举消息类型数组函数 .values();
     * @param scanMessageHandlerPackage 消息处理器扫描路径
     * @param <ProtoBuf>            protoBuf 协议类
     * @param <ProtoMsgType>        protoBuf 协议枚举消息类型
     */
    public static <ProtoBuf, ProtoMsgType extends ProtocolMessageEnum> void init
    (Class<ProtoBuf> protoBufClazz, Supplier<ProtoMsgType[]> protoMsgTypesFunction, String... scanMessageHandlerPackage)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        /** 解码器消息类型注册 */
        initDecodeMapping(protoBufClazz, protoMsgTypesFunction.get());
        /** 消息处理器路由注册 */
        initMessageRouter(scanMessageHandlerPackage);
    }

    /**
     *   解码器消息类型注册
     */
    private static <ProtoBuf, ProtoMsgType extends ProtocolMessageEnum> void initDecodeMapping(
            Class<ProtoBuf> protoBufClazz,
            ProtoMsgType[] protoMsgTypes
    ) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
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

                if (!strMsgCode.startsWith(clazzName)) {
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

                Type[] genericParameterTypes = currMethod.getGenericParameterTypes();
                if (genericParameterTypes.length < 2) {
                    continue;
                }

                Type[] actualTypeArguments = ((ParameterizedType) currMethod.getGenericParameterTypes()[1]).getActualTypeArguments();
                if (actualTypeArguments[0] == GeneratedMessageV3.class) {
                    continue;
                }

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

                msgRouter.put((Class) cmdClazz, newHandler);
            } catch (Exception ex) {
                // 记录错误日志
                log.error(ex.getMessage(), ex);
            }
        }

        log.info("\n\n==== 开始检查消息与消息处理器的关联 ====");
        msgMapping.forEach((k, v) -> {
            Class<? extends GeneratedMessageV3> aClass = v.getClass();
            IWebSocketMessageHandler handler = msgRouter.get(aClass);
            if (Objects.isNull(handler)) {
                log.warn("message handler mapping not fund! message type --:: [{}]", k);
            }
        });

        log.info("==== 结束扫描消息与消息处理器的关联 ====\n\n");
    }

}
