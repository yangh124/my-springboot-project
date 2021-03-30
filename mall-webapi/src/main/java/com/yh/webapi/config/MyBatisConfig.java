package com.yh.webapi.config;

import com.yh.common.mybatis.MyMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * mybatis配置
 * @author : yh
 * @date : 2020/12/11 11:42
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {"com.yh.webapi.dao"}, markerInterface = MyMapper.class)
public class MyBatisConfig {
}
