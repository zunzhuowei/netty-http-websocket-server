package com.hbsoo.commons;

import com.hbsoo.commons.processor.ItemProcessor;
import com.hbsoo.commons.processor.ProcessorHolder;
import com.hbsoo.commons.processor.ProcessorType;

/**
 * Created by zun.wei on 2021/7/25.
 */
public class ProcessorHolderTest {


    public static void main(String[] args) {
        ProcessorHolder.getHolder()
                .addLast(ItemProcessor.getItem().setType(ProcessorType.MULTI).setLogic(obj -> {
                    System.out.println("first obj  = " + obj);
                    return 1;
                }))
                .addLast(ItemProcessor.getItem().setType(ProcessorType.SINGLE).setLogic(obj -> {
                    System.out.println("second obj  = " + obj);
                    return 2;
                }))
                .addLast(ItemProcessor.getItem().setType(ProcessorType.MULTI).setLogic(obj -> {
                    System.out.println("three obj  = " + obj);
                    return 3;
                }))
                .addLast(ItemProcessor.getItem().setType(ProcessorType.SINGLE).setLogic(obj -> {
                    System.out.println("four obj  = " + obj);
                    return 4;
                }))
                .addLast(ItemProcessor.getItem().setType(ProcessorType.SINGLE).setLogic(obj -> {
                    System.out.println("five obj  = " + obj);
                    return 5;
                }))
                .execute();
        System.out.println("test finish 111111111111111");
        ProcessorHolder.getHolder()
                .addLast(ItemProcessor.getItem().setType(ProcessorType.MULTI).setLogic(obj -> {
                    System.out.println("first obj  = " + obj);
                    return 11;
                }))
                .addLast(ItemProcessor.getItem().setType(ProcessorType.SINGLE).setLogic(obj -> {
                    System.out.println("second obj  = " + obj);
                    return 22;
                }))
                .addLast(ItemProcessor.getItem().setType(ProcessorType.MULTI).setLogic(obj -> {
                    System.out.println("three obj  = " + obj);
                    return 33;
                }))
                .addLast(ItemProcessor.getItem().setType(ProcessorType.SINGLE).setLogic(obj -> {
                    System.out.println("four obj  = " + obj);
                    return 44;
                }))
                .addLast(ItemProcessor.getItem().setType(ProcessorType.SINGLE).setLogic(obj -> {
                    System.out.println("five obj  = " + obj);
                    return 55;
                }))
                .execute();
        System.out.println("test finish 2222222222222222222222");
    }


}
