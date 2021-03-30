package com.yh.web.service.user;

import com.yh.common.domin.entity.UserAdminEntity;
import com.yh.common.domin.entity.UserResourceEntity;

import java.util.List;

/**
 * 后台用户缓存操作类
 */
public interface UserAdminCacheService {
    /**
     * 删除后台用户缓存
     * （修改、删除用户时需要清除缓存）
     */
    void delAdmin(Long  userKey);

    /**
     * 删除后台用户资源列表缓存
     * （修改、删除用户资源时需要清除缓存）
     */
    void delResourceList(Long  userKey);

    /**
     * 当角色相关资源信息改变时删除相关后台用户缓存
     */
    void delResourceListByRole(Long roleKey);

    /**
     * 当角色相关资源信息改变时删除相关后台用户缓存
     */
    void delResourceListByRoleIds(List<Long> roleKeys);

    /**
     * 当资源信息改变时，删除资源项目后台用户缓存
     */
    void delResourceListByResource(Long resourceKey);

    /**
     * 获取缓存后台用户信息
     */
    UserAdminEntity getAdmin(String  username);

    /**
     * 设置缓存后台用户信息
     */
    void setAdmin(UserAdminEntity admin);

    /**
     * 获取缓存后台用户资源列表
     */
    List<UserResourceEntity> getResourceList(Long userKey);

    /**
     * 设置后台后台用户资源列表
     */
    void setResourceList(Long userKey, List<UserResourceEntity> resourceList);
}
