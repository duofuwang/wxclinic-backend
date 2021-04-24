package com.dopoiv.clinic.security.handler;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.dopoiv.clinic.common.constant.HttpStatus;
import com.dopoiv.clinic.common.utils.ServletUtil;
import com.dopoiv.clinic.common.web.domain.R;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author doverwong
 * @date 2021/4/23 20:01
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        int code = HttpStatus.UNAUTHORIZED;
        String msg = StrUtil.format("请求访问：{}，认证失败", request.getRequestURI());
        ServletUtil.renderString(response, JSON.toJSONString(R.error(code, msg)), code);
    }
}