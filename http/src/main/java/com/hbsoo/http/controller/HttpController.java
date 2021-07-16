package com.hbsoo.http.controller;

import com.alibaba.fastjson.JSON;
import com.hbsoo.http.conf.UriHandlerMapper;
import com.hbsoo.http.model.HttpMessage;
import com.hbsoo.http.utils.HttpHelper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.util.Objects;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by zun.wei on 2021/7/15.
 */
public interface HttpController {


    enum ContentType {
        HTML,JSON,;
    }

    /**
     * 处理http消息
     * @param request
     * @return
     * @throws IOException
     */
    static DefaultFullHttpResponse handle(FullHttpRequest request) throws IOException {
        //uri中包含了请求参数 ？aa=value
        String uri = request.uri();
        String[] split = uri.split("[?]");
        HttpController controller = UriHandlerMapper.router.get(split[0]);
        if (Objects.isNull(controller)) {
            System.err.println("controller for request uri --::" + uri + " not found!");
            return new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
        }
        HttpMessage httpMessage = HttpHelper.parseReqMessage(request);
        Object responseMsg = controller.handle(httpMessage);
        ByteBuf content;
        if (responseMsg instanceof ByteBuf) {
            content = (ByteBuf) responseMsg;
        } else if (responseMsg instanceof CharSequence) {
            content = Unpooled.copiedBuffer(responseMsg.toString(), CharsetUtil.UTF_8);
        } else {
            String string = JSON.toJSONString(responseMsg);
            content = Unpooled.copiedBuffer(string, CharsetUtil.UTF_8);
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

    /**
     * 注册的请求uri
     */
    String[] regUri();

    /**
     * 返回内容类型
     */
    default ContentType contentType(){
        return ContentType.JSON;
    }

    /**
     * 处理http消息
     * @param httpMessage 消息参数
     * @return
     */
    Object handle(HttpMessage httpMessage);



}
