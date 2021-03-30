package com.yh.webapi;

import com.yh.common.redis.impl.RedisServiceImpl;
import com.yh.webapi.study.LearnApplicationContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * @author : yh
 * @date : 2020/12/11 09:51
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@Import(RedisServiceImpl.class)
public class WebApiApplication {

    public static void main(String[] args) {
        //SpringApplication.run(WebApiApplication.class, args);
        SpringApplication springApplication = new SpringApplication(WebApiApplication.class);
        springApplication.addInitializers(new LearnApplicationContextInitializer());
        springApplication.run(args);
    }

}
