package com.yh.web.service.user;

import com.yh.common.domin.dto.LoginedParamDTO;
import com.yh.common.domin.entity.UserAdminEntity;
import com.yh.common.domin.entity.UserResourceEntity;
import com.yh.common.domin.entity.UserRoleEntity;
import com.yh.common.domin.vo.UserMenuNode;
import com.yh.common.domin.dto.UserAdminParam;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * @author : yh
 * @date : 2020/12/14 15:56
 */
public interface UserAdminService {

    UserDetails loadUserByUsername(String username);

    /**
     * 后台用户注册
     * @param param
     * @return
     */
    UserAdminEntity register(LoginedParamDTO<UserAdminParam> param);

    /**
     * 登录
     * @param param
     * @return
     */
    String login(LoginedParamDTO<UserAdminParam> param);

    /**
     * 查询全部资源
     */
    List<UserResourceEntity> listAll();

    /**
     * 查询用户
     * @param username 用户名
     * @return
     */
    UserAdminEntity getAdminByUsername(String username);

    /**
     * 查询菜单（路由）
     * @param userKey
     * @return
     */
    List<UserMenuNode> getMenuListByUserKey(Long userKey);

    /**
     * 查询角色
     * @param userKey
     * @return
     */
    UserRoleEntity getRoleListByUserKey(Long userKey);
}
