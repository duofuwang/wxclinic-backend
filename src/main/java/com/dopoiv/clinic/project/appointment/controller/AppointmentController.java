package com.dopoiv.clinic.project.appointment.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.tools.BaseController;
import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.project.appointment.entity.Appointment;
import com.dopoiv.clinic.project.appointment.mapper.AppointmentMapper;
import com.dopoiv.clinic.project.user.controller.UserController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/appointment")
public class AppointmentController extends BaseController {
    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    UserController userController;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取Appointment信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public R page(
            Integer pageNum,
            Integer pageSize) {
        Page<Appointment> page = new Page<>(pageNum, pageSize);
        Appointment params = new Appointment();
        QueryWrapper<Appointment> wrapper = new QueryWrapper<>(params);

        return R.data(appointmentMapper.selectPage(page, wrapper));
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Appointment信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public R getAllItems() {
        Appointment params = new Appointment();
        QueryWrapper<Appointment> wrapper = new QueryWrapper<>(params);
        List<Appointment> appointmentList = appointmentMapper.selectList(wrapper);

        return R.data(appointmentList);
    }

    @ApiOperation(value = "保存修改Appointment信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public R save(@RequestBody Appointment entity) {

        if (!userController.exists(entity.getUserId())) {
            return R.error("用户不存在");
        }

        if (entity.getId() == null) {
            entity.setCreateTime(LocalDateTime.now());
            appointmentMapper.insert(entity);
        } else {
            appointmentMapper.updateById(entity);
        }
        return R.success();
    }

    @ApiOperation(value = "按id删除Appointment，可以传入多个id用，隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public R delete(String ids) {
        List<String> deleteIds = new ArrayList<>(Arrays.asList(ids.split(",")));
        appointmentMapper.deleteBatchIds(deleteIds);
        return R.success();
    }
}
