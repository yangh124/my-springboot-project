package com.yh.webapi.aspect;

import com.yh.common.enums.RedisKeyEnum;
import com.yh.common.enums.exception.BusinessException;
import com.yh.common.enums.exception.ResponseCodeEnum;
import com.yh.common.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by 79493 on 2018/9/20.
 */
@Slf4j
@Component
@Aspect
@Order
public class NoRepeatSubmitAspect {

    @Resource
    private RedisService redisService;

    @Before("@annotation(com.yh.common.annotation.NoRepeatSubmit)")
    public void beforeMethod(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
        HttpServletRequest request = attributes.getRequest();
        String key = sessionId + "-" + request.getServletPath();
        boolean b = redisService.setNX(RedisKeyEnum.S_REPEAT_SUBMIT.getName() + key, "0", 1L);
        if (!b) {
            throw new BusinessException(ResponseCodeEnum.SYSTEM_EXCEPTION.getCode(),"重复提交");
        }
    }
}
