#HashMap 源码学习笔记
##  Hash 算法 
*  指 将任意长度的输入通过Hash算法转换成固定长度的输出，这个输出的值就是Hash值
## HashMap 常见常量
* 初始化Hash桶数量：static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; 
* 最大容量：  static final int MAXIMUM_CAPACITY = 1 << 30;
* 默认负载因子： static final float DEFAULT_LOAD_FACTOR = 0.75f; // 3/4
* 数组转树 阈值（树化）：static final int TREEIFY_THRESHOLD = 8;
* 树转数组 阈值 （数退化）： static final int UNTREEIFY_THRESHOLD = 6;
* 最小树阈值： static final int MIN_TREEIFY_CAPACITY = 64;
## HashMap 常见变量
*  transient Node<K,V>[] table; 存储节点数组
*  transient int modCount; HashMap结构修改次数
*  transient int size; key-value 映射的大小
*  int threshold; 扩容阈值 (capacity * load factor)容量×负载因子
*  final float loadFactor; 负载因子
