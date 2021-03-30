package com.yh.common.redis;

import com.netflix.hystrix.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * <pre>
 *
 * @title : 对redis进行限流处理的command
 * @description :
 * </pre>
 */
public class RedisHystrixCommand<T> extends HystrixCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisHystrixCommand.class);

    private Callable<T> callable;

    private String methodName;

    private String cacheKey;

    public RedisHystrixCommand(Callable<T> callable, String methodName){
        this(callable,methodName,false,null);
    }

//    private static HystrixCommandKey commandKey = HystrixCommandKey.Factory.asKey("test");
//
//    public static void flushCache(String cacheKey) {
//        HystrixRequestCache.getInstance(commandKey, HystrixConcurrencyStrategyDefault.getInstance()).clear(cacheKey);
//    }

    public RedisHystrixCommand(Callable<T> callable, String methodName, boolean isCache, String cacheKey) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("redis_group_key"))
                .andCommandKey(HystrixCommandKey.Factory.asKey(String.format("%s_%s", "redis_command_key_", methodName)))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000)
                        .withCircuitBreakerEnabled(true)
                        //一个统计窗口内熔断触发的最小个数
                        .withCircuitBreakerRequestVolumeThreshold(50)
                        .withCircuitBreakerSleepWindowInMilliseconds(2000)
                        .withCircuitBreakerErrorThresholdPercentage(50)
                        .withFallbackIsolationSemaphoreMaxConcurrentRequests(100)
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                        .withRequestCacheEnabled(isCache)
                        .withMetricsHealthSnapshotIntervalInMilliseconds(5000))
                // .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withCircuitBreakerRequestVolumeThreshold(100)
                // .withCircuitBreakerErrorThresholdPercentage(50)
                // .withCircuitBreakerSleepWindowInMilliseconds(1000))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("redis_thread"))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(Runtime.getRuntime().availableProcessors() * 2).withMaxQueueSize(500)
                        .withQueueSizeRejectionThreshold(350)));
        this.callable = callable;
        this.methodName = methodName;
        this.cacheKey = cacheKey;
    }

    @Override
    protected String getCacheKey() {
        return this.cacheKey;
    }

    @Override
    protected T run() throws Exception {
        if (callable == null) {
            return null;
        }
        return callable.call();
    }

    @Override
    protected T getFallback() {
        LOGGER.error( "redis.RedisHystrixCommand===========redis=======fallback!!!!!!!!!!!! the executed method is:"
                + methodName + " cache key is:" + cacheKey,getFailedExecutionException());
        return null;
    }
}
