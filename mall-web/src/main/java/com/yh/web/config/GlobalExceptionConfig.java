package com.yh.web.config;

import com.yh.common.domin.dto.ResultDTO;
import com.yh.common.enums.exception.BusinessException;
import com.yh.common.enums.exception.ResponseCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author : yh
 * @date : 2020/12/19 11:38
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionConfig {

    /**
     * 自定义异常
     *
     * @param e 自定义异常
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    public ResultDTO<String> handle(BusinessException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return ResultDTO.createFailedDatagram(e.getErrCode(), e.getErrMsg(), e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResultDTO<String> handle(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return ResultDTO.createFailedDatagram(ResponseCodeEnum.SYSTEM_EXCEPTION.getCode(),
                ResponseCodeEnum.SYSTEM_EXCEPTION.getMessage(), e.getMessage());
    }
}
