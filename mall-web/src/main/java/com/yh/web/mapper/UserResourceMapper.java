package com.yh.web.mapper;

import com.yh.common.domin.entity.UserResourceEntity;
import com.yh.common.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserResourceMapper extends MyMapper<UserResourceEntity> {

    /**
     * 获取用户所有可访问资源
     * @param userKey
     * @return
     */
    List<UserResourceEntity> getResourceList(@Param("userKey") Long userKey);
}