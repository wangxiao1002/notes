package com.example.demo.doc.code;

import java.util.Stack;

/**
 * 数组实现栈
 * @author wang xiao
 * @date Created in 14:49 2021/3/9
 */
public class MyArrayStack<V> {

    private Object [] values;

    private volatile int top =-1;

    public MyArrayStack(int capacity) {
        Stack<String> s = new Stack<>();
        values = new Object[capacity];
    }

    public V push (V item) {
        values[top+1] = item;
        top+=1;
        return item;
    }

    public V pop () {
        top-=1;
        return (V) values[top+1];
    }
}
