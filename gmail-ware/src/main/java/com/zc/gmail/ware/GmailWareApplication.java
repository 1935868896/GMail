package com.zc.gmail.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.zc.gmail.ware.feign")
@MapperScan("com.zc.gmail.ware.dao")
public class GmailWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmailWareApplication.class, args);
    }

}
