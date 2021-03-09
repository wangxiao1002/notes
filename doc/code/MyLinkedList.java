package com.example.demo.doc.code;

/**
 * @author wang xiao
 * @date Created in 11:30 2021/3/9
 */
public class MyLinkedList<V> {


    private Node<V> head;

    private Node<V> current;

    private static  class Node<V> {
        private final V value;
        private Node<V> next;

        public Node(V value) {
            this.value = value;
        }
    }

    public V add (V v){
        if (null == head) {
            head = new Node<>(v);
            current = head;
            return v;
        }
        current.next = new Node<>(v);
        current = current.next;
        return v;
    }

    public V get () {
        return current.value;
    }

    public V remove (V v) {
        Node<V> cur = head,next = null,prev = null;
        while (cur!= null){
            next = cur.next;
            if (v.equals(cur.value)){
                break;
            }
            prev = cur;
            cur = cur.next;
        }
        if (null != prev) {
            prev.next = next;
        }else {
            this.head = next;
        }
        return v;
    }
}
