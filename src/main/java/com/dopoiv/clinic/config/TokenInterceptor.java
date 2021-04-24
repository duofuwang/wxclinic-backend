package com.dopoiv.clinic.config;

import com.dopoiv.clinic.common.utils.JwtUtil;
import com.dopoiv.clinic.project.user.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author doverwong
 * @date 2021/2/25 15:05
 */
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 前置拦截方法
     *
     * @param request 请求
     * @param response 响应
     * @param handler handler
     * @return 放行:true 拦截：false
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        // 1、从请求头中获取token
//        String token = request.getHeader("token");
//
//        // 2、判断 token 是否存在
//        if (token == null || "".equals(token)) {
//            // 这里可以自定义 抛出 token 异常
//            throw new TokenRuntimeException("未登录");
//        }
//
//        // 3、解析token
//        Claims claim = jwtUtil.getClaimsByToken(token);
//
//        if (null == claim) {
//            // 这里可以自定义 抛出 token 异常
//            throw new TokenRuntimeException("token 解析错误");
//        }
//
//        // 4、判断 token 是否过期
//        Date expiration = claim.getExpiration();
//        boolean tokenExpired = jwtUtil.isTokenExpired(expiration);
//        if (tokenExpired) {
//            // 这里可以自定义 抛出 token 异常
//            throw new TokenRuntimeException("token已过期，请重新登录");
//        }
//
//        // 5、 从 token 中获取信息
//        String subject = claim.getSubject();
//        String openid = claim.get("openid").toString();
//        String sessionKey = claim.get("sessionKey").toString();
//
//        logger.debug("subject -----> " + subject);
//        logger.debug("openid -----> " + openid);
//        logger.debug("sessionKey -----> " + sessionKey);
//
//        // 6、去数据库中匹配 id 是否存在
//        User user = userMapper.selectById(openid);
//        if (user == null || !user.getOpenId().equals(openid) && !user.getSessionKey().equals(sessionKey) && !user.getToken().equals(token)) {
//            // 这里可以自定义 抛出 token 异常
//            throw new TokenRuntimeException("请求异常");
//        }
//
//        // 7、成功后 设置想设置的属性
////        request.setAttribute("userId", subject);
////        request.setAttribute("userName", "张三");

        return true;
    }
}
