# ConcurrentHashMap

*实现的是ConcurrentMap接口*

*实现思路：ConcurrentHashMap内分为多个Segment,每一个Segment拥有锁（继承于ReentrantLock）*

## 常量

* 最大桶容量 ：private static final int MAXIMUM_CAPACITY = 1 << 30;

* 默认桶容量：private static final int DEFAULT_CAPACITY = 16;

* 最大数组大小：static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

* 并发级别，用来控制Segment的数量。private static final int DEFAULT_CONCURRENCY_LEVEL = 16;

* 其余扩容，转换树、负载因子，最小树容量和HashMap 一致

* 最小转换幅度 ：private static final int MIN_TRANSFER_STRIDE = 16;

  ```java
  /**
       * The number of bits used for generation stamp in sizeCtl.
       * Must be at least 6 for 32bit arrays.
       */
      private static int RESIZE_STAMP_BITS = 16;
  
      /**
       * The maximum number of threads that can help resize.
       * Must fit in 32 - RESIZE_STAMP_BITS bits.
       */
      private static final int MAX_RESIZERS = (1 << (32 - RESIZE_STAMP_BITS)) - 1;
  
      /**
       * The bit shift for recording size stamp in sizeCtl.
       */
      private static final int RESIZE_STAMP_SHIFT = 32 - RESIZE_STAMP_BITS;
  
      /*
       * Encodings for Node hash fields. See above for explanation.
       */
      static final int MOVED     = -1; // hash for forwarding nodes
      static final int TREEBIN   = -2; // hash for roots of trees
      static final int RESERVED  = -3; // hash for transient reservations
      static final int HASH_BITS = 0x7fffffff; // usable bits of normal node hash
  ```

  # sizeCtl

  volatile 修饰标识数组初始化和扩容

  ```xml
  当前未初始化:
  	= 0  //未指定初始容量
  	> 0  //由指定的初始容量计算而来，再找最近的2的幂次方。
  		//比如传入6，计算公式为6+6/2+1=10，最近的2的幂次方为16，所以sizeCtl就为16。
  初始化中：
  	= -1 //table正在初始化
  	= -N //N是int类型，分为两部分，高15位是指定容量标识，低16位表示
  	     //并行扩容线程数+1
  初始化完成：
  	=table.length * 0.75  //扩容阈值调为table容量大小的0.75倍
  
  
  ```

  

  
