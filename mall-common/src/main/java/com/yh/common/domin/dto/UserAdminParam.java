package com.yh.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author : yh
 * @date : 2020/12/16 11:22
 */
@Data
public class UserAdminParam implements Serializable {

    private static final long serialVersionUID = 8549918012281569893L;
    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("账号")
    private String userAccount;

    @ApiModelProperty("密码")
    private String password;
}
