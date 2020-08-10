# String 源码

## 重要参数
```java
 private final char value[]; // 字符存储
 private int hash; // 缓存字符串的hashCode 值
```
## 构造参数
```java

  /**
    * 初始化一个空字符串
    */
    public String() {
        this.value = "".value;
    }
  /**
    * 初始化字符串 将目标字符串值 copy 过来
    */
    public String(String original) {
        this.value = original.value;
        this.hash = original.hash;
    }

  /**
    * char[] 为参数构造方法
    */
    public String(char value[]) {
           this.value = Arrays.copyOf(value, value.length);
    }
  /**
    * StringBuffer作为构造参数,因为Buffer是可变的 添加synchronized关键字 
    */
    public String(StringBuffer buffer) {
        synchronized(buffer) {
            this.value = Arrays.copyOf(buffer.getValue(), buffer.length());
        }
    }
   /**
        * StringBuilder 做构造参数 StringBuilder 本身不是线程安全的 故不需要你添加 
     */
    public String(StringBuilder builder) {
        this.value = Arrays.copyOf(builder.getValue(), builder.length());
    }
    
```
## 扩展
* StringBuffer 的append()方法添加了synchronized关键字是线程安全的，StringBuilder 不是线程安全
* append () 方法都是调用父类AbstractStringBuilder的append()方法，该方法char[] 数组中添加了字符串的cahr[]
调用方法是 
```java// 将源字符串数组copy的指定长度copy到目标字符串
System.arraycopy(value, srcBegin, dst, dstBegin, srcEnd - srcBegin);
```
## equals() 方法 判断字符是否相等
```java
   public boolean equals(Object anObject) {
        // 首先判断引用地址是否相同
        if (this == anObject) {
            return true;
        }
        // 判断 类型
        if (anObject instanceof String) {
            String anotherString = (String)anObject;
            int n = value.length;
            // 判断长度 是否一致
            if (n == anotherString.value.length) {
                char v1[] = value;
                char v2[] = anotherString.value;
                int i = 0;
                while (n-- != 0) {
                    // 循环体做判断 有一个不相等 直接返回
                    if (v1[i] != v2[i])
                        return false;
                    i++;
                }
                return true;
            }
        }
        return false;
    }

```
## compareTo() 比较两个字符串 返回 int
```java
   public int compareTo(String anotherString) {
        int len1 = value.length;
        int len2 = anotherString.value.length;
        // 取两个长度的最小值 
        int lim = Math.min(len1, len2);
        char v1[] = value;
        char v2[] = anotherString.value;

        int k = 0;
        while (k < lim) {
            // 循环比较 最小字符串里面的字符串  有不相等直接返回字符差值
            char c1 = v1[k];
            char c2 = v2[k];
            if (c1 != c2) {
                return c1 - c2;
            }
            k++;
        }
        // 前者比较通过 直接返回长度差值
        return len1 - len2;
    }
``` 
*  compareTo  长度相等且字符相等时候返回0，
## 字符串常量池
* 因为final 修饰String 是不可变 且安全，为了快速返回字符串 提供了字符串池
* 创建字符串时候 如果字符串常量池中已经存在字符串，直接返回语句柄
```java
    // 判断常量池是否存在 hello 存在返回没有则创建 
    String str1 = "hello";
    // 常量池中已经存在直接返回
    String str2 = "hello";
    System.out.println(str1==str2);//true


    // 判断常量池中是否存在hello 存在并返回 不存在新建
    String str1 = "hello";
    // 判断常量池中是否存在  存在旧跳过 不存在则新建，完成后（跳过或新建）在堆内存中新建 hello并指向str2
    String str2 = new String("hello");
    System.out.println(str1==str2);//false


    String str1 = "helloworld";
    // + 拼接会在编译时候变成整体 
    String str2 = "hello"+"world";
    System.out.println(str1==str2);//true

 
    String str1 = "helloworld";
    String str2 = "hello";
    // 带变量的 拼接在编译时期不会存在编译成整体，因为变量值是可变的
    String str3 = str2+"world";
    System.out.println(str1==str3);//false


```
## intern()入池操作
* intern入池操作，会先检查常量池中是否有str1对应的”hello”，若没有则直接创建，并返回常量池中”hello”的引用。若存在，则直接返回常量池中”hello”的引用
* 相比于new String  的跳过
```java

    String str1 = new String("hello").intern();
    String str2 = "hello";
    System.out.println(str1==str2);//true

    String str1 = new String("Java");
    // 执行入池操作 但是与Str1 的Heap 空间已经没太大联系（不严谨） 
    String str2 = str1.intern();
    String str3 = "Java"
    str1 !=  str2
    str2 == str3
```