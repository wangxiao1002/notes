package com.example.demo.doc.code;

/**
 * 链表 栈
 * @author wang xiao
 * @date Created in 15:01 2021/3/9
 */
public class MyNodeStack<V> {

    private Node<V> top;

    private static  class Node<V> {
        private final V value;
        private Node<V> next;

        public Node(V value) {
            this.value = value;
        }
    }

    public V push (V v){
        Node<V> node = new Node<V>(v);
        if (null == top){
            top = node;
            return v;
        }else {
            node.next = top;
            top = node;
        }
        return v;
    }

    public V pop () {
        V v= top.value;
        top = top.next;
        return v;
    }
}
