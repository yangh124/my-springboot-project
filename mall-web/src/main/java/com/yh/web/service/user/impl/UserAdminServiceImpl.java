package com.yh.web.service.user.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yh.security.util.JwtTokenUtil;
import com.yh.common.domin.dto.LoginedParamDTO;
import com.yh.common.domin.dto.params.user.UserAdminParam;
import com.yh.common.domin.entity.UserAdminEntity;
import com.yh.common.domin.entity.UserMenuEntity;
import com.yh.common.domin.entity.UserResourceEntity;
import com.yh.common.domin.entity.UserRoleEntity;
import com.yh.common.domin.vo.UserMenuNode;
import com.yh.common.enums.exception.BusinessException;
import com.yh.common.enums.exception.ResponseCodeEnum;
import com.yh.common.utils.validater.ParameterValidateUtil;
import com.yh.web.bo.AdminUserDetails;
import com.yh.web.manage.UserAdminManage;
import com.yh.web.service.user.UserAdminCacheService;
import com.yh.web.service.user.UserAdminService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : yh
 * @date : 2020/12/14 15:56
 */
@Service
public class UserAdminServiceImpl implements UserAdminService {

    @Resource
    private UserAdminManage userAdminManage;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserAdminCacheService userAdminCacheService;
    @Resource
    private JwtTokenUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserAdminEntity admin = getAdminByUsername(username);
        if (ObjectUtil.isNotNull(admin)) {
            Long userKey = admin.getUserKey();
            //从缓存中查询
            List<UserResourceEntity> resourceList = userAdminCacheService.getResourceList(userKey);
            if (CollUtil.isEmpty(resourceList)) {
                //缓存中没有，从数据库查询
                resourceList = userAdminManage.listResourceByUserKey(userKey);
                //存入缓存
                userAdminCacheService.setResourceList(userKey, resourceList);
            }
            return new AdminUserDetails(admin, resourceList);
        }
        throw new BusinessException(ResponseCodeEnum.USER_ACCOUNT_PASSWORD_ERROR);
    }

    @Override
    public UserAdminEntity register(LoginedParamDTO<UserAdminParam> param) {
        UserAdminParam entity = param.getEntity();
        ParameterValidateUtil.isNull(entity, "参数");
        String userAccount = entity.getUserAccount();
        ParameterValidateUtil.isBlank(userAccount, "账号");
        UserAdminEntity userAdminByAccount = userAdminManage.getUserAdminByAccount(userAccount);
        if (ObjectUtil.isNotNull(userAdminByAccount)) {
            throw new BusinessException(ResponseCodeEnum.USER_IS_EXIST);
        }
        String password = entity.getPassword();
        //ParameterValidateUtil.isNotLegalPassword(password);
        UserAdminEntity userAdminEntity = new UserAdminEntity();
        BeanUtils.copyProperties(entity, userAdminEntity);
        userAdminEntity.setPassword(passwordEncoder.encode(password));
        Boolean aBoolean = userAdminManage.addUserAdmin(userAdminEntity);
        if (aBoolean) {
            userAdminEntity.setPassword("");
            return userAdminEntity;
        } else {
            throw new BusinessException(ResponseCodeEnum.SYSTEM_EXCEPTION);
        }

    }

    @Override
    public String login(LoginedParamDTO<UserAdminParam> param) {
        UserAdminParam entity = param.getEntity();
        ParameterValidateUtil.isNull(entity, "参数");
        String username = entity.getUserAccount();
        ParameterValidateUtil.isBlank(username, "账号");
        String password = entity.getPassword();
        ParameterValidateUtil.isBlank(password, "密码");
        String token;
        //密码需要客户端加密后传递
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BusinessException(ResponseCodeEnum.USER_ACCOUNT_PASSWORD_ERROR);
            }
            if (!userDetails.isEnabled()) {
                throw new BusinessException(ResponseCodeEnum.USER_ERROR);
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtUtil.generateToken(userDetails);
//            updateLoginTimeByUsername(username);
            //insertLoginLog(username);
        } catch (AuthenticationException e) {
            throw new BusinessException(ResponseCodeEnum.SYSTEM_EXCEPTION);
        }
        return token;
    }

    @Override
    public List<UserResourceEntity> listAll() {
        return userAdminManage.listAll();
    }

    @Override
    public UserAdminEntity getAdminByUsername(String username) {
        UserAdminEntity entity = userAdminCacheService.getAdmin(username);
        if (ObjectUtil.isNotNull(entity)) {
            return entity;
        }
        entity = userAdminManage.getUserAdminByAccount(username);
        userAdminCacheService.setAdmin(entity);
        return entity;
    }

    @Override
    public List<UserMenuNode> getMenuListByUserKey(Long userKey) {
        List<UserMenuEntity> menuList = userAdminManage.getMenuListByUserKey(userKey);
        return menuList.stream()
                .filter(menu -> menu.getParentKey().equals(0L))
                .map(menu -> covertMenuNode(menu, menuList)).collect(Collectors.toList());
    }

    /**
     * 将UserMenu转化为UserMenuNode并设置children属性
     */
    private UserMenuNode covertMenuNode(UserMenuEntity menu, List<UserMenuEntity> menuList) {
        UserMenuNode node = new UserMenuNode();
        BeanUtils.copyProperties(menu, node);
        List<UserMenuNode> children = menuList.stream()
                .filter(subMenu -> subMenu.getParentKey().equals(menu.getMenuKey()))
                .map(subMenu -> covertMenuNode(subMenu, menuList)).collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }

    @Override
    public UserRoleEntity getRoleListByUserKey(Long userKey) {
        return userAdminManage.getRoleListByUserKey(userKey);
    }
}
