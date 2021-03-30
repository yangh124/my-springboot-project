package com.yh.webapi.service.user.impl;

import cn.hutool.core.util.ObjectUtil;
import com.yh.common.domin.entity.UserInfoEntity;
import com.yh.common.enums.exception.BusinessException;
import com.yh.common.enums.exception.ResponseCodeEnum;
import com.yh.webapi.config.UserInfoDetails;
import com.yh.webapi.manage.user.UserManage;
import com.yh.webapi.service.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : yh
 * @date : 2020/12/14 15:56
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserManage userManage;

    @Override
    public UserInfoDetails loadUserByUsername(String username) {
        UserInfoEntity userInfo = userManage.getUserInfoByUserName(username);
        if (ObjectUtil.isNotNull(userInfo)) {
            return new UserInfoDetails(userInfo);
        } else {
            return null;
        }
    }

    @Override
    public UserInfoEntity getCurrentUserInfo() {
        UserInfoEntity userInfo;
        try {
            SecurityContext ctx = SecurityContextHolder.getContext();
            Authentication auth = ctx.getAuthentication();
            UserInfoDetails userInfoDetails = (UserInfoDetails) auth.getPrincipal();
            userInfo = userInfoDetails.getUserInfo();
        } catch (ClassCastException e) {
            throw new BusinessException(ResponseCodeEnum.USER_ERROR);
        }
        if (null == userInfo) {
            throw new BusinessException(ResponseCodeEnum.USER_NOT_EXIST);
        }
        return userInfo;
    }


}
