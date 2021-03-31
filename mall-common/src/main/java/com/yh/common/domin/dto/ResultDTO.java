package com.yh.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yh.common.enums.exception.ResponseCodeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author yh
 */
@Data
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultDTO<T> implements Serializable {

    private static final long serialVersionUID = 6619214563835101337L;

    @ApiModelProperty("返回码:0.成功;其他失败详情待定")
    private Integer code;

    @ApiModelProperty("返回说明")
    private String message;

    @ApiModelProperty("业务实体参数")
    private T entity;

    public static <T> ResultDTO<T> createSuccessDatagram(T entity) {
        ResultDTO<T> datagram = new ResultDTO<T>();
        datagram.setCode(ResponseCodeEnum.SUCCESS.getCode());
        datagram.setMessage(ResponseCodeEnum.SUCCESS.getMessage());
        datagram.setEntity(entity);
        return datagram;
    }

    public static <T> ResultDTO<T> createFailedDatagram(int code, String message, T entity) {
        ResultDTO<T> datagram = new ResultDTO<T>();
        datagram.setCode(code);
        datagram.setMessage(message);
        datagram.setEntity(entity);
        return datagram;
    }

    public static <T> ResultDTO<T> success() {
        ResultDTO<T> datagram = new ResultDTO<T>();
        datagram.setCode(ResponseCodeEnum.SUCCESS.getCode());
        datagram.setMessage(ResponseCodeEnum.SUCCESS.getMessage());
        return datagram;
    }

    public static <T> ResultDTO<T> successMsg(String msg) {
        ResultDTO<T> datagram = new ResultDTO<T>();
        datagram.setCode(ResponseCodeEnum.SUCCESS.getCode());
        datagram.setMessage(msg);
        return datagram;
    }

    public static <T> ResultDTO<T> successData(T data) {
        ResultDTO<T> datagram = new ResultDTO<T>();
        datagram.setCode(ResponseCodeEnum.SUCCESS.getCode());
        datagram.setMessage(ResponseCodeEnum.SUCCESS.getMessage());
        datagram.setEntity(data);
        return datagram;
    }

    public static <T> ResultDTO<T> createSuccess(ResponseCodeEnum responseCodeEnum) {
        ResultDTO<T> datagram = new ResultDTO<T>();
        datagram.setCode(responseCodeEnum.getCode());
        datagram.setMessage(responseCodeEnum.getMessage());
        return datagram;
    }

}
