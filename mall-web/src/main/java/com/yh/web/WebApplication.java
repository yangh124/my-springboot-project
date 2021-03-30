package com.yh.web;

import com.yh.common.redis.impl.RedisServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * @author : yh
 * @date : 2020/12/10 14:05
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@Import(RedisServiceImpl.class)
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
