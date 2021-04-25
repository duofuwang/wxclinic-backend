package com.dopoiv.clinic.project.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.tools.BaseController;
import com.dopoiv.clinic.common.utils.JwtUtil;
import com.dopoiv.clinic.common.utils.SecurityUtil;
import com.dopoiv.clinic.common.utils.WechatUtil;
import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.project.user.entity.User;
import com.dopoiv.clinic.project.user.mapper.UserMapper;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 控制器类
 * </p>
 *
 * @author wangduofu
 * @since 2021-04-24
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${wx.mini.appid}")
    private String appid;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取User信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public R page(
            Integer pageNum,
            Integer pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        User params = new User();
        QueryWrapper<User> wrapper = new QueryWrapper<>(params);

        return R.data(userMapper.selectPage(page, wrapper));
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部User信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public R getAllItems() {
        User params = new User();
        QueryWrapper<User> wrapper = new QueryWrapper<>(params);
        List<User> userList = userMapper.selectList(wrapper);

        return R.data(userList);
    }

    @ApiOperation(value = "保存修改User信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public R save(@RequestBody User entity) {
        if (entity.getId() == null) {
            userMapper.insert(entity);
        } else {
            userMapper.updateById(entity);
        }
        return R.success();
    }

    @ApiOperation(value = "按id删除User，可以传入多个id用，隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public R delete(String ids) {
        List<String> deleteIds = new ArrayList<>(Arrays.asList(ids.split(",")));
        userMapper.deleteBatchIds(deleteIds);
        return R.success();
    }

    @ApiOperation(value = "微信登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", paramType = "String", value = "code", required = true),
            @ApiImplicitParam(name = "rawData", paramType = "String", value = "rawData", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/wxlogin")
    public R wxLogin(String code, String rawData, String encryptedData, String iv, String signature) {

        // 使用 appId + appSecret + code 登录凭证校验接口，获取session_key和openid
        JSONObject sessionKeyOpenId = WechatUtil.getSessionKeyOrOpenId(code);
        // 获取返回的参数 session_key 和 openid
        String openid = sessionKeyOpenId.getString("openid");
        String sessionKey = sessionKeyOpenId.getString("session_key");

        // 校验签名是否一致
        String signature1 = DigestUtils.sha1Hex(rawData + sessionKey);
        if (!signature1.equals(signature)) {
            return R.error("签名校验失败");
        }

        // 解密用户手机号信息
        JSONObject phoneNumberData = JSON.parseObject(WechatUtil.decrypt(encryptedData, sessionKey, iv));
        // 校验 appid
        String appid1 = phoneNumberData.getJSONObject("watermark").getString("appid");
        if (!appid1.equals(appid)) {
            return R.error("appid校验失败");
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
                    .setCountryCode(phoneNumberData.getString("countryCode"));
            // 生成 token，并保存用户登录信息
            user.setToken(this.login(user));
            userMapper.insert(user);
        } else {
            logger.debug("用户已存在，更新信息");
            // 用户已存在，更新用户登录时间
            user.setLastVisitTime(LocalDateTime.now());
            // 重新设置会话token
            user.setToken(this.login(user));
            userMapper.updateById(user);
        }

        return R.data(user);
    }

    @ApiOperation(value = "获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "userId", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/getUserInfo")
    public R getUserInfo() {
        return R.data(SecurityUtil.getUserInfo());
    }

    public String login(User user) {
        UserDetails userDetails = new LoginUser(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                AuthorityUtils.createAuthorityList("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginUser loginUser = SecurityUtil.getLoginUser();
        return jwtUtil.createToken(loginUser);
    }

    /**
     * 判断用户是否存在
     *
     * @param userId 用户 id
     * @return boolean 存在：true 不存在：false
     */
    public boolean exists(String userId) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", userId);
        return userMapper.selectCount(userQueryWrapper) != 0;
    }

    /**
     * 判断用户是否存在
     *
     * @param userId 用户 id
     * @return boolean 存在：false 不存在：true
     */
    public boolean notExists(String userId) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", userId);
        return userMapper.selectCount(userQueryWrapper) == 0;
    }
}
