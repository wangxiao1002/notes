# Thread 线程状态码和工作流程
* 线程是程序执行的最小单元,依赖于进程而存在. 一个进程中包含多个线程，多线程可以共享一块内存空间和一组系统资源
## 线程状态
```java
  public enum State {
        /**
         * 线程刚被创建，尚未启动状态
         */
        NEW,

        /**
         * 就绪状态，线程可以执行,但执行需要cpu调度
         */
        RUNNABLE,

        /**
         * 阻塞状态 
         * 正在等待监视器锁或者等待执行 synchronized 代码快或者标记的资源 
         */
        BLOCKED,

        /**
         * 等待状态,一个处于等待状态的线程正在等待另一个线程执行某一个特定的动作，比如一个线程调用了Object.wait()方法，
         * 那么它等待Object.notify()或者Object.notifyAll()
         */
        WAITING,

        /**
         * 计时等待，到期自动进入RUNNABLE状态。
         * 调用了 以下方法或进入该状态  Object.wait(long timeout)/ thread.join(long timeout)
         */
        TIMED_WAITING,

        /**
         * 完成状态
         */
        TERMINATED;
    }
```
## 线程的工作流程
* 首先创建线程，并指定业务代码，然后调用start()方法 线程状态由 NEW --->RUNNABLE 
* 线程在调度执行过程中，首先判断是否有synchronized同步代码块，如果有并且其他线程正在占用该监视器，则进入BLOCKED状态，当锁释放则会继续执行
* 当前线程遇到object.wait()或者thread.join()方法，但只会释放 object 这个对象的同步锁，线程进入WAITING/TIMED_WAITING状态
* 当线程被Object.notify()/Object.notifyAll()唤醒后会自动继续执行接下来代码，一直到状态是TERMINATED
