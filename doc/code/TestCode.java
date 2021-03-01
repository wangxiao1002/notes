package com.example.demo.doc.code;

/**
 * @author wang xiao
 * @date Created in 19:05 2021/3/1
 */
public class TestCode {

    public static void main(String[] args) {
        System.out.println("main");
        findMax();
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



}
