package com.dopoiv.clinic.project.emergency.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.tools.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dopoiv.clinic.common.constants.Result;
import com.dopoiv.clinic.project.emergency.mapper.EmergencyMapper;
import com.dopoiv.clinic.project.emergency.entity.Emergency;

import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author duofuwang
 * @since 2021-04-10
 */
@RestController
@RequestMapping("/basic/emergency")
public class EmergencyController extends BaseController {
    @Autowired
    private EmergencyMapper emergencyMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum",paramType = "query",value = "当前页码",required = true),
        @ApiImplicitParam(name = "pageSize",paramType = "query",value = "每页显示记录数",required = true)
    })
    @ApiOperation(value = "分页获取Emergency信息")
    @RequestMapping(method= RequestMethod.POST,value="/page")
    public Result page(
        Integer pageNum,
        Integer pageSize) {
        Result result = new Result();
        Page<Emergency> page = new Page<Emergency>(pageNum, pageSize);
        Emergency params = new Emergency();
        QueryWrapper<Emergency> wrapper = new QueryWrapper<Emergency>(params);

        result.setData(emergencyMapper.selectPage(page, wrapper));
        return result;
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Emergency信息")
    @RequestMapping(method= RequestMethod.POST,value="/getAllItems")
    public Result getAllItems() {
        Result result = new Result();
        Emergency params = new Emergency();
        QueryWrapper<Emergency> wrapper = new QueryWrapper<Emergency>(params);
        List<Emergency> emergencyList = emergencyMapper.selectList(wrapper);

        result.setData(emergencyList);
        return result;
    }

    @ApiOperation(value = "保存修改Emergency信息")
    @RequestMapping(method= RequestMethod.POST,value="/save")
    public Result save(@RequestBody Emergency entity) {
        Result result = new Result();
        if (entity.getId() == null) {
            emergencyMapper.insert(entity);
        } else {
            emergencyMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除Emergency，可以传入多个id用，隔开")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids",paramType = "query",value = "传入的id串，用，隔开",required = true)
    })
    @RequestMapping(method= RequestMethod.DELETE,value="/delete")
    public Result delete( String ids) {
        Result result = new Result();
        List<String> deleteIds = new ArrayList<String>();
        for (String id : ids.split(",")) {
            deleteIds.add(id);
        }
        emergencyMapper.deleteBatchIds(deleteIds);
        return result;
    }

}
