package com.yh.web.mapper;

import com.yh.common.domin.entity.UserRoleEntity;
import com.yh.common.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface UserRoleMapper extends MyMapper<UserRoleEntity> {

    /**
     * @param userKey
     * @return
     */
    UserRoleEntity getRoleListByUserKey(@Param("userKey") Long userKey);
}