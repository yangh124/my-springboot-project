package com.yh.common.domin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    private String key;

    private String token;

    private String version;

    private String currentTime;

    private String type;

}
