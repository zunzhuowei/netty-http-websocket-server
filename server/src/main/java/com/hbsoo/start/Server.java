package com.hbsoo.start;

import com.hbsoo.commons.message.MagicNum;
import com.hbsoo.game.protocol.GameProtocol;
import com.hbsoo.http.conf.UriHandlerMapper;
import com.hbsoo.protobuf.conf.MessageConfig;
import com.hbsoo.protobuf.conf.MessageTypeHandleMapper;
import com.hbsoo.websocket.protocol.ProtoBufMessage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by zun.wei on 2021/7/15.
 */
public class Server {

    static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));

    public static void main(String[] args) throws Exception {
        UriHandlerMapper.init();
        MessageTypeHandleMapper.init(
                new MessageConfig<ProtoBufMessage,ProtoBufMessage.MessageType>()
                        .setMagicNum(MagicNum.COMMON)
                        .setProtoBufClazz(ProtoBufMessage.class)
                        .setProtoMsgTypesValuesFunction(ProtoBufMessage.MessageType::values)
                        .setProtoMsgTypesForNumberFunction(ProtoBufMessage.MessageType::forNumber)
                        .setScanMessageHandlerPackagePath("com.hbsoo.websocket.message"),
                new MessageConfig<GameProtocol,GameProtocol.MessageType>()
                        .setMagicNum(MagicNum.GAME)
                        .setProtoBufClazz(GameProtocol.class)
                        .setProtoMsgTypesValuesFunction(GameProtocol.MessageType::values)
                        .setProtoMsgTypesForNumberFunction(GameProtocol.MessageType::forNumber)
                        .setScanMessageHandlerPackagePath("com.hbsoo.game.message")
        );

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(10);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new WebSocketServerInitializer("/websocket"));

            Channel ch = b.bind(PORT).sync().channel();
            System.out.println("Open your web browser and navigate to http://127.0.0.1:" + PORT + '/');
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
