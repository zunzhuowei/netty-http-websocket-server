package com.hbsoo.protobuf.processor;

import com.hbsoo.protobuf.async.AsyncOperation;
import com.hbsoo.protobuf.exception.GlobalExceptionWriter;
import io.netty.channel.Channel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zun.wei on 2021/7/21.
 * 异步线程，用于处理IO操作
 */
public final class AsyncOperationProcessor {

    /**
     * cpu核心数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * 异步线程
     */
    private final ExecutorService[] async;
    /**
     * 单例
     */
    public static final AsyncOperationProcessor instance = new AsyncOperationProcessor();


    /**
     * 获取单例
     */
    public static AsyncOperationProcessor getInstance() {
        return instance;
    }

    /**
     * 构造器
     */
    public AsyncOperationProcessor() {
        async = new ExecutorService[CPU_COUNT];
        AtomicInteger integer = new AtomicInteger();
        for (int i = 0; i < async.length; i++) {
            async[i] = Executors.newSingleThreadExecutor(r -> {
                final Thread thread = new Thread(r);
                final int i1 = integer.incrementAndGet();
                thread.setName("async-thread-" + i1);
                thread.setUncaughtExceptionHandler((t, e) -> {
                    e.printStackTrace();
                });
                return thread;
            });
        }
    }


    /**
     * 处理异步操作
     *
     * @param asyncOperation 异步操作
     */
    public static <T> void process(Channel channel, AsyncOperation<T> asyncOperation) {
        final int bindId = asyncOperation.getBindId();
        final int abs = Math.abs(bindId);
        final int i = abs % CPU_COUNT;
        getInstance().async[i].execute(() -> {
            try {
                // 执行异步操作,例如耗时的IO操作
                asyncOperation.execute();
                // 返回主线程执行完成逻辑;这里不能获取  Future<?> 返回值 get()；否则会堵塞；
                // 原因：HandlerThreadProcessor是单线程，前面逻辑已经调用了 Future<?> 返回值 get()方法，
                // 堵塞等待返回值；这里再调用一次则会造成死锁；
                HandlerThreadProcessor.process(() -> {
                    try {
                        asyncOperation.finish();
                    } catch (Exception e) {
                        //e.printStackTrace();
                        GlobalExceptionWriter.getProcessor().writeGlobalException(channel, e);
                    }
                });
            } catch (Exception e) {
                GlobalExceptionWriter.getProcessor().writeGlobalException(channel, e);
            }
        });
    }


}
