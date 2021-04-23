JVM 启动中 一般会添加一些参数来指定信息 比如 堆大小  原空间 大小 这些比较基础，
-Xms1024m 堆内存 初始内存  一般为1/16
-Xmx1024m 堆内存 最大内存 一般为1/4
-XX:PermSize设置非堆内存初始值
-XX:MaxPermSize设置最大非堆内存的大小，默认是物理内存的1/4
-Xmn2G：设置年轻代大小为2G。
-XX:+UseG1GC 使用G1收集器 
## 错误信息收集
 -XX:+HeapDumpOnOutOfMemoryError OOM 错误
 -XX:HeapDumpPath=*/java.hprof  保存dump 
 -XX:ErrorFile=/var/log/hs_err_pid<pid>.log  JDK错误日志保存地方，这个文件的内容他主要有如下内容

日志头文件
导致 crash 的线程信息
所有线程信息
安全点和锁信息
堆信息
本地代码缓存
编译事件
gc 相关记录
jvm 内存映射
jvm 启动参数
服务器信息
