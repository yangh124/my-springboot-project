package com.yh.security.component;

import cn.hutool.json.JSONUtil;
import com.yh.common.domin.dto.ResultDTO;
import com.yh.common.enums.exception.ResponseCodeEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义返回结果：自定义的授权异常处理
 *
 * @author yh
 */
public class WebAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.parse(JSONUtil.parse(ResultDTO.createFailedDatagram(ResponseCodeEnum.FORBIDDEN.getCode(),
                ResponseCodeEnum.FORBIDDEN.getMessage(), e.getMessage()))));
        response.getWriter().flush();
    }
}
