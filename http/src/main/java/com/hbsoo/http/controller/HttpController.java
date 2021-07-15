package com.hbsoo.http.controller;

import com.hbsoo.http.model.HttpMessage;
import com.hbsoo.http.utils.HttpHelper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by zun.wei on 2021/7/15.
 */
public interface HttpController {

    Map<String, HttpController> router = new HashMap<>();

    enum ContentType {
        HTML,JSON,;
    }


    static void init() {
        HttpController.initController(
                new IndexController(),
                new IndexController2()
        );
    }

    static <T extends HttpController> void initController(T... controller) {
        for (T t : controller) {
            String[] uris = t.regUri();
            for (String uri : uris) {
                router.put(uri, t);
            }
        }
    }

    //DefaultFullHttpResponse extends DefaultHttpResponse implements FullHttpResponse
    static DefaultFullHttpResponse handle(FullHttpRequest request) throws IOException {
        //uri中包含了请求参数 ？aa=value
        String uri = request.uri();
        String[] split = uri.split("[?]");
        HttpController controller = router.get(split[0]);
        if (Objects.isNull(controller)) {
            System.err.println("controller for request uri --::" + uri + " not found!");
            return new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
        }
        HttpMessage httpMessage = HttpHelper.parseReqMessage(request);
        Object responseMsg = controller.handle(httpMessage);
        ByteBuf content;
        if (responseMsg instanceof ByteBuf) {
            content = (ByteBuf) responseMsg;
        } else {
            content = Unpooled.copiedBuffer(responseMsg.toString(), CharsetUtil.UTF_8);
        }
        DefaultFullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, OK, content);
        ContentType contentType = controller.contentType();
        switch (contentType) {
            case HTML:
                res.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
                break;
            case JSON:
                res.headers().set(CONTENT_TYPE, "application/json;charset=utf-8");
                break;
        }
        HttpUtil.setContentLength(res, content.readableBytes());
        return res;
    }


    String[] regUri();

    ContentType contentType();

    Object handle(HttpMessage httpMessage);



}
