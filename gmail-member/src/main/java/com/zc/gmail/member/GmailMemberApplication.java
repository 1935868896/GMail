package com.zc.gmail.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.zc.gmail.member.dao")
@SpringBootApplication
public class GmailMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmailMemberApplication.class, args);
    }

}
