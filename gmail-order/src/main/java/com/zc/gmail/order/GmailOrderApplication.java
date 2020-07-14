package com.zc.gmail.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@MapperScan("com.zc.gmail.order.dao")
@EnableDiscoveryClient
public class GmailOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmailOrderApplication.class, args);
    }

}
