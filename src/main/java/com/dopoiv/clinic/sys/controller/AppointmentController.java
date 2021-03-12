package com.dopoiv.clinic.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dopoiv.clinic.common.constants.Result;
import com.dopoiv.clinic.sys.mapper.AppointmentMapper;
import com.dopoiv.clinic.sys.entity.Appointment;

import org.springframework.web.bind.annotation.RestController;
import com.dopoiv.clinic.common.tools.BaseController;

/**
 * @author dov
 * @since 2021-03-02
 */
@RestController
@RequestMapping("/sys/appointment")
public class AppointmentController extends BaseController {
    @Autowired
    private AppointmentMapper appointmentMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserController userController;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取Appointment信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public Result page(
            Integer pageNum,
            Integer pageSize) {
        Result result = new Result();
        Page<Appointment> page = new Page<Appointment>(pageNum, pageSize);
        Appointment parms = new Appointment();
        QueryWrapper<Appointment> wrapper = new QueryWrapper<Appointment>(parms);

        result.setData(appointmentMapper.selectPage(page, wrapper));
        return result;
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Appointment信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public Result getAllItems() {
        Result result = new Result();
        Appointment parms = new Appointment();
        QueryWrapper<Appointment> wrapper = new QueryWrapper<Appointment>(parms);
        List<Appointment> appointmentList = appointmentMapper.selectList(wrapper);

        result.setData(appointmentList);
        return result;
    }

    @ApiOperation(value = "保存修改Appointment信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Result save(@RequestBody Appointment entity) {
        Result result = new Result();

        if (!userController.exists(entity.getUserId())) {
            result.fail("用户不存在");
            return result;
        }

        if (entity.getId() == null) {
            entity.setCreateTime(LocalDateTime.now());
            appointmentMapper.insert(entity);
        } else {
            appointmentMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除Appointment，可以传入多个id用，隔开")
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
        appointmentMapper.deleteBatchIds(deleteIds);
        return result;
    }

}
