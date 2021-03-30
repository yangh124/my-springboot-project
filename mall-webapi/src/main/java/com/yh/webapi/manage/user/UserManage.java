package com.yh.webapi.manage.user;

import com.yh.common.domin.entity.UserInfoEntity;

/**
 * @author : yh
 * @date : 2020/12/15 14:37
 */
public interface UserManage {

    UserInfoEntity getUserInfoByUserName(String userName);
}
