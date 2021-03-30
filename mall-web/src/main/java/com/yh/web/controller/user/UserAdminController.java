package com.yh.web.controller.user;

import cn.hutool.core.util.ObjectUtil;
import com.yh.common.domin.dto.LoginedParamDTO;
import com.yh.common.domin.dto.ResultDTO;
import com.yh.common.domin.dto.params.user.UserAdminParam;
import com.yh.common.domin.entity.UserAdminEntity;
import com.yh.common.domin.entity.UserRoleEntity;
import com.yh.common.enums.exception.BusinessException;
import com.yh.common.enums.exception.ResponseCodeEnum;
import com.yh.web.service.user.UserAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : yh
 * @date : 2020/12/16 11:08
 */
@Api(tags = "后台用户模块")
@RestController
@Slf4j
@RequestMapping("/userAdmin")
public class UserAdminController {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Resource
    private UserAdminService userAdminService;

    @ApiOperation("用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResultDTO<UserAdminEntity> register(@RequestBody LoginedParamDTO<UserAdminParam> param) {
        UserAdminEntity userAdminEntity = userAdminService.register(param);
        return ResultDTO.successData(userAdminEntity);
    }

    @ApiOperation(value = "登录以后返回token")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultDTO login(@RequestBody LoginedParamDTO<UserAdminParam> param) {
        String token = userAdminService.login(param);
        Map<String, String> tokenMap = new HashMap<>(2);
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return ResultDTO.successData(tokenMap);
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ResultDTO getAdminInfo(Principal principal) {
        if (ObjectUtil.isNull(principal)) {
            throw new BusinessException(ResponseCodeEnum.UNAUTHORIZED);
        }
        String username = principal.getName();
        UserAdminEntity umsAdmin = userAdminService.getAdminByUsername(username);
        Map<String, Object> data = new HashMap<>(4);
        data.put("username", umsAdmin.getUserAccount());
        data.put("menus", userAdminService.getMenuListByUserKey(umsAdmin.getUserKey()));
        data.put("nickName", umsAdmin.getNickName());
        UserRoleEntity role = userAdminService.getRoleListByUserKey(umsAdmin.getUserKey());
        data.put("role", role);
        return ResultDTO.successData(data);
    }

}
