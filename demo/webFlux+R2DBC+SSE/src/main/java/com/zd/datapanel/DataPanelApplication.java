package com.zd.datapanel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *  中德数据看板
 * @author wangxiao
 * @date 10:43 2021/1/26
 */
@EnableScheduling
@SpringBootApplication
public class DataPanelApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataPanelApplication.class, args);
    }

}
