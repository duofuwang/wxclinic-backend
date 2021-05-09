package com.dopoiv.clinic.security.handler;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.dopoiv.clinic.common.constant.HttpStatus;
import com.dopoiv.clinic.common.utils.JwtUtil;
import com.dopoiv.clinic.common.utils.ServletUtil;
import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.security.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author doverwong
 * @date 2021/5/7 20:47
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 退出处理
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        LoginUser loginUser = jwtUtil.getLoginUser(request);
        if (ObjectUtil.isNotNull(loginUser)) {
            // 删除用户缓存
            jwtUtil.deleteLoginUser(loginUser.getToken());
        }
        ServletUtil.renderString(response, JSON.toJSONString(R.success(HttpStatus.SUCCESS, "退出成功")), 200);
    }
}
