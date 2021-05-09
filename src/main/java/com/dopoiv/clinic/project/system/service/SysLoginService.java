package com.dopoiv.clinic.project.system.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dopoiv.clinic.common.utils.JwtUtil;
import com.dopoiv.clinic.common.utils.SecurityUtil;
import com.dopoiv.clinic.project.admin.entity.Admin;
import com.dopoiv.clinic.project.admin.mapper.AdminMapper;
import com.dopoiv.clinic.project.user.entity.User;
import com.dopoiv.clinic.project.user.mapper.UserMapper;
import com.dopoiv.clinic.security.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @author doverwong
 * @date 2021/5/7 21:06
 */
@Component
public class SysLoginService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 管理员手机号密码登录
     *
     * @param phoneNumber 手机号
     * @param password 密码
     * @return User 用户信息
     */
    public User login(String phoneNumber, String password) {

        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getPhoneNumber, phoneNumber));

        if (ObjectUtil.isNotNull(user) && isAdmin(user)) {
            return user;
        }

        return null;
    }

    /**
     * 保存登录状态
     * @param user 用户实体
     * @return token 字符串
     */
    public String saveLoginStatus(User user) {
        UserDetails userDetails = new LoginUser(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginUser loginUser = SecurityUtil.getLoginUser();
        return jwtUtil.createToken(loginUser);
    }

    /**
     * 是否为管理员
     * @param user 用户
     * @return true | false
     */
    public boolean isAdmin(User user) {
        return adminMapper.selectCount(Wrappers.<Admin>lambdaQuery().eq(Admin::getUserId, user.getId())) > 0;
    }


}
