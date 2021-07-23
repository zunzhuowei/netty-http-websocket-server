package com.hbsoo.groovy


import com.hbsoo.groovy.processor.AsyncOperationGroovyProcessor
import org.junit.Test

/**
 * Created by zun.wei on 2021/7/23.
 *
 */
class AsyncOperationGroovyProcessorTest {


    /**
     *  {@link com.hbsoo.java.ProcessorTest#test()}
     *
     * 多线程测试结果；
     * 1. 如果在main方法中执行，main主线程会等待子线程执行完成后再退出；
     * 2. 如果是在单元测试中执行，主线程不会等待子线程执行完再退出；
     * 3. 如果是在java单元测试中执行，子线程可以用join方式阻塞主线程退出；
     * 4. 如果是在groovy单元测试中执行，子线程不可以用join方式阻塞主线程退出；
     */
    @Test
    void test() {
        Thread t = new Thread(() -> {
            for (int x : 0..100) {
                String name = "" + x
                AsyncOperationGroovyProcessor.process(null,
                        {
                            name.hashCode()
                        },
                        {
                            readDb(10, "readDb:" + name)
                        },
                        {
                            doBizz(1, "doBizz:" + name)
                        }
                )
            }
        })

        t.start()
        sleep(1000)
        t.join()
        //sleep(1000_000_000L)
        println "------------finish test------------"

    }


    def readDb = { int seconds, String bzName ->
        execute(seconds,bzName)
    }

    def doBizz = { int seconds, String bzName ->
        execute(seconds,bzName)
    }

    def execute = { Integer seconds, String bzName ->
        println Thread.currentThread().name + " -- " + bzName + " --- sleep seconds :" + seconds
        sleep(seconds * 1000L)
    }



}
