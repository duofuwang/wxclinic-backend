package com.dopoiv.clinic.project.admin.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.constant.Constants;
import com.dopoiv.clinic.common.utils.JwtUtil;
import com.dopoiv.clinic.common.utils.SecurityUtil;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.admin.dto.LoginBody;
import com.dopoiv.clinic.project.admin.service.IAdminService;
import com.dopoiv.clinic.project.user.controller.UserController;
import com.dopoiv.clinic.project.user.entity.User;
import com.dopoiv.clinic.project.user.mapper.UserMapper;
import com.dopoiv.clinic.redis.RedisCache;
import com.dopoiv.clinic.security.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.project.admin.mapper.AdminMapper;
import com.dopoiv.clinic.project.admin.entity.Admin;

import com.dopoiv.clinic.common.tools.BaseController;

import javax.annotation.Resource;

/**
 * <p>
 * 控制器类
 * </p>
 *
 * @author wangduofu
 * @since 2021-05-07
 */
@RestController
@RequestMapping("/admin")
public class AdminController extends BaseController {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private IAdminService adminService;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "params", paramType = "query", value = "Admin", required = true),
    })
    @ApiOperation(value = "分页获取Admin信息")
    @GetMapping("/page")
    public R page(Admin params) {
        PageDomain pageDomain = startMybatisPlusPage();
        return R.data(adminService.pageForQuery(pageDomain, params));
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Admin信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public R getAllItems() {
        Admin params = new Admin();
        QueryWrapper<Admin> wrapper = new QueryWrapper<>(params);
        List<Admin> adminList = adminMapper.selectList(wrapper);

        return R.data(adminList);
    }

    @ApiOperation(value = "保存修改Admin信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public R save(@RequestBody Admin entity) {
        if (entity.getId() == null) {
            adminMapper.insert(entity);
        } else {
            adminMapper.updateById(entity);
        }
        return R.success();
    }

    @ApiOperation(value = "按id删除Admin，可以传入多个id用，隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public R delete(String ids) {
        List<String> deleteIds = new ArrayList<>(Arrays.asList(ids.split(",")));
        adminMapper.deleteBatchIds(deleteIds);
        return R.success();
    }

    @ApiOperation(value = "用户是否为管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "query", value = "用户 id", required = true)
    })
    @GetMapping("/isAdmin")
    public R isAdmin(String userId) {
        return R.data(adminService.count(Wrappers.<Admin>lambdaQuery().eq(Admin::getUserId, userId)) > 0);
    }
}
