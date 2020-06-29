package com.zc.gmail.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

@MapperScan("com.zc.gmail.member.dao")
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = "com.zc.gmail.member.fegin" )
public class GmailMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmailMemberApplication.class, args);
    }

}
