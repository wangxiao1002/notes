package com.idiot.esdemo.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wang xiao
 * @date Created in 15:54 2021/2/2
 */
@Configuration
public class EsConfig {

    final String hostList = "127.0.0.1:9200";

     // 高版本
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        HttpHost[] httpHostArray = spliteHost();
        return new RestHighLevelClient(RestClient.builder(httpHostArray));
    }



     // 低版本
    @Bean
    public RestClient restClient() {
        HttpHost[] httpHostArray = spliteHost();
        return RestClient.builder(httpHostArray).build();
    }


    private  HttpHost[] spliteHost() {
        String[] split = hostList.split(",");
        HttpHost[] httpHostArray = new HttpHost[split.length];
        for (int i = 0; i < split.length; i++) {
            String item = split[i];
            httpHostArray[i] = new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]), "http");
        }
        return httpHostArray;
    }

}
