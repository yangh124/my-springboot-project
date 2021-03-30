package com.yh.webapi.study;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 监听器
 *
 * @author : yh
 * @date : 2021/3/29 21:26
 */
@Component
public class LearnListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        System.out.println("监听器获得容器中初始化Bean数量：" + applicationContext.getBeanDefinitionCount());
    }
}
