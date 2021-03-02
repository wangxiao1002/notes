package com.example.demo.doc.code;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author wang xiao
 * @date Created in 19:05 2021/3/1
 */
public class TestCode {

    public static void main(String[] args) {
       

    }

    private static void countKind () {
        // 2 元 3元 7 元  能有多种组成 100 方式
        int count = 0;
        for (int i =0;i<=(100/7);i++) {
            for (int j =0;j<=(100/3);j++){
               if ((100-7*i-j*3>=0)&&(100-7*i-j*3)%2==0){
                   count+=1;
               }
            }
        }
        System.out.println(count);
    }
    static int[] array = new  int[] {1,2,3,4,5,5,6};


    private static void  findMax () {
        // 数组里面寻找最大出现次数
        int var_max = -1;
        int var_temp = 0;
        int var_count = 0;
        for (int i =0;i<array.length;i++) {
            var_count = 1;
            for (int j=0;j<array.length;j++){
                if (array[i] == array [j]) {
                    var_count+=1;
                }
            }
            if (var_count > var_temp) {
                var_max = array[i];
                var_temp = var_count;
            }
        }
        System.out.println(var_max);
        System.out.println(var_count);
    }


    private static void findMaxByKV () {
        Map<Integer,Integer> result = new HashMap<>(8);
        for (int i : array) {
            result.compute(i,(k,v)->{
                if (Objects.isNull(v)) {
                    v = 1;
                }else {
                    v+=1;
                }
                return  v;
            });
        }

        for (Map.Entry<Integer,Integer> entry: result.entrySet()){
            System.out.println("key is "+ entry.getKey()+" value is "+entry.getValue());
        }
    }

    /**
     *   数组反转 直接赋值
     * @author wangxiao
     * @date 14:20 2021/3/2
     * @param array targetArray
     * @return int[]
     */
    public static int[] reverseArray (int [] array) {
        final int length = array.length;
        int [] result = new int[length];
        for (int i= 0;i<length;i++) {
            result[i] = array[length-i-1];
        }
        return result;
    }

    static class Node<V> {

        private V value;

        private Node<V> nextNode;

        public Node(V value, Node<V> nextNode) {
            this.value = value;
            this.nextNode = nextNode;
        }
    }

    /**
     *  单向链表反转 需要构造三个元素  prev、curr 和 next
     *  while(curr){
     *     next = curr.next;
     *     curr.next = prev；
     *     prev = curr;
     *     curr = next;
     * }
     * @author wangxiao
     * @date 14:21 2021/3/2
     * @param head 头
     * @return com.example.demo.doc.code.TestCode.Node<java.lang.Integer>
     */
    public static Node<Integer> reverseNode (Node<Integer> head) {
        Node<Integer> prev = null,curr = head,next;
        while (null != curr) {
            next = curr.nextNode;
            curr.nextNode = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }


    







}
