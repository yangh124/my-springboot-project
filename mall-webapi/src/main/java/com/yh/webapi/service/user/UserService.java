package com.yh.webapi.service.user;

import com.yh.common.domin.entity.UserInfoEntity;
import com.yh.webapi.config.UserInfoDetails;

/**
 * @author : yh
 * @date : 2020/12/14 15:56
 */
public interface UserService {

    /**
     * 获取用户信息
     *
     * @param username
     * @return
     */
    UserInfoDetails loadUserByUsername(String username);

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    UserInfoEntity getCurrentUserInfo();

}
