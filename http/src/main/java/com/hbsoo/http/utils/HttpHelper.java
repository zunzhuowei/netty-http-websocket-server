package com.hbsoo.http.utils;

import com.hbsoo.http.model.HttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zun.wei on 2021/7/15.
 */
public final class HttpHelper {


    public static HttpMessage parseReqMessage(FullHttpRequest request) throws IOException {
        HttpMessage httpMessage = new HttpMessage();
        String uri = request.uri();
        HttpHeaders headers = request.headers();
        Map<String, String> reqParams = parse(request);
        HttpMethod method = request.method();


        httpMessage.setHttpMethod(method);
        httpMessage.setUri(uri);
        httpMessage.setParmMap(reqParams);
        httpMessage.setHeaders(headers);
        return httpMessage;
    }

    /**
     * 解析请求参数
     *
     * @return 包含所有请求参数的键值对, 如果没有参数, 则返回空Map
     */
    private static Map<String, String> parse(FullHttpRequest fullReq) throws IOException {
        HttpMethod method = fullReq.method();
        Map<String, String> parmMap = new HashMap<>();

        // 是GET请求
        if (HttpMethod.GET == method) {
            QueryStringDecoder decoder = new QueryStringDecoder(fullReq.uri());
            decoder.parameters().entrySet().forEach(entry -> {
                //TODO entry.getValue()是一个List, 只取第一个元素
                parmMap.put(entry.getKey(), entry.getValue().get(0));
            });
        }
        // 是POST请求
        else if (HttpMethod.POST == method) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(fullReq);
            decoder.offer(fullReq);

            List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();

            for (InterfaceHttpData parm : parmList) {

                Attribute data = (Attribute) parm;
                parmMap.put(data.getName(), data.getValue());
            }

        }
        // 不支持其它方法
        else {
            throw new RuntimeException("不支持其它请求方法"); // 这是个自定义的异常, 可删掉这一行
        }

        return parmMap;
    }

}
