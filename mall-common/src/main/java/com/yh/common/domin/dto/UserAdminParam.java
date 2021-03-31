package com.yh.common.domin.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : yh
 * @date : 2020/12/16 11:22
 */
@Data
public class UserAdminParam implements Serializable {

    private static final long serialVersionUID = 8549918012281569893L;
    private String nickName;

    private String userAccount;

    private String password;
}
