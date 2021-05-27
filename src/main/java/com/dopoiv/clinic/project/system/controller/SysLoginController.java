package com.dopoiv.clinic.project.system.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dopoiv.clinic.common.tools.BaseController;
import com.dopoiv.clinic.common.utils.JwtUtil;
import com.dopoiv.clinic.common.utils.SecurityUtil;
import com.dopoiv.clinic.common.utils.WechatUtil;
import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.project.admin.dto.LoginBody;
import com.dopoiv.clinic.project.system.service.SysLoginService;
import com.dopoiv.clinic.project.user.entity.User;
import com.dopoiv.clinic.project.user.mapper.UserMapper;
import com.dopoiv.clinic.redis.RedisCache;
import com.dopoiv.clinic.security.LoginUser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

    @Value("${wx.mini.appid}")
    private String appid;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiOperation(value = "管理员登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phoneNumber", paramType = "form", value = "手机号", required = true),
            @ApiImplicitParam(name = "password", paramType = "form", value = "密码", required = true)
    })
    @PostMapping("/login")
    public R getLoginToken(@RequestBody LoginBody loginBody) {

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

    @ApiOperation(value = "微信登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", paramType = "String", value = "code", required = true),
            @ApiImplicitParam(name = "rawData", paramType = "String", value = "rawData", required = true)
    })
    @PostMapping("/wxlogin")
    public R wxLogin(String code, String rawData, String encryptedData, String iv, String signature) {

        // 使用 appId + appSecret + code 登录凭证校验接口，获取session_key和openid
        JSONObject sessionKeyOpenId = WechatUtil.getSessionKeyOrOpenId(code);
        // 获取返回的参数 session_key 和 openid
        String openid = sessionKeyOpenId.getString("openid");
        String sessionKey = sessionKeyOpenId.getString("session_key");

        // 校验签名是否一致
        String signature1 = DigestUtils.sha1Hex(rawData + sessionKey);
        if (!signature1.equals(signature)) {
            return R.error("登录校验失败");
        }

        // 解密用户手机号信息
        JSONObject phoneNumberData = JSON.parseObject(WechatUtil.decrypt(encryptedData, sessionKey, iv));
        // 校验 appid
        String appid1 = phoneNumberData.getJSONObject("watermark").getString("appid");
        if (!appid1.equals(appid)) {
            return R.error("登录校验失败");
        }

        // 判断用户是否是新用户，是的话，将用户信息存到数据库；不是的话，更新最新登录时间
        User user = userMapper.selectById(openid);
        // 生成token，用于维护微信小程序用户与服务端的会话
        if (user == null) {
            logger.debug("新用户");
            // 保存用户公开信息
            JSONObject rawDataJson = JSON.parseObject(rawData);
            // 信息保存为user对象
            user = JSONObject.parseObject(rawDataJson.toJSONString(), User.class);
            user.setId(openid);
            user.setOpenId(openid)
                    .setCreateTime(LocalDateTime.now())
                    .setLastVisitTime(LocalDateTime.now())
                    .setSessionKey(sessionKey)
                    .setPhoneNumber(phoneNumberData.getString("phoneNumber"))
                    .setPurePhoneNumber(phoneNumberData.getString("purePhoneNumber"))
                    .setCountryCode(phoneNumberData.getString("countryCode"))
                    .setStatus(1);
            // 生成 token，并保存用户登录信息
            user.setToken(this.getLoginToken(user));
            userMapper.insert(user);
        } else {
            logger.debug("用户已存在，更新信息");
            // 用户已存在，更新用户登录时间
            user.setLastVisitTime(LocalDateTime.now());
            // 重新设置会话token
            user.setToken(this.getLoginToken(user));
            userMapper.updateById(user);
        }

        return R.data(user);
    }

    /**
     * 获得登录令牌
     *
     * @param user 用户
     * @return {@link String}
     */
    public String getLoginToken(User user) {
        UserDetails userDetails = new LoginUser(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                AuthorityUtils.createAuthorityList("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginUser loginUser = SecurityUtil.getLoginUser();
        return jwtUtil.createToken(loginUser);
    }
}
