package com.xiao.demo.threadorder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * @author wang xiao
 * @date Created in 19:13 2021/2/19
 */
public class ThreadOrder {

    static boolean t2Run = false;
    static boolean t3Run = false;

    
    /**
     *  线程 按照顺序执行的方法
     */
    public static void main(String[] args) throws InterruptedException {

        /**
         * 1 使用线程join 方法
         * join(): 是 Theard 实例的方法，作用是调用线程需等待该 join() 线程执行完成后，才能继续用下运行。
         */

        Thread one = new Thread(()->{
            System.out.println("早上");
        });
        Thread two = new Thread(()->{
            try {
                one.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("中午");
        });
        Thread three = new Thread(()->{
            try {
                two.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("晚上");
        });


        one.start();
        two.start();
        three.start();


        /**
         * 2. 使用 主线程的join 方法
         */
        Thread one1 = new Thread(()->{
            System.out.println("早上1");
        });
        Thread two1 = new Thread(()->{

            System.out.println("中午1");
        });
        Thread three1 = new Thread(()->{

            System.out.println("晚上1");
        });

        one1.start();
        one1.join();
        two1.start();
        two1.join();
        three1.start();
        three1.join();

        /**
         * 使用wait 方法
         * wait(): 是 Object 的方法，作用是让当前线程进入等待状态，同时，wait() 也会让当前线程释放它所持有的锁。
         * “直到其他线程调用此对象的 notify() 方法或 notifyAll() 方法”，当前线程被唤醒 (进入 “就绪状态”)
         * notify() 和 notifyAll(): 是 Object 的方法，作用则是唤醒当前对象上的等待线程；notify() 是唤醒单个线程，而 notifyAll() 是唤醒所有的线程。
         * wait(long timeout): 让当前线程处于 “等待(阻塞) 状态”，“直到其他线程调用此对象的 notify()方法或 notifyAll() 方法，或者超过指定的时间量”，当前线程被唤醒(进入“就绪状态”)。
         */



        /**
         * 使用 CountDownLatch （倒计数）
         * CountDownLatch主要有两个方法：countDown()和await()。countDown()方法用于使计数器减一，其一般是执行任务的线程调用，
         * await()方法则使调用该方法的线程处于等待状态，其一般是主线程调用。这里需要注意的是，
         * countDown()方法并没有规定一个线程只能调用一次，当同一个线程调用多次countDown()方法时，每次都会使计数器减一；
         * 另外，await()方法也并没有规定只能有一个线程执行该方法，如果多个线程同时执行await()方法，那么这几个线程都将处于等待状态，并且以共享模式享有同一个锁
         */
         CountDownLatch c1 = new CountDownLatch(1);

        /** 用于判断线程二是否执行，倒计时设置为1，执行后减1 */
         CountDownLatch c2 = new CountDownLatch(1);

        final Thread thread1 = new Thread(() -> {
            System.out.println("产品经理规划新需求");
            // 对c1倒计时-1
            c1.countDown();
        });
        c1.await();
        final Thread thread2 = new Thread(() -> {
            try {
                // 等待c1倒计时，计时为0则往下运行
                c1.await();
                System.out.println("开发人员开发新需求功能");
                // 对c2倒计时-1
                c2.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread3 = new Thread(() -> {
            try {
                // 等待c2倒计时，计时为0则往下运行
                c2.await();
                System.out.println("测试人员测试新功能");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        thread3.start();

        thread1.start();

        thread2.start();


    }


}
