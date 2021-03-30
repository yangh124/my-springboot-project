package com.yh.common.domin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 *  on 2017/3/22.
 * @author yh
 */
@Data
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnLoginedParamDTO<T> extends UnLoginedParamBaseDTO implements Serializable {

    private static final long serialVersionUID = -445775406091265610L;

    @ApiModelProperty("业务实体参数")
    private T entity;

}
