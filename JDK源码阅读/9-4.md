# HashTable

## HashTable变量

* 数据存储结构：private transient Entry<?,?>[] table;
* private int threshold; 扩容阈值（capacity * loadFactor，与HashMap一致）
* private float loadFactor; 负载因子
* private transient int modCount = 0; 结构操作次数
*****
## 构造参数

```java
 public Hashtable(int initialCapacity, float loadFactor) {
     // check 容量
     if (initialCapacity < 0)
         throw new IllegalArgumentException("Illegal Capacity: "+
                                            initialCapacity);
     // check  负载因子
     if (loadFactor <= 0 || Float.isNaN(loadFactor))
         throw new IllegalArgumentException("Illegal Load:"+loadFactor);

     if (initialCapacity==0)
         initialCapacity = 1;
     this.loadFactor = loadFactor;
     table = new Entry<?,?>[initialCapacity];
     threshold = (int)Math.min(initialCapacity *loadFactor,
                               MAX_ARRAY_SIZE + 1);
 }

private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

```

* 与HashMap 不同处：

  1、默认值不一致 ，HashMap是16，0.75，HashTable是11，0.75

  2、负载因子初始化值不一致

* table 结构不同，在于HashMap是有TreeNode,HashTable只有链表

* hash算法取值不一致，HashMap(n-1)&Hash(key),HashTable((hash(key)&0x7fffffff))%tab.length)

* HashMap允许null,HashTable不允许

* HashMap 非线程安全，HashTable线程安全
*****

  ## put

```java
// synchronized 同步锁 加在方法上表示锁当前实例 
public synchronized V put(K key, V value) {
        // value 不能够为空
        if (value == null) {
            throw new NullPointerException();
        }

        // Makes sure the key is not already in the hashtable.
        Entry<?,?> tab[] = table;
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % tab.length;
        // table 下标 index
        @SuppressWarnings("unchecked")
        // 获取下表位置 entry 
        Entry<K,V> entry = (Entry<K,V>)tab[index];
    	// 循环
        for(; entry != null ; entry = entry.next) {
            // 取代存在的值
            if ((entry.hash == hash) && entry.key.equals(key)) {
                V old = entry.value;
                entry.value = value;
                return old;
            }
        }
		// 添加
        addEntry(hash, key, value, index);
        return null;
}
  private void addEntry(int hash, K key, V value, int index) {
        // 操作次数+1
      	modCount++;

        Entry<?,?> tab[] = table;
        // 当前数量大于 加载阈值
        if (count >= threshold) {
            // Rehash the table if the threshold is exceeded
            // 重新计算hash
            rehash();

            tab = table;
            hash = key.hashCode();
            index = (hash & 0x7FFFFFFF) % tab.length;
        }

        // Creates the new entry.
        @SuppressWarnings("unchecked")
       // 获取桶上的值
        Entry<K,V> e = (Entry<K,V>) tab[index];
       // 将放入的一个置为第一个。原先是放入的next 
        tab[index] = new Entry<>(hash, key, value, e);
        count++;
    }
```

## rehash 扩容重新计算Hash

```java
protected void rehash() {
    // 储备旧值
    int oldCapacity = table.length;
    Entry<?,?>[] oldMap = table;

    // 扩容2倍
    int newCapacity = (oldCapacity << 1) + 1;
    if (newCapacity - MAX_ARRAY_SIZE > 0) {
        if (oldCapacity == MAX_ARRAY_SIZE)
            // Keep running with MAX_ARRAY_SIZE buckets
            return;
        newCapacity = MAX_ARRAY_SIZE;
    }
    Entry<?,?>[] newMap = new Entry<?,?>[newCapacity];

    modCount++;
    threshold = (int)Math.min(newCapacity * loadFactor, MAX_ARRAY_SIZE + 1);
    table = newMap;
    // 循环桶
    for (int i = oldCapacity ; i-- > 0 ;) {
        // 循环链表
        for (Entry<K,V> old = (Entry<K,V>)oldMap[i] ; old != null ; ) {
            Entry<K,V> e = old;
            old = old.next;

            int index = (e.hash & 0x7FFFFFFF) % newCapacity;
            e.next = (Entry<K,V>)newMap[index];
            // 重新指向地址
            newMap[index] = e;
        }
    }
}
```

## get

```java
//   synchronized
public synchronized V get(Object key) {
        Entry<?,?> tab[] = table;
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % tab.length;
       // 循环节点
        for (Entry<?,?> e = tab[index] ; e != null ; e = e.next) {
            if ((e.hash == hash) && e.key.equals(key)) {
                // return
                return (V)e.value;
            }
        }
        return null;
    }
```

