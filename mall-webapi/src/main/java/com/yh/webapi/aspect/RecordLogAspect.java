package com.yh.webapi.aspect;

import com.yh.common.domin.dto.ResultDTO;
import com.yh.common.enums.exception.BusinessException;
import com.yh.common.enums.exception.ResponseCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author yh
 */
@Slf4j
@Component
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RecordLogAspect {

    @Pointcut("execution(* com.yh.webapi.controller..*.*(..))")
    public void log() {
    }

    @Around(value = "log()")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        String interfaceName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        Object[] args = joinPoint.getArgs();
        for (int i = 0, len = args.length; i < len; ++i) {
            sb.append("参数:[")
                    .append(i)
                    .append("]=")
                    .append(args[i])
                    .append(",");
        }
        sb.append("}");
        log.info("{}>>>>访问参数:{}", interfaceName, sb.toString());
        try {
            return joinPoint.proceed(joinPoint.getArgs());
        } catch (Exception ex) {
            if (ex instanceof BusinessException) {
                BusinessException bex = (BusinessException) ex;
                log.info("{}>>>>参数:{},错误:{}", interfaceName, sb.toString(), bex.getErrMsg());
                return ResultDTO.createFailedDatagram(bex.getErrCode(), bex.getErrMsg(), null);
            } else {
                log.error(interfaceName + ">>>>参数:" + sb.toString() + ",错误:" + ex.getMessage(), ex);
                return ResultDTO.createFailedDatagram(
                        ResponseCodeEnum.SYSTEM_EXCEPTION.getCode(),
                        ResponseCodeEnum.SYSTEM_EXCEPTION.getMessage(),
                        null
                );
            }
        }
    }
}
