package com.hbsoo;

import com.hbsoo.protobuf.processor.AsyncOperationProcessor;

/**
 * Created by zun.wei on 2021/7/25.
 */
public class ProcessorTest {


    public static void main(String[] args) {
        AsyncOperationProcessor.process(null, () -> 1,
                () -> {
                    System.out.println("async");
                    return 1;
                }, obj -> {
                    System.out.println("sync");
                });
        AsyncOperationProcessor.process(null, () -> 1,
                () -> {
                    System.out.println("async");
                    return 1;
                }, obj -> {
                    System.out.println("sync");
                });
    }


}
