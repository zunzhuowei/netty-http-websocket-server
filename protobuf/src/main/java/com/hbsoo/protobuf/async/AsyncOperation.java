package com.hbsoo.protobuf.async;

import com.hbsoo.protobuf.processor.AsyncOperationProcessor;
import io.netty.channel.Channel;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created by zun.wei on 2021/7/21.
 *  异步操作类；
 *  执行流程：
 *  1. 在主线程中使用异步线程处理异步事件
 *  2. 异步线程中，异步事件处理完成之后，拿到处理结果，
 *  3. 调用主线程处理异步操作的 finish 方法，将结果以callback方式导出
 */
public abstract class AsyncOperation<R> {


    /**
     * 异步处理返回的结果
     */
    private R result;
    /**
     * 异步处理结果消费者
     */
    private final Consumer<R> resultConsumer;

    public AsyncOperation() {
        this.resultConsumer = null;
    }

    public AsyncOperation(Consumer<R> resultConsumer) {
        this.resultConsumer = resultConsumer;
    }

    public void setResult(R r) {
        this.result = r;
    }

    public R getResult() {
        return this.result;
    }

    /**
     * 绑定到指定线程id中执行
     * @return 根据返回值，取模
     */
    public abstract int getBindId();

    /**
     * 异步操作
     */
    public abstract void execute();

    /**
     * 执行完成,消费结果
     */
    public void finish() throws Exception{
        if (Objects.nonNull(this.resultConsumer)) {
            this.resultConsumer.accept(this.result);
        }
    }

    /**
     * 调用异步处理类
     */
    public void process(Channel... channels) {
        for (Channel channel : channels) {
            AsyncOperationProcessor.process(channel, this);
        }
    }

}
