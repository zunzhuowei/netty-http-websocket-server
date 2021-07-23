package com.hbsoo.groovy.processor

import com.hbsoo.protobuf.processor.AsyncOperationProcessor
import io.netty.channel.Channel

/**
 * Created by zun.wei on 2021/7/23.
 *
 */
final class AsyncOperationGroovyProcessor {


    /**
     * 处理异步操作
     *
     * @param channel           消息管道，用于捕获业务异常统一返回
     * @param threadRatio       线程系数，根据系数与线程池取模，选择不同线程执行
     * @param asyncExecute      异步处理函数，该线程为多线程，专门执行耗时的操作;（返回异步处理结果）
     * @param asyncExecuteAfter 异步处理之后，回到主线程处理函数;该线程为单线程不能执行耗时的操作；（消费异步处理产生的结果）
     * @param <T>               异步处理得到的结果类型
     */
    static <T> void process(Channel channel, Closure<Integer> threadRatio,
                            Closure<T> asyncExecute, Closure<T> asyncExecuteAfter) {
        AsyncOperationProcessor.process(channel, threadRatio, asyncExecute, asyncExecuteAfter)
    }

}
