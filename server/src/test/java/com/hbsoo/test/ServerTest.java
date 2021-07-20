package com.hbsoo.test;

import org.junit.Test;

/**
 * Created by zun.wei on 2021/7/16.
 */
public class ServerTest {


    @Test
    public void test() throws InterruptedException {
        final MyExecutorService service = new MyExecutorService();
        for (int i = 0; i < 100; i++) {
            service.execute(() ->{
                final String name = Thread.currentThread().getName();
                System.out.println("----" + name);
            });
        }
        service.join();
        System.out.println("service = " + service);
    }

    public static void main(String[] args) {
        final MyExecutorService service = new MyExecutorService();
        for (int i = 0; i < 100; i++) {
            service.execute(() ->{
                final String name = Thread.currentThread().getName();
                System.out.println("----" + name);
            });
        }
        System.out.println("service = " + service);
    }

}
