package com.yh.common.domin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class LoginedParamBaseDTO implements Serializable {

    private static final long serialVersionUID = 2324190652588158157L;

    @ApiModelProperty("用户唯一标识")
    private String key;

    @ApiModelProperty("用户令牌")
    private String token;

    @ApiModelProperty("版本号")
    private String version;

    @ApiModelProperty("用户当前时间")
    private String currentTime;

    @ApiModelProperty("接口类型")
    private String type;

}
