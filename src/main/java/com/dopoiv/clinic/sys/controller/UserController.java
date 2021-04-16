package com.dopoiv.clinic.sys.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.constants.Result;
import com.dopoiv.clinic.common.tools.BaseController;
import com.dopoiv.clinic.common.utils.JwtUtil;
import com.dopoiv.clinic.common.utils.SecurityUtil;
import com.dopoiv.clinic.common.utils.WechatUtil;
import com.dopoiv.clinic.security.LoginUser;
import com.dopoiv.clinic.sys.entity.User;
import com.dopoiv.clinic.sys.mapper.UserMapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dov
 * @since 2021-02-23
 */
@RestController
@RequestMapping("/sys/user")
public class UserController extends BaseController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Resource
    @Lazy
    private AuthenticationManager authenticationManager;

    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${wx.mini.appid}")
    private String appid;

    @ApiOperation(value = "微信登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", paramType = "String", value = "code", required = true),
            @ApiImplicitParam(name = "rawData", paramType = "String", value = "rawData", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/wxlogin")
    public Result wxLogin(String code, String rawData, String encryptedData, String iv, String signature) {
        Result result = new Result();

        // 使用 appId + appSecret + code 登录凭证校验接口，获取session_key和openid
        JSONObject sessionKeyOpenId = WechatUtil.getSessionKeyOrOpenId(code);
        // 获取返回的参数 session_key 和 openid
        String openid = sessionKeyOpenId.getString("openid");
        String sessionKey = sessionKeyOpenId.getString("session_key");
        logger.debug("openid ------> " + openid);
        logger.debug("sessionKey ------> " + sessionKey);

        // 校验签名是否一致
        String signature1 = DigestUtils.sha1Hex(rawData + sessionKey);
        if (!signature1.equals(signature)) {
            result.fail("签名校验失败");
            return result;
        }

        // 解密用户手机号信息
        JSONObject phoneNumberData = JSON.parseObject(WechatUtil.decrypt(encryptedData, sessionKey, iv));
        // 校验appid
        String appid1 = phoneNumberData.getJSONObject("watermark").getString("appid");
        if (!appid1.equals(appid)) {
            result.fail("appid校验失败");
            return result;
        }

        // 判断用户是否是新用户，是的话，将用户信息存到数据库；不是的话，更新最新登录时间
        User user = userMapper.selectById(openid);
        // 生成token，用于维护微信小程序用户与服务端的会话
        String token = jwtUtil.generateToken(openid, sessionKey);
        logger.info("token -----> " + token);
        if (user == null) {
            logger.debug("新用户");
            // 保存用户公开信息
            JSONObject rawDataJson = JSON.parseObject(rawData);
            // 信息保存为user对象
            user = JSONObject.parseObject(rawDataJson.toJSONString(), User.class);
            user.setId(openid);
            user.setOpenId(openid)
                    .setToken(token)
                    .setCreateTime(LocalDateTime.now())
                    .setLastVisitTime(LocalDateTime.now())
                    .setSessionKey(sessionKey)
                    .setPhoneNumber(phoneNumberData.getString("phoneNumber"))
                    .setPurePhoneNumber(phoneNumberData.getString("purePhoneNumber"))
                    .setCountryCode(phoneNumberData.getString("countryCode"));
            logger.debug("user -----> " + user);
            userMapper.insert(user);
        } else {
            logger.debug("用户已存在，更新信息");
            // 用户已存在，更新用户登录时间
            user.setLastVisitTime(LocalDateTime.now());
            // 重新设置会话token
            user.setToken(token);
            userMapper.updateById(user);
        }

        // 保存用户登录信息
        this.login(user);
//        User sysUser = SecurityUtil.getUserInfo();
        logger.debug("user -----> {}", SecurityUtil.getAuthentication().getPrincipal());

        // 把新的token返回给小程序
        JSONObject data = new JSONObject();
        data.put("token", token);
        data.put("phoneNumber", user.getPhoneNumber());
        data.put("id", user.getId());
        result.setData(user);
        return result;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取User信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public Result page(
            Integer pageNum,
            Integer pageSize) {
        Result result = new Result();
        Page<User> page = new Page<User>(pageNum, pageSize);
        User parms = new User();
        QueryWrapper<User> wrapper = new QueryWrapper<User>(parms);

        result.setData(userMapper.selectPage(page, wrapper));
        return result;
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部User信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public Result getAllItems() {
        Result result = new Result();
        User parms = new User();
        QueryWrapper<User> wrapper = new QueryWrapper<User>(parms);
        List<User> userList = userMapper.selectList(wrapper);

        result.setData(userList);
        return result;
    }

    @ApiOperation(value = "保存修改User信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Result save(@RequestBody User entity) {
        Result result = new Result();
        if (entity.getId() == null) {
            userMapper.insert(entity);
        } else {
            userMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除User，可以传入多个id用，隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public Result delete(String ids) {
        Result result = new Result();
        List<String> deleteIds = new ArrayList<String>();
        for (String id : ids.split(",")) {
            deleteIds.add(id);
        }
        userMapper.deleteBatchIds(deleteIds);
        return result;
    }

    @ApiOperation(value = "测试用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "message", paramType = "String", value = "message", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/hello")
    public Result<User> hello(String message) {
        Result<User> result = new Result<>();

        logger.debug(message);

        result.success("请求成功");
        return result;
    }

    @ApiOperation(value = "获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "userId", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/getUserInfo")
    public Result<User> getUserInfo(String userId) {
        Result<User> result = new Result<>();
        result.setData(userMapper.selectById(userId));
        return result;
    }

    /**
     * 判断用户是否存在
     * @param userId 用户id
     * @return boolean 存在：true 不存在：false
     */
    public boolean exists(String userId) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", userId);
        return userMapper.selectCount(userQueryWrapper) != 0;
    }

    /**
     * 判断用户是否存在
     * @param userId 用户id
     * @return boolean 存在：false 不存在：true
     */
    public boolean notExists(String userId) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", userId);
        return userMapper.selectCount(userQueryWrapper) == 0;
    }

    public void login(User user) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getId(), user.getId()));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
    }
}
