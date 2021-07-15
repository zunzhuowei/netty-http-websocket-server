package com.hbsoo.http.controller;

import com.hbsoo.http.model.HttpMessage;
import com.hbsoo.http.pages.WebSocketServerIndexPage;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaderNames;

/**
 * Created by zun.wei on 2021/7/15.
 */
public class IndexController implements HttpController {


    @Override
    public String[] regUri() {
        return new String[]{"/", "/index.html"};
    }

    @Override
    public ContentType contentType() {
        return ContentType.HTML;
    }

    @Override
    public Object handle(HttpMessage httpMessage) {
        String webSocketLocation = "ws://" + httpMessage.getHeaders().get(HttpHeaderNames.HOST) + "/websocket";
        ByteBuf content = WebSocketServerIndexPage.getContent(webSocketLocation);
        return content;
    }


}
