package com.dopoiv.clinic.sys.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import com.dopoiv.clinic.sys.mapper.ApplicationMapper;
import com.dopoiv.clinic.sys.entity.Application;

import org.springframework.web.bind.annotation.RestController;
import com.dopoiv.clinic.common.tools.BaseController;

/**
 * @author dov
 * @since 2021-03-07
 */
@RestController
@RequestMapping("/sys/application")
public class ApplicationController extends BaseController {
    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private UserController userController;

    @Autowired
    VisitOrderMapper visitOrderMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取Application信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public Result page(
            Integer pageNum,
            Integer pageSize) {
        Result result = new Result();
        Page<Application> page = new Page<Application>(pageNum, pageSize);
        Application parms = new Application();
        QueryWrapper<Application> wrapper = new QueryWrapper<Application>(parms);

        result.setData(applicationMapper.selectPage(page, wrapper));
        return result;
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Application信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public Result getAllItems() {
        Result result = new Result();
        Application parms = new Application();
        QueryWrapper<Application> wrapper = new QueryWrapper<Application>(parms);
        List<Application> applicationList = applicationMapper.selectList(wrapper);

        result.setData(applicationList);
        return result;
    }

    @ApiOperation(value = "保存修改Application信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Result save(@RequestBody Application entity) {
        Result result = new Result();
        if (entity.getId() == null) {
            applicationMapper.insert(entity);
        } else {
            applicationMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除Application，可以传入多个id用，隔开")
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
        applicationMapper.deleteBatchIds(deleteIds);
        return result;
    }

    @ApiOperation(value = "按id撤销Application，可以传入多个id用，隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/revoke")
    public Result revoke(String ids) {
        Result result = new Result();
        logger.debug("ids: {}", ids);
        UpdateWrapper<Application> applicationUpdateWrapper = new UpdateWrapper<>();
        for (String id : ids.split(",")) {
            applicationUpdateWrapper
                    .eq("id", id)
                    .set("status", -1);
            applicationMapper.update(null, applicationUpdateWrapper);
        }
        return result;
    }

    @ApiOperation(value = "申请预约、出诊")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/apply")
    public Result apply(@RequestBody Map<String, String> body) {
        Result result = new Result();
        logger.debug("body: {}", body);
        Application application = JSONObject.parseObject(JSONObject.toJSONString(body), Application.class);
        logger.debug("application: {}", application);
        if (!userController.exists(application.getUserId())) {
            result.fail("用户不存在");
            return result;
        }
        if ("visit".equals(application.getType())) {
            result = saveVisit(body, application);
        }

        if ("appointment".equals(application.getType())) {
            result = saveAppointment(application);
        }
        return result;
    }

    /**
     * 保存出诊请求
     *
     * @param body   请求体
     * @param entity Application对象
     * @return result
     */
    public Result saveVisit(Map<String, String> body, Application entity) {
        Result result = new Result();
        // 新的出诊请求
        JSONObject visit = JSONObject.parseObject(JSON.toJSONString(entity));
        String time = visit.getString("time");
        String day = body.get("day");
        LocalDateTime localDateTime = LocalDateTimeUtil.parse(time);
        LocalDateTime offset = LocalDateTimeUtil.offset(localDateTime, Long.parseLong(day), ChronoUnit.DAYS);
        entity.setTime(offset);
        entity.setCreateTime(LocalDateTime.now());
        applicationMapper.insert(entity);

        // 同时生成出诊订单
        VisitOrder visitOrder = new VisitOrder();
        visitOrder.setVisitId(entity.getId());
        visitOrder.setFee(body.get("fee"));
        visitOrder.setPaid(0);
        visitOrder.setCreateTime(entity.getCreateTime());
        visitOrder.setExpirationTime(LocalDateTimeUtil.offset(entity.getCreateTime(), 30, ChronoUnit.MINUTES));
        visitOrderMapper.insert(visitOrder);
        result.setData(visitOrder);
        return result;
    }

    /**
     * 保存预约请求
     *
     * @param entity Application对象
     * @return result
     */
    public Result saveAppointment(Application entity) {
        Result result = new Result();
        entity.setCreateTime(LocalDateTime.now());
        applicationMapper.insert(entity);
        return result;
    }

    @ApiOperation(value = "获取用户申请信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "用户id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/getApplicationList")
    public Result getApplicationList(String userId) {
        Result result = new Result();
        if (!userController.exists(userId)) {
            result.fail("用户不存在");
            return result;
        }
        QueryWrapper<Application> applicationQueryWrapper = new QueryWrapper<>();
        applicationQueryWrapper
                .eq("user_id", userId)
                .orderByDesc("create_time");
        List<Application> applicationList = applicationMapper.selectList(applicationQueryWrapper);
        for (Application application : applicationList) {
            if ("visit".equals(application.getType())) {
                QueryWrapper<VisitOrder> visitOrderQueryWrapper = new QueryWrapper<>();
                visitOrderQueryWrapper.eq("visit_id", application.getId());
                VisitOrder visitOrder = visitOrderMapper.selectOne(visitOrderQueryWrapper);
                application.setEtc(visitOrder);
            }
        }
        result.setData(applicationList);
        return result;
    }


}