package com.xiao.demo;

import java.util.concurrent.CountDownLatch;

/**
 * @author wang xiao
 * @date Created in 19:01 2021/2/19
 */
public class TestCountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        new Thread(()->{
            try {
                //第一个线程被阻塞
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.err.println("其他线程等待");
        }).start();
        for (int i=0;i<10;i++){
            Thread.sleep(2000);
            new Thread(()->{
                //其他线程执行countDown(),当count值减少到0，上边等待的线程将被释放
                System.out.println(Thread.currentThread().getName());
                countDownLatch.countDown();
            }).start();
        }
    }
}
