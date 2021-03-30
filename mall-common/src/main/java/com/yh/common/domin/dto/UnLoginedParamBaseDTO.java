package com.yh.common.domin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 *  on 2017/3/22.
 */
@Data
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnLoginedParamBaseDTO implements Serializable {

    private static final long serialVersionUID = 376238899360624269L;

    @ApiModelProperty("用户当前时间")
    private String currentTime;

    @ApiModelProperty("版本号")
    private String version;
}
