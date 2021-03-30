package com.yh.web.mapper;

import com.yh.common.domin.entity.UserMenuEntity;
import com.yh.common.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMenuMapper extends MyMapper<UserMenuEntity> {


    /**
     * @param userKey
     * @return
     */
    List<UserMenuEntity> getMenuListByUserKey(@Param("userKey") Long userKey);
}