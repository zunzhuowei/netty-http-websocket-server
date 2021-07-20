package com.hbsoo.test;

import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by zun.wei on 2021/7/20.
 * 自定义线程池
 */
public final class MyExecutorService {


    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    private final Thread singleThread;

    public MyExecutorService() {
        this.singleThread = new Thread(() -> {
            while (true) {
                try {
                    final Runnable runnable = queue.take();
                    runnable.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        this.singleThread.setUncaughtExceptionHandler(((t, e) -> {
            e.printStackTrace();
        }));
        this.singleThread.setName("MyExecutorService thread");
        this.singleThread.start();
    }


    public void execute(Runnable runnable) {
        this.queue.offer(runnable);
    }

    /**
     * 等待子线程执行完成，
     * 原理是主线程调用了wait()方法；等待子线程的notify；
     * @throws InterruptedException
     */
    public void join() throws InterruptedException {
        this.singleThread.join();
    }


}
