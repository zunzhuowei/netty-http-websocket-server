package com.hbsoo.java;

import com.hbsoo.groovy.processor.AsyncOperationGroovyProcessor;
import groovy.lang.Closure;
import org.junit.Test;

/**
 * Created by zun.wei on 2021/7/23.
 */
public class ProcessorTest {


    @Test
    public void test() throws InterruptedException {
        final long millis = System.currentTimeMillis();
        final Thread thread = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName());
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        final Thread thread1 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName());
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
        //thread1.start();

        thread.join();
        //thread1.join();
        final long millise = System.currentTimeMillis();
        System.out.println("finish test --::" + (millise - millis));

    }

}
