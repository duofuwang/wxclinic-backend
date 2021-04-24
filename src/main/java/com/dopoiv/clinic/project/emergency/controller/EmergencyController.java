package com.dopoiv.clinic.project.emergency.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.tools.BaseController;
import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.project.emergency.entity.Emergency;
import com.dopoiv.clinic.project.emergency.mapper.EmergencyMapper;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author duofuwang
 * @since 2021-04-10
 */
@RestController
@RequestMapping("/emergency")
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
    public R page(
        Integer pageNum,
        Integer pageSize) {
        Page<Emergency> page = new Page<>(pageNum, pageSize);
        Emergency params = new Emergency();
        QueryWrapper<Emergency> wrapper = new QueryWrapper<>(params);

        return R.data(emergencyMapper.selectPage(page, wrapper));
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Emergency信息")
    @RequestMapping(method= RequestMethod.POST,value="/getAllItems")
    public R getAllItems() {
        Emergency params = new Emergency();
        QueryWrapper<Emergency> wrapper = new QueryWrapper<>(params);
        List<Emergency> emergencyList = emergencyMapper.selectList(wrapper);

        return R.data(emergencyList);
    }

    @ApiOperation(value = "保存修改Emergency信息")
    @RequestMapping(method= RequestMethod.POST,value="/save")
    public R save(@RequestBody Emergency entity) {

        if (entity.getId() == null) {
            emergencyMapper.update(new Emergency(), new UpdateWrapper<Emergency>().eq("user_id", entity.getUserId()).set("is_calling", 0));
            entity.setIsCalling(1);
            emergencyMapper.insert(entity);
        } else {
            emergencyMapper.updateById(entity);
        }
        return R.data(entity);
    }

    @ApiOperation(value = "按id删除Emergency，可以传入多个id用，隔开")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids",paramType = "query",value = "传入的id串，用，隔开",required = true)
    })
    @RequestMapping(method= RequestMethod.DELETE,value="/delete")
    public R delete( String ids) {
        List<String> deleteIds = new ArrayList<String>(Arrays.asList(ids.split(",")));
        emergencyMapper.deleteBatchIds(deleteIds);
        return R.success();
    }

    @ApiOperation(value = "获取正在进行的呼救信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",paramType = "String",value = "用户id",required = true)
    })
    @RequestMapping(method= RequestMethod.POST,value="/getCurrentCall")
    public R<Emergency> getCurrentCall(String userId) {
        return R.data(emergencyMapper.selectOne(new QueryWrapper<Emergency>().eq("user_id", userId).eq("is_calling", 1)));
    }

    @ApiOperation(value = "停止正在进行的呼救信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",paramType = "String",value = "用户id",required = true)
    })
    @RequestMapping(method= RequestMethod.POST,value="/stopEmergencyCall")
    public R stopEmergencyCall(String userId) {
        Emergency emergency = new Emergency();
        emergency.setIsCalling(0);
        emergencyMapper.update(emergency, new UpdateWrapper<Emergency>().eq("user_id", userId).eq("is_calling", 1));
        return R.success();
    }

}
