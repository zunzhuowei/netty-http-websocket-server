package com.hbsoo.http.model;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zun.wei on 2021/7/15.
 */
@Data
public class HttpMessage {

    String uri;

    HttpMethod httpMethod;

    Map<String, String> parmMap;

    HttpHeaders headers;

    //ServerCookieDecoder serverCookieDecoder ;



}
