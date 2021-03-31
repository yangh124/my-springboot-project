package com.yh.common.domin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;


/**
 * @author yh
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginedParamDTO<T> extends LoginedParamBaseDTO implements Serializable {


    private static final long serialVersionUID = 494950671335977503L;
    private T entity;

}
