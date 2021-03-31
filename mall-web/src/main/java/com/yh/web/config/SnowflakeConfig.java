package com.yh.web.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : yh
 * @date : 2021/3/31 22:28
 */
@Configuration
public class SnowflakeConfig {

    /**
     * 全局配置一个
     * 雪花算法生成分布式ID
     *
     * @return
     */
    @Bean
    public Snowflake snowflake() {
        return IdUtil.getSnowflake(1, 1);
    }
}
