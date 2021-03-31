package com.yh.web.manage.impl;

import cn.hutool.core.lang.Snowflake;
import com.yh.common.domin.entity.*;
import com.yh.common.enums.ValidTypeEnum;
import com.yh.web.manage.UserAdminManage;
import com.yh.web.mapper.*;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : yh
 * @date : 2020/12/15 20:02
 */
@Component
public class UserAdminManageImpl implements UserAdminManage {

    @Resource
    private UserAdminMapper userAdminMapper;
    @Resource
    private UserResourceMapper userResourceMapper;
    @Resource
    private UserMenuMapper userMenuMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private UserAdminRoleRelationMapper userAdminRoleRelationMapper;
    @Resource
    private Snowflake snowflake;

    @Override
    public UserAdminEntity getUserAdminByAccount(String userAccount) {
        Example example = new Example(UserAdminEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deleted", ValidTypeEnum.VALID.getCode()).andEqualTo("userAccount", userAccount);
        return userAdminMapper.selectOneByExample(example);
    }

    @Override
    public List<UserResourceEntity> listResourceByUserKey(Long userKey) {

        return userResourceMapper.getResourceList(userKey);
    }

    @Override
    public List<UserResourceEntity> listAll() {
        Example example = new Example(UserResourceEntity.class);
        example.createCriteria().andEqualTo("deleted", ValidTypeEnum.VALID.getCode());
        return userResourceMapper.selectByExample(example);
    }

    @Override
    public Boolean addUserAdmin(UserAdminEntity userAdminEntity) {
        Long nextId = snowflake.nextId();
        userAdminEntity.setUserKey(nextId);
        return userAdminMapper.insertSelective(userAdminEntity) > 0;
    }

    @Override
    public List<UserMenuEntity> getMenuListByUserKey(Long userKey) {
        return userMenuMapper.getMenuListByUserKey(userKey);
    }

    @Override
    public UserRoleEntity getRoleListByUserKey(Long userKey) {
        return userRoleMapper.getRoleListByUserKey(userKey);
    }

    @Override
    public UserAdminRoleRelationEntity getUserAdminRoleRelationByRoleKey(Long roleKey) {
        Example example = new Example(UserAdminRoleRelationEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deleted", ValidTypeEnum.VALID.getCode()).andEqualTo("roleKey", roleKey);
        return userAdminRoleRelationMapper.selectOneByExample(example);
    }

    @Override
    public List<UserAdminRoleRelationEntity> getUserAdminRoleRelationByRoleKeys(List<Long> roleKeys) {
        Example example = new Example(UserAdminRoleRelationEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deleted", ValidTypeEnum.VALID.getCode()).andIn("roleKey", roleKeys);
        return userAdminRoleRelationMapper.selectByExample(example);
    }

    @Override
    public List<Long> getAdminIdList(Long resourceKey) {
        return userAdminRoleRelationMapper.getAdminIdList(resourceKey);
    }


}
