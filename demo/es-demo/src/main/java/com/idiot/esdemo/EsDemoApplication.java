package com.idiot.esdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *  es 的一些基础操作 因为之前只是简单的查询 对效率 以及内存结构了解不是很深入
 * @author wangxiao
 * @date 15:38 2021/2/2
 */
@SpringBootApplication
public class EsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsDemoApplication.class, args);
    }

}
