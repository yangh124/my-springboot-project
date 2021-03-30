package com.yh.web.manage;

import com.yh.common.domin.entity.*;

import java.util.List;

/**
 * @author : yh
 * @date : 2020/12/15 20:02
 */
public interface UserAdminManage {

    UserAdminEntity getUserAdminByAccount(String userAccount);

    /**
     * 查询用户资源
     *
     * @param userKey
     * @return
     */
    List<UserResourceEntity> listResourceByUserKey(Long userKey);

    /**
     * 查询全部资源
     */
    List<UserResourceEntity> listAll();

    /**
     * 添加后台用户
     *
     * @param userAdminEntity
     * @return
     */
    Boolean addUserAdmin(UserAdminEntity userAdminEntity);


    List<UserMenuEntity> getMenuListByUserKey(Long userKey);


    UserRoleEntity getRoleListByUserKey(Long userKey);

    UserAdminRoleRelationEntity getUserAdminRoleRelationByRoleKey(Long roleKey);

    List<UserAdminRoleRelationEntity> getUserAdminRoleRelationByRoleKeys(List<Long> roleKeys);

    /**
     * 获取资源相关用户ID列表
     */
    List<Long> getAdminIdList(Long resourceKey);
}
