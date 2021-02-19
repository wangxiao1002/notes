package com.xiao.demo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author wang xiao
 * @date Created in 15:24 2021/2/19
 */
public class TestSemaphoreDemo {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3,false);

        for (int i = 0; i < 6; i++) {
            new Thread(()->{
                try {
                    // 代表一辆车，已经占用了该车位
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName()+"\t抢到车位");
                    // 每个车停3秒
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "\t 离开车位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 释放停车位
                    semaphore.release();
                }
            }).start();
        }

    }
}
