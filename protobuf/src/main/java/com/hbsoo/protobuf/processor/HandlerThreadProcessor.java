package com.hbsoo.protobuf.processor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by zun.wei on 2021/7/21.
 * 游戏逻辑处理类，因为都是在内存操作，没有io操作。所以用单线程执行
 */
public final class HandlerThreadProcessor {

    /**
     * 业务处理线程池，用单线程处理，保证线程安全
     */
    private final ExecutorService handlerThread;

    private HandlerThreadProcessor() {
        handlerThread = Executors.newSingleThreadExecutor(r -> {
            final Thread thread = new Thread(r);
            thread.setName("handler-thread");
            thread.setUncaughtExceptionHandler((t, e) -> {
                e.printStackTrace();
            });
            return thread;
        });
    }

    /**
     * 单例
     */
    private static HandlerThreadProcessor processor = new HandlerThreadProcessor();

    /**
     * 获取单例
     * @return 单例
     */
    public static HandlerThreadProcessor getInstance() {
        return processor;
    }

    /**
     * 处理事件
     * @param runnable 可执行事件
     * @return Future
     */
    public static void process(Runnable runnable) {
        HandlerThreadProcessor.getInstance().handlerThread.execute(runnable);
    }



}
