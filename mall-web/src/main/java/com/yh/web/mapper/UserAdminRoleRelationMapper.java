package com.yh.web.mapper;

import com.yh.common.domin.entity.UserAdminRoleRelationEntity;
import com.yh.common.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAdminRoleRelationMapper extends MyMapper<UserAdminRoleRelationEntity> {

    /**
     * 获取资源相关用户ID列表
     */
    List<Long> getAdminIdList(@Param("resourceKey") Long resourceKey);
}