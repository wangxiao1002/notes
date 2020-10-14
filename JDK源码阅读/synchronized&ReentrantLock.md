# synchronized && ReentrantLock
## 区别
* synchronized属于关键字,ReentrantLock 是接口Lock实现类API
* synchronized属于独占式悲观锁，通过JVM硬式实现，synchronized只允许同一时刻只有一个线程操作资源。ReentrantLock 可设置公平锁,synchronized属于独占式悲观锁
* synchronized可以修饰方法，代码块，类对象
* ReentrantLock 需要手动加锁和释放锁
## synchronized 实现
