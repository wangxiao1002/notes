# ThreadPoolExecutor
线程池为了避免线程的频繁创建和销毁带来性能消耗，使用化技术来使用线程
## ThreadPoolExecutor 的方式优于 Executors
```java
  public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
    }
   public static ExecutorService newSingleThreadExecutor() {
        return new FinalizableDelegatedExecutorService
            (new ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>()));
  }
 //  允许的请求队列长度为 Integer.MAX_VALUE，可能会堆积大量的请求，从而导致 OOM
 public LinkedBlockingQueue() {
        this(Integer.MAX_VALUE);
    } 
 // CachedThreadPool 和 ScheduledThreadPool：允许的创建线程数量为 Integer.MAX_VALUE，可能会创建大量的线程，从而导致 OOM。
 public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
 }
```
## 构造参数和含义
```java
    /**
     *  corePoolSize 核心线程数
     *  maximumPoolSize 最大线程数
     *  keepAliveTime 线程存活时间
     *  unit 时间单位
     *  workQueue 工作队列 当没有线程执行时候 放入工作队列
     *  threadFactory 线程创建工厂
     *  handler 线程池 拒绝策略
     */
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {
        // 检验maximumPoolSize>corePoolSize>0 和keepAliveTime >0
        if (corePoolSize < 0 || maximumPoolSize <= 0 || maximumPoolSize < corePoolSize || keepAliveTime < 0)
            throw new IllegalArgumentException();

       // 检验  workQueue threadFactory  handler 不能为null
        if (workQueue == null || threadFactory == null || handler == null)
            throw new NullPointerException();

        this.acc = System.getSecurityManager() == null ? null : AccessController.getContext();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
    }

```
## 线程池工作流程
* 任务进来先判断是否有空闲的核心线程，有则执行 否则存入工作队列 
* 判断是否可以存在工作队列中，能则存入工作队列 否存判断是否到达最大线程数
* 到达最大线程数 执行拒绝策略，否则开始创建新线程执行

## execute 执行任务
```java
  public void execute(Runnable command) {
        // 检查任务
        if (command == null)
            throw new NullPointerException();
        int c = ctl.get();
        
        // 判断是否大于核心线程数
        if (workerCountOf(c) < corePoolSize) {
            if (addWorker(command, true))
                return;
            c = ctl.get();
        }

        // 检查线程池是否处于运行状态，如果是则把任务添加到队列
        if (isRunning(c) && workQueue.offer(command)) {
            int recheck = ctl.get();
            if (! isRunning(recheck) && remove(command))
                reject(command);
            else if (workerCountOf(recheck) == 0)
                addWorker(null, false);
        }
        else if (!addWorker(command, false))
            // 拒绝
            reject(command);
    }

```
## submit 有返回值
```java
    public Future<?> submit(Runnable task) {
        if (task == null) throw new NullPointerException();
        RunnableFuture<Void> ftask = newTaskFor(task, null);
        execute(ftask);
        return ftask;
    }
```
## 拒绝策略
* ThreadPoolExecutor.AbortPolicy()  终止策略，线程池会抛出异常并终止执行，它是默认的拒绝策略
* CallerRunsPolicy，把任务交给当前线程来执行
* DiscardPolicy，忽略此任务
* DiscardOldestPolicy，忽略最早的任务
* 自定义拒绝策略
```java
 new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                // 重写拒绝业务代码
            }
}
```
## beforeExecute() 和 afterExecute()  
* 任务执行前和执行后
```java
public class MyThreadPoolExecutor extends ThreadPoolExecutor {
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        // 开始执行  可以假如业务代码
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
    }
}
```