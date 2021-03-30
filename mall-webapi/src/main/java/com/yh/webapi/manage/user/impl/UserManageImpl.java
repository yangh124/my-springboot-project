package com.yh.webapi.manage.user.impl;

import com.yh.common.domin.entity.UserInfoEntity;
import com.yh.common.enums.ValidTypeEnum;
import com.yh.webapi.dao.UserInfoDao;
import com.yh.webapi.manage.user.UserManage;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @author : yh
 * @date : 2020/12/15 14:38
 */

@Component
public class UserManageImpl implements UserManage {

    @Resource
    private UserInfoDao userInfoDao;

    @Override
    public UserInfoEntity getUserInfoByUserName(String userName) {
        Example example = new Example(UserInfoEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deleted", ValidTypeEnum.VALID.getCode()).andEqualTo("userName", userName);
        return userInfoDao.selectOneByExample(example);
    }
}
