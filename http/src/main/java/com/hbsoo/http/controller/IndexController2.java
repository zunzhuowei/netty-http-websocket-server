package com.hbsoo.http.controller;

import com.alibaba.fastjson.JSON;
import com.hbsoo.http.model.HttpMessage;
import jdk.nashorn.internal.parser.JSONParser;

/**
 * Created by zun.wei on 2021/7/15.
 */
public class IndexController2 implements HttpController{


    @Override
    public String[] regUri() {
        return new String[]{"/test2","/test22"};
    }

    @Override
    public ContentType contentType() {
        return ContentType.JSON;
    }

    @Override
    public Object handle(HttpMessage httpMessage) {
        System.out.println("httpMessage = " + httpMessage);
        String string = JSON.toJSONString(httpMessage);
        return string;
    }

}
