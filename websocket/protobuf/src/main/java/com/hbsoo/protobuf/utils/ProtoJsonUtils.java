package com.hbsoo.protobuf.utils;

import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;
import com.googlecode.protobuf.format.JsonJacksonFormat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

/**
 * 特别主要：
 * <ul>
 *  <li>该实现无法处理含有Any类型字段的Message</li>
 *  <li>enum类型数据会转化为enum的字符串名</li>
 *  <li>bytes会转化为Base64字符串</li>
 * </ul>
 */
public final class ProtoJsonUtils {


    public static final JsonFormat jsonFormat = new JsonFormat();

    /**
     * 转成 json
     * @param sourceMessage
     * @param <T>
     * @return
     */
    public static <T extends Message> String toJson(T sourceMessage) {
        final String string = jsonFormat.printToString(sourceMessage);
        return string;
    }

    /**
     * json 转成 protobuf 对象
     * @param builder
     * @param json
     * @param <T>
     * @param <B>
     * @return
     * @throws IOException
     */
    public static <T extends Message, B extends Message.Builder> T toProtoBean(B builder, String json)  {
        //JsonFormat.parser().merge(json, builder);
        try (final ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes())) {
            jsonFormat.merge(inputStream, builder);
            return (T) builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json 转成 protobuf 对象
     * @param builderSupplier
     * @param json
     * @param <T>
     * @param <B>
     * @return
     */
    public static <T extends Message, B extends Message.Builder> T toProtoBean(Supplier<B> builderSupplier, String json) {
        B builder = builderSupplier.get();
        return toProtoBean(builder, json);
    }

    /*
1     // protobuf 转 json
2     Message.Builder message = Message.newBuilder();
3     String json = JsonFormat.printToString(message.build());

//json 转 protobuf
    try {
        JsonFormat.merge(json, message);
    } catch (ParseException e) {
        e.printStackTrace();
    }
 */

}
