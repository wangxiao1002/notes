# synchronized && ReentrantLock
## 区别
* synchronized属于关键字,ReentrantLock 是接口Lock实现类API
* synchronized属于独占式悲观锁，通过JVM硬式实现，synchronized只允许同一时刻只有一个线程操作资源。ReentrantLock 可设置公平锁,synchronized属于独占式悲观锁
* synchronized可以修饰方法，代码块，类对象
* ReentrantLock 需要手动加锁和释放锁
## synchronized 实现
* 每一个对象内部都会有一个Monitor监视器，获取锁和释放锁都是对Monitor的竞争
## 公平锁和非公平锁
* 公平锁是按照请求顺序来获取锁
* 非公平锁是允许“插队“，插队指的是当前线程获取锁时候如果锁是可用，直接获取
## ReentrantLock
* ReentrantLock 内部是通过sync.lock()方法实现加锁，sync是一个抽象类，类实现就有 FairSync /  NonfairSync
* 构造参数
```java
 /**
     * Creates an instance of {@code ReentrantLock}.
     * This is equivalent to using {@code ReentrantLock(false)}.
     */
    public ReentrantLock() {
        // 非公平锁
        sync = new NonfairSync();
    }

    /**
     * Creates an instance of {@code ReentrantLock} with the
     * given fairness policy.
     *
     * @param fair {@code true} if this lock should use a fair ordering policy
     */
    public ReentrantLock(boolean fair) {
        // 参数判定是否是公平锁
        sync = fair ? new FairSync() : new NonfairSync();
    }
```