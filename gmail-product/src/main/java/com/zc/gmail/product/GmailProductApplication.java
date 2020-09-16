package com.zc.gmail.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
/**导入RedisHttpSessionConfiguration配置
 *1.给容器中添加一个组件
 *RedisIndexedSessionRepository redis操作session增删改查
 *SessionRepositoryFilter==>filter
 *
 * request.setAttribute(SESSION_REPOSITORY_ATTR, this.sessionRepository);
 *         SessionRepositoryFilter<S>.SessionRepositoryRequestWrapper wrappedRequest = new SessionRepositoryFilter.SessionRepositoryRequestWrapper(request, response);
 *         SessionRepositoryFilter.SessionRepositoryResponseWrapper wrappedResponse = new SessionRepositoryFilter.SessionRepositoryResponseWrapper(wrappedRequest, response);
 *
 *         try {
 *             filterChain.doFilter(wrappedRequest, wrappedResponse);
 *         } finally {
 *             wrappedRequest.commitSession();
 *         }
 * */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.zc.gmail.product.dao")
@EnableFeignClients(basePackages = "com.zc.gmail.product.feign")
public class GmailProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmailProductApplication.class, args);
    }

}
