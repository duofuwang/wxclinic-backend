package com.dopoiv.clinic.sys.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.sys.entity.VisitOrder;
import com.dopoiv.clinic.sys.mapper.VisitOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dopoiv.clinic.common.constants.Result;
import com.dopoiv.clinic.sys.mapper.VisitMapper;
import com.dopoiv.clinic.sys.entity.Visit;

import org.springframework.web.bind.annotation.RestController;
import com.dopoiv.clinic.common.tools.BaseController;

/**
 * @author dov
 * @since 2021-03-02
 */
@RestController
@RequestMapping("/sys/visit")
public class VisitController extends BaseController {
    @Autowired
    private VisitMapper visitMapper;

    @Autowired
    UserController userController;

    @Autowired
    VisitOrderMapper visitOrderMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取Visit信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public Result page(
            Integer pageNum,
            Integer pageSize) {
        Result result = new Result();
        Page<Visit> page = new Page<Visit>(pageNum, pageSize);
        Visit parms = new Visit();
        QueryWrapper<Visit> wrapper = new QueryWrapper<Visit>(parms);

        result.setData(visitMapper.selectPage(page, wrapper));
        return result;
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Visit信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public Result getAllItems() {
        Result result = new Result();
        Visit parms = new Visit();
        QueryWrapper<Visit> wrapper = new QueryWrapper<Visit>(parms);
        List<Visit> visitList = visitMapper.selectList(wrapper);

        result.setData(visitList);
        return result;
    }

    @ApiOperation(value = "保存修改Visit信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Result save(@RequestBody Map<String, String> body) {
        Result result = new Result();
        Visit entity = JSONObject.parseObject(JSONObject.toJSONString(body), Visit.class);
        if (!userController.exists(entity.getUserId())) {
            result.fail("用户不存在");
            return result;
        }

        if (entity.getId() == null) {
            // 新的出诊请求
            JSONObject visit = JSONObject.parseObject(JSON.toJSONString(entity));
            String time = visit.getString("time");
            String day = body.get("day");
            LocalDateTime localDateTime = LocalDateTimeUtil.parse(time);
            LocalDateTime offset = LocalDateTimeUtil.offset(localDateTime, Long.parseLong(day), ChronoUnit.DAYS);
            entity.setTime(offset);
            entity.setCreateTime(LocalDateTime.now());
            visitMapper.insert(entity);

            // 同时生成出诊订单
            VisitOrder visitOrder = new VisitOrder();
            visitOrder.setVisitId(entity.getId());
            visitOrder.setFee(body.get("fee"));
            visitOrder.setPaid(0);
            visitOrder.setCreateTime(entity.getCreateTime());
            visitOrder.setExpirationTime(LocalDateTimeUtil.offset(entity.getCreateTime(), 30, ChronoUnit.MINUTES));
            visitOrderMapper.insert(visitOrder);
            result.setData(visitOrder);
        } else {
            visitMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除Visit，可以传入多个id用，隔开")
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
        visitMapper.deleteBatchIds(deleteIds);
        return result;
    }

}
