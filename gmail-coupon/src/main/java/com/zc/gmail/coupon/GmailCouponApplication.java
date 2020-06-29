package com.zc.gmail.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.zc.gmail.coupon.dao")
@SpringBootApplication
public class GmailCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmailCouponApplication.class, args);
    }

}
