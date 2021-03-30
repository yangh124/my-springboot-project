package com.yh.security.component;

import cn.hutool.json.JSONUtil;
import com.yh.common.domin.dto.ResultDTO;
import com.yh.common.enums.exception.ResponseCodeEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义返回结果：自定义的认证异常处理
 *
 * @author yh
 */
public class WebAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.parse(ResultDTO.createFailedDatagram(ResponseCodeEnum.UNAUTHORIZED.getCode(),
                ResponseCodeEnum.UNAUTHORIZED.getMessage(), authException.getMessage())));
        response.getWriter().flush();
    }
}
