package com.dopoiv.clinic.security.service;

import com.dopoiv.clinic.security.LoginUser;
import com.dopoiv.clinic.sys.entity.User;
import com.dopoiv.clinic.sys.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author wangduofu
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userMapper.selectById(s);
        return createLoginUser(user);
    }

    public UserDetails createLoginUser(User user) {
        return new LoginUser(user);
    }
}
