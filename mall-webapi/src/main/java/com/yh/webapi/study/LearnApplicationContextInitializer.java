package com.yh.webapi.study;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;

/**
 * 初始化器
 * @author : yh
 * @date : 2021/3/29 21:12
 */
@Order
public class LearnApplicationContextInitializer implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        System.out.println("容器中的Bean数量：" + configurableApplicationContext.getBeanDefinitionCount());
        String[] beanDefinitionNames = configurableApplicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }
}
