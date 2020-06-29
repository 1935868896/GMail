package com.zc.gmail.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zc.gmail.ware.dao")
public class GmailWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmailWareApplication.class, args);
    }

}
