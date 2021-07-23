package com.hbsoo.protobuf.processor;

import com.hbsoo.protobuf.async.AsyncOperation;
import com.hbsoo.protobuf.exception.GlobalExceptionWriter;
import io.netty.channel.Channel;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
     * 异步线程数
     */
    public static final int asyncThreads = CPU_COUNT * 2;

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
     * 获取异步线程池
     */
    public ExecutorService[] getAsync() {
        return this.async;
    }

    /**
     * 构造器
     */
    private AsyncOperationProcessor() {
        async = new ExecutorService[asyncThreads];
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
        process(channel,
                asyncOperation::getBindId,
                () -> {
                    asyncOperation.execute();
                    return asyncOperation.getResult();
                }, result -> {
                    try {
                        asyncOperation.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        GlobalExceptionWriter.getProcessor().writeGlobalException(channel, e);
                    }
                });
    }

    /**
     * 处理异步操作
     *
     * @param channel           消息管道，用于捕获业务异常统一返回
     * @param threadRatio       线程系数，根据系数与线程池取模，选择不同线程执行
     * @param asyncExecute      异步处理函数，该线程为多线程，专门执行耗时的操作
     * @param asyncExecuteAfter 异步处理之后，回到主线程处理函数;该线程为单线程不能执行耗时的操作
     * @param <T>               异步处理得到的结果类型
     */
    public static <T> void process(Channel channel, Supplier<Integer> threadRatio, Supplier<T> asyncExecute, Consumer<T> asyncExecuteAfter) {
        final Integer ratio = threadRatio.get();
        final int abs = Math.abs(ratio);
        final int i = abs % AsyncOperationProcessor.asyncThreads;
        AsyncOperationProcessor.getInstance().getAsync()[i].execute(() -> {
            try {
                // 执行异步操作,例如耗时的IO操作
                final T result = asyncExecute.get();

                // 返回主线程执行完成逻辑;这里不能获取  Future<?> 返回值 get()；否则会堵塞；
                // 原因：HandlerThreadProcessor是单线程，前面逻辑已经调用了 Future<?> 返回值 get()方法，
                // 堵塞等待返回值；这里再调用一次则会造成死锁；
                HandlerThreadProcessor.process(() -> {
                    try {
                        asyncExecuteAfter.accept(result);
                    } catch (Exception e) {
                        if (Objects.nonNull(channel)) {
                            GlobalExceptionWriter.getProcessor().writeGlobalException(channel, e);
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                if (Objects.nonNull(channel)) {
                    GlobalExceptionWriter.getProcessor().writeGlobalException(channel, e);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }


}
