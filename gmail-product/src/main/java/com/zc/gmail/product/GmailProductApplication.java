package com.zc.gmail.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.zc.gmail.product.dao")
public class GmailProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmailProductApplication.class, args);
    }

}
