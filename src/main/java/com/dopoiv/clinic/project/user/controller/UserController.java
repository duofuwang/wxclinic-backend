package com.dopoiv.clinic.project.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dopoiv.clinic.common.tools.BaseController;
import com.dopoiv.clinic.common.utils.JwtUtil;
import com.dopoiv.clinic.common.utils.SecurityUtil;
import com.dopoiv.clinic.common.utils.WechatUtil;
import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.user.entity.User;
import com.dopoiv.clinic.project.user.mapper.UserMapper;
import com.dopoiv.clinic.project.user.service.IUserService;
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
    private IUserService userService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true),
            @ApiImplicitParam(name = "User", paramType = "query", value = "查询参数")
    })
    @ApiOperation(value = "分页获取User信息")
    @GetMapping("/page")
    public R page(User user) {
        PageDomain pageDomain = startMybatisPlusPage();
        return R.data(userService.getPageForQuery(user, pageDomain));
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

    @ApiOperation(value = "获取当前用户信息")
    @GetMapping("/getUserInfo")
    public R getUserInfo() {
        User user = SecurityUtil.getUserInfo();
        return R.data(userService.getById(user.getId()));
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping("/{userId}")
    public R getUserById(@PathVariable("userId") String userId) {
        return R.data(userService.getById(userId));
    }

    @ApiOperation(value = "更改用户状态")
    @PutMapping("/changeStatus")
    public R changeStatus(@RequestBody User user) {
        return R.status(userService.update(
                Wrappers.<User>lambdaUpdate()
                        .eq(User::getId, user.getId())
                        .set(User::getStatus, user.getStatus()))
        );
    }

    @ApiOperation(value = "获取新用户统计数据")
    @GetMapping("/getNewVisitStatistics")
    public R getNewVisitStatistics() {
        return R.data(userMapper.selectNewVisitStatistics());
    }
}
