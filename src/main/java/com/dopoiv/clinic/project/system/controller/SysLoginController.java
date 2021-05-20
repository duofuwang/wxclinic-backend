package com.dopoiv.clinic.project.system.controller;

import cn.hutool.core.util.ObjectUtil;
import com.dopoiv.clinic.common.tools.BaseController;
import com.dopoiv.clinic.common.utils.JwtUtil;
import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.project.admin.dto.LoginBody;
import com.dopoiv.clinic.project.system.service.SysLoginService;
import com.dopoiv.clinic.project.user.entity.User;
import com.dopoiv.clinic.project.user.mapper.UserMapper;
import com.dopoiv.clinic.redis.RedisCache;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author doverwong
 * @date 2021/5/7 21:04
 */
@RestController
public class SysLoginController extends BaseController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SysLoginService loginService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private JwtUtil jwtUtil;

    @ApiOperation(value = "管理员登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phoneNumber", paramType = "form", value = "手机号", required = true),
            @ApiImplicitParam(name = "password", paramType = "form", value = "密码", required = true)
    })
    @PostMapping("/login")
    public R login(@RequestBody LoginBody loginBody) {

        User user = loginService.login(loginBody.getPhoneNumber(), loginBody.getPassword());

        if (ObjectUtil.isNotNull(user)) {
            if (user.getStatus() == 0) {
                return R.error("抱歉，账号已被禁用TvT");
            }
            if (redisCache.hasKey(jwtUtil.getTokenKey(user.getToken()))) {
                return R.data(user);
            }
            user.setToken(loginService.saveLoginStatus(user));
            userMapper.updateById(user);
            return R.data(user);
        }

        return R.error("用户名或密码错误");
    }
}
