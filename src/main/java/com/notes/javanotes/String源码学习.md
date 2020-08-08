# String 源码

## 重要参数
```java
 private final char value[]; // 存储结构
 private int hash; // hash 值
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
    
```