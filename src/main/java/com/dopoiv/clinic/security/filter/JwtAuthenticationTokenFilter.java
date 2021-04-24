package com.dopoiv.clinic.security.filter;

import cn.hutool.core.util.ObjectUtil;
import com.dopoiv.clinic.common.utils.JwtUtil;
import com.dopoiv.clinic.common.utils.SecurityUtil;
import com.dopoiv.clinic.security.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token 过滤器 验证 token 有效性
 *
 * @author doverwong
 * @date 2021/4/20 14:24
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 缓存中获取登录用户
        LoginUser loginUser = jwtUtil.getLoginUser(request);

        if (ObjectUtil.isNotNull(loginUser) && ObjectUtil.isNull(SecurityUtil.getAuthentication())) {
            // 缓存中存在用户信息
            jwtUtil.verifyToken(loginUser);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // 保存到上下文
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        chain.doFilter(request, response);
    }
}
