package com.hbsoo.websocket.handler;

import com.hbsoo.websocket.utils.Broadcaster;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

/**
 * Created by zun.wei on 2021/7/15.
 */
@Slf4j
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Broadcaster.subscribe(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Broadcaster.unsubscribe(ctx.channel());
        super.channelInactive(ctx);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // ping and pong frames already handled

        if (frame instanceof TextWebSocketFrame) {
            // Send the uppercase string back.
            String request = ((TextWebSocketFrame) frame).text();
            ctx.channel().writeAndFlush(new TextWebSocketFrame(request.toUpperCase(Locale.US)));
        } else {
            frame.retain();
            ctx.fireChannelRead(frame);

            //String message = "unsupported frame type: " + frame.getClass().getName();
            //throw new UnsupportedOperationException(message);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info(cause.getMessage());
    }
}
