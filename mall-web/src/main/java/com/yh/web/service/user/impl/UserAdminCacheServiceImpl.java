package com.yh.web.service.user.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yh.common.domin.entity.UserAdminEntity;
import com.yh.common.domin.entity.UserAdminRoleRelationEntity;
import com.yh.common.domin.entity.UserResourceEntity;
import com.yh.common.enums.RedisKeyEnum;
import com.yh.web.manage.UserAdminManage;
import com.yh.web.service.redis.IRedisService;
import com.yh.web.service.user.UserAdminCacheService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : yh
 * @date : 2020/12/18 14:44
 */
@Service
public class UserAdminCacheServiceImpl implements UserAdminCacheService {

    @Resource
    private IRedisService iRedisService;
    @Resource
    private UserAdminManage userAdminManage;

    @Override
    public void delAdmin(Long userKey) {
        String key = RedisKeyEnum.GOS_USER_ADMIN.getName() + ":" + userKey;
        iRedisService.remove(key);
    }

    @Override
    public void delResourceList(Long userKey) {
        String key = RedisKeyEnum.GOS_USER_RESOURCE.getName() + ":" + userKey;
        iRedisService.remove(key);
    }

    @Override
    public void delResourceListByRole(Long roleId) {
        UserAdminRoleRelationEntity relation = userAdminManage.getUserAdminRoleRelationByRoleKey(roleId);
        if (ObjectUtil.isNotNull(relation)) {
            String key = RedisKeyEnum.GOS_USER_RESOURCE.getName() + ":" + relation.getUserKey();
            iRedisService.remove(key);
        }
    }

    @Override
    public void delResourceListByRoleIds(List<Long> roleIds) {
        List<UserAdminRoleRelationEntity> relationList =
                userAdminManage.getUserAdminRoleRelationByRoleKeys(roleIds);
        if (CollUtil.isNotEmpty(relationList)) {
            String keyPrefix = RedisKeyEnum.GOS_USER_RESOURCE.getName() + ":";
            List<String> keys = relationList.stream().map(relation -> keyPrefix + relation.getUserKey()).collect(Collectors
                    .toList());
            iRedisService.remove(keys);
        }
    }

    @Override
    public void delResourceListByResource(Long resourceKey) {
        List<Long> adminIdList = userAdminManage.getAdminIdList(resourceKey);
        if (CollUtil.isNotEmpty(adminIdList)) {
            String keyPrefix = RedisKeyEnum.GOS_USER_RESOURCE.getName() + ":";
            List<String> keys = adminIdList.stream().map(userKey -> keyPrefix + userKey).collect(Collectors.toList());
            iRedisService.remove(keys);
        }
    }

    @Override
    public UserAdminEntity getAdmin(String  username) {
        String key = RedisKeyEnum.GOS_USER_ADMIN.getName() + ":" + username;
        return (UserAdminEntity) iRedisService.get(key);
    }

    @Override
    public void setAdmin(UserAdminEntity admin) {
        String key = RedisKeyEnum.GOS_USER_ADMIN.getName() + ":" + admin.getUserAccount();
        iRedisService.set(key, admin, 86400L);//24小时
    }

    @Override
    public List<UserResourceEntity> getResourceList(Long userKey) {
        String key = RedisKeyEnum.GOS_USER_RESOURCE.getName() + ":" + userKey;
        return (List<UserResourceEntity>) iRedisService.get(key);
    }

    @Override
    public void setResourceList(Long userKey, List<UserResourceEntity> resourceList) {
        String key = RedisKeyEnum.GOS_USER_RESOURCE.getName() + ":" + userKey;
        iRedisService.set(key, resourceList, 86400L);//24小时
    }
}
