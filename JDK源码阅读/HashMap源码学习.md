# HashMap 源码学习笔记
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
## 构造器

```java
public HashMap(int initialCapacity, float loadFactor) {
    // 校验初始容量
    if (initialCapacity < 0)
        throw new IllegalArgumentException("Illegal initial capacity: " +
                                           initialCapacity);
    if (initialCapacity > MAXIMUM_CAPACITY)
        initialCapacity = MAXIMUM_CAPACITY;
    // 校验负载因子
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
        throw new IllegalArgumentException("Illegal load factor: " +
                                           loadFactor);
    this.loadFactor = loadFactor;
    // 扩容阈值 2的n次方
    this.threshold = tableSizeFor(initialCapacity);
}

static final int tableSizeFor(int cap) {
    //  >>>表示无符号右移，也叫逻辑右移，即若该数为正，则高位补0
    // 111 右移2 001
    // | 位或运算  1010 | 0101 = 1111
    int n = cap - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
}

```

*****

## PUT

```java
 public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
 }
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    // table 为null 或空 进行初始化
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    // 判断hash 节点是否为空, 空则新建
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        Node<K,V> e; K k;
        // p的hash,key 都等于目标值 
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            // hash桶上第一个元素
            e = p;
        else if (p instanceof TreeNode)
            // 如果第一个节点是树 直接插入
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            // 循环hash桶链表
            for (int binCount = 0; ; ++binCount) {
                // 判断下一节点是为null
                if ((e = p.next) == null) {
                    // 为null 则开始新增next
                    p.next = newNode(hash, key, value, null);
                    if (binCount >= TREEIFY_THRESHOLD - 1) 
                        // -1 for 1st  判断是否大于>=8 开始转换成树
                        treeifyBin(tab, hash);
                    // 跳出循环
                    break;
                }
               // next 不为null 且key 相同 p=k
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        // 判断映射是否存在
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            // 是否覆盖
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    // 增加操作次数
    ++modCount;
    if (++size > threshold)
        // 扩容，调整
        resize();
    afterNodeInsertion(evict);
    return null;
}

// 扩容 resize
 final Node<K,V>[] resize() {
	 // old 
     Node<K,V>[] oldTab = table;
     // old Capacity
     int oldCap = (oldTab == null) ? 0 : oldTab.length;
     int oldThr = threshold;
     int newCap, newThr = 0;
     if (oldCap > 0) {
         // 判断临界值
         if (oldCap >= MAXIMUM_CAPACITY) {
             threshold = Integer.MAX_VALUE;
             return oldTab;
         }
         // 扩容 2倍
         else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                  oldCap >= DEFAULT_INITIAL_CAPACITY)
             newThr = oldThr << 1; // double threshold
     }else if (oldThr > 0) 
         // 初始容量处于阈值
         newCap = oldThr;
     else { 
         // 0 或者其他 使用默认值
         newCap = DEFAULT_INITIAL_CAPACITY;
         newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
     }
     if (newThr == 0) {
         //  判断并重新设置临界值
         float ft = (float)newCap * loadFactor;
         newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                   (int)ft : Integer.MAX_VALUE);
     }
     // 更新临界值
     threshold = newThr;
     @SuppressWarnings({"rawtypes","unchecked"})
     Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
     // 更新 table
     table = newTab;
     if (oldTab != null) {
         // 循环调整链表 和红黑树引用
         for (int j = 0; j < oldCap; ++j) {
             Node<K,V> e;
             if ((e = oldTab[j]) != null) {
                 // 数组节点不为空
                 oldTab[j] = null;
                 if (e.next == null)
                     // 只有一个节点 直接找hash 值
                     newTab[e.hash & (newCap - 1)] = e;
                 else if (e instanceof TreeNode)
                     // node 是树 树操作
                     ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                 else { 
                     // 调整 链表
                     Node<K,V> loHead = null, loTail = null;
                     Node<K,V> hiHead = null, hiTail = null;
                     Node<K,V> next;
                     do {
                         next = e.next;
                         if ((e.hash & oldCap) == 0) {
                             if (loTail == null)
                                 loHead = e;
                             else
                                 loTail.next = e;
                             loTail = e;
                         }
                         else {
                             if (hiTail == null)
                                 hiHead = e;
                             else
                                 hiTail.next = e;
                             hiTail = e;
                         }
                     } while ((e = next) != null);
                     if (loTail != null) {
                         loTail.next = null;
                         newTab[j] = loHead;
                     }
                     if (hiTail != null) {
                         hiTail.next = null;
                         newTab[j + oldCap] = hiHead;
                     }
                 }
             }
         }
     }
     return newTab;
 }
 
```
*****

## GET

```java
  public V get(Object key) {
     Node<K,V> e;
     return (e = getNode(hash(key), key)) == null ? null : e.value;
  }
  final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
        // table 不为空且first 不为空
        if ((tab = table) != null && (n = tab.length) > 0 &&
            (first = tab[(n - 1) & hash]) != null) {
            if (first.hash == hash && // 判断第一节点
                ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
                // 判断是否是treee
                if (first instanceof TreeNode)
                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
                do {
                    // 循环判断
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
  }
```
*****

## 其他重要方法

* values： 返回所有V 的Collection

* entrySet: 返回Set<Map.Entry<K,V>> ,常用于遍历

* getOrDefault： 获取value获取不到赋一个默认值，并不会改变Map

* putIfAbsent: 存在直接返回，不put,不存在和put 一样

* V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction)： 不存在则去函数里面的值，并put 类似 :
```
   Object key = map.get("key");
  if (key == null) {
    key = new Object();
    map.put("key", key);
  }
```
* computeIfPresent(K key,BiFunction<? super K, ? super V, ? extends V> remappingFunction) 同上存在则操作Map
* compute 同上不管存不存在都会操作Map
* V merge(K key, V value,BiFunction<? super V, ? super V, ? extends V> remappingFunction)  merge 它将新的值赋值到 key （如果不存在）或更新给定的key 值对应的 value


