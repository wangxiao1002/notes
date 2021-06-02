# JVM 信息 和问答

## JVM 内存结构

* 本地方法栈

* Java 虚拟机栈：java线程栈，基于线程，用来字节码运行

* 堆 ：java中共享空间，创建的对象，用-Xms -Xmx 进行大小设置

* 原空间 ：包含方法区 静态变量存放 类信息

* 程数计数器：指向字节码执行编号

## JVM 内存模型
抽象一种内存模型，将底层的硬件和操作系统区别区分开来，主要分为 主内存和工作内存两块区域，工作内存只能拿到主内存中副本，两线程的通信只能依靠主内存
## JVM 如何回收垃圾，GCRoots 是什么
* JVM 回收垃圾靠的可达性分析算法。
* JVM 从GCRoots来判断对象是否存活，从GCRoots 向下追溯，搜索产生一个叫做Reference Chain ，当一个对象与任意一个GCRoots 关联时候判断为垃圾对象
* GC Roots主要包括 线程活动中的引用(本地方法栈中的参数，局部变量)，类的静态变量，JNI等
## 找到Reference Chain 一定存活吗
Reference Chain 一共分为以下几类
* 强引用 ，普通对象之间的应用
* 弱引用 ，维护一些可有可无的引用对象，只有内存不够时候才会回收，如果回收还是空间不足，则会OOM
* 软引用，当JVM回收时候一定会回收
## JVM 基本参数
1. -XX:+PrintFlagFinal 参数可以查询JVM 默认值
2. Xmx, Xms ,Xmn, MetaspaceSize
## 内存溢出OOM 和排查OOM
* OOM 可以在堆栈中都可能发生，一般是堆空间一处，
*  Jstat 可以查看 JVM空间的大小，jmap 导出堆照 使用MAT分析
## GC 垃圾回收算法和收集器
1. 常见的算法有标记清除，标记整理，复制算法等
2. Mintor GC  对于年轻代主要有Serial ParNew,使用算法是: 标记清除,标记整理
3. Majoor GC 对于老年代主要是Serial CMS 等 主要算法是 复制算法 标记整理
## CPU 爆满排查方案
1. top -H 命令获取使用CPU最高的线程，并将它转换为16进制
2. jstack 获取对信息，查询16进制
## JVM 自带命令
* jps 显示java 进程
* jstack 用来dump 栈
* jmap 用来dump 堆
* jstat  用来查看gc
## 双亲委派
* 当收到一个类加载信息时候优先交给父类去完成加载，没有加载后再自己去加载
* 优先加载jkd 类信息，防止冲突吧
## GC 过程
* 堆分为老年代和年轻代 比例 2：1
* 年轻代中分为 eden ,to Survivor,From Survivor 比例 8：1：1
* eden 满了之后出发minor GC 存活的会被移动From Survivor,当eden 再次满了之后会触发复制算法，将这俩快区域一起回收,存活下来的会被移动到to
## minor GC ,Major GC ,FullGc 什么时候出发
* Mintor GC 是年轻代空间不足触发
* Major GC 是老年代GC 一般会伴随 Minor GC
* Full GC 触发情况 当老年代无法再分配内存，原空间内存不够
## 类加载过程
连接+验证+准备+解析+初始化

