package com.dopoiv.clinic.project.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dopoiv.clinic.common.tools.BaseController;
import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.admin.dto.ResetPwd;
import com.dopoiv.clinic.project.admin.entity.Admin;
import com.dopoiv.clinic.project.admin.mapper.AdminMapper;
import com.dopoiv.clinic.project.admin.service.IAdminService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @ApiOperation(value = "获取全部Admin信息")
    @GetMapping("/getAllItems")
    public R getAllItems() {
        return R.data(adminMapper.selectAll());
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

    @ApiOperation(value = "用户是否为管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "body", value = "用户 id", required = true),
            @ApiImplicitParam(name = "oldPassword", paramType = "body", value = "旧密码", required = true),
            @ApiImplicitParam(name = "newPassword", paramType = "body", value = "新密码", required = true)
    })
    @PutMapping("/resetPwd")
    public R resetPwd(@RequestBody ResetPwd resetPwd) {
        Admin admin = adminService.getOne(Wrappers.<Admin>lambdaQuery().eq(Admin::getUserId, resetPwd.getUserId()));
        if (!admin.getPassword().equals(resetPwd.getOldPassword())) {
            return R.error("旧密码输入错误");
        }
        admin.setPassword(resetPwd.getNewPassword());
        return R.status(adminService.updateById(admin));
    }
}
