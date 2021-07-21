package com.hbsoo.start;

import com.hbsoo.http.handler.HttpProtoHandler;
import com.hbsoo.protobuf.codec.MyProtobufDecoder;
import com.hbsoo.protobuf.codec.MyProtobufEncoder;
import com.hbsoo.protobuf.handler.ProtobufHandler;
import com.hbsoo.protobuf.handler.WebSocketFrameHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;

/**
 * Created by zun.wei on 2021/7/15.
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    private final String WEBSOCKET_PATH;


    public WebSocketServerInitializer(String path) {
        this.WEBSOCKET_PATH = path;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
        pipeline.addLast(new HttpProtoHandler(WEBSOCKET_PATH));
        pipeline.addLast(new WebSocketFrameHandler());
        pipeline.addLast(new MyProtobufDecoder());
        pipeline.addLast(new MyProtobufEncoder());
        pipeline.addLast(new ProtobufHandler());
    }

}
