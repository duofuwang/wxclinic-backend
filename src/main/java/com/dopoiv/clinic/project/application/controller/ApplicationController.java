package com.dopoiv.clinic.project.application.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dopoiv.clinic.common.tools.BaseController;
import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.application.entity.Application;
import com.dopoiv.clinic.project.application.mapper.ApplicationMapper;
import com.dopoiv.clinic.project.application.service.IApplicationService;
import com.dopoiv.clinic.project.application.vo.ReviewApplicationVo;
import com.dopoiv.clinic.project.application.vo.UserApplicationVo;
import com.dopoiv.clinic.project.order.entity.Order;
import com.dopoiv.clinic.project.order.mapper.OrderMapper;
import com.dopoiv.clinic.project.user.mapper.UserMapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 控制器类
 * </p>
 *
 * @author wangduofu
 * @since 2021-04-24
 */
@RestController
@RequestMapping("/application")
public class ApplicationController extends BaseController {
    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private IApplicationService applicationService;

    @Autowired
    private UserMapper userMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiOperation(value = "分页获取Application信息")
    @GetMapping("/page")
    public R page(UserApplicationVo params, String startDate, String endDate) {
        PageDomain pageDomain = startMybatisPlusPage();
        return R.data(applicationService.pageForQuery(pageDomain, params, startDate, endDate));
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Application信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public R getAllItems() {
        Application params = new Application();
        QueryWrapper<Application> wrapper = new QueryWrapper<>(params);
        List<Application> applicationList = applicationMapper.selectList(wrapper);

        return R.data(applicationList);
    }

    @ApiOperation(value = "保存修改Application信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public R save(@RequestBody Application entity) {
        if (entity.getId() == null) {
            applicationMapper.insert(entity);
        } else {
            applicationMapper.updateById(entity);
        }
        return R.success();
    }

    @ApiOperation(value = "按id删除Application，可以传入多个id用，隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public R delete(String ids) {
        List<String> deleteIds = new ArrayList<>(Arrays.asList(ids.split(",")));
        applicationMapper.deleteBatchIds(deleteIds);
        return R.success();
    }

    @ApiOperation(value = "按id撤销Application，可以传入多个id用，隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/revoke")
    public R revoke(String ids) {
        logger.debug("ids: {}", ids);
        UpdateWrapper<Application> applicationUpdateWrapper = new UpdateWrapper<>();
        for (String id : ids.split(",")) {
            applicationUpdateWrapper
                    .eq("id", id)
                    .set("status", -1);
            applicationMapper.update(null, applicationUpdateWrapper);
        }
        return R.success();
    }

    @ApiOperation(value = "申请预约、出诊")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/apply")
    public R apply(@RequestBody Map<String, String> body) {
        logger.debug("body: {}", body);
        Application application = JSONObject.parseObject(JSONObject.toJSONString(body), Application.class);
        logger.debug("application: {}", application);
        if (2 == application.getType()) {
            return saveVisit(body, application);
        }

        if (1 == application.getType()) {
            return saveAppointment(application);
        }
        return R.success();
    }

    /**
     * 保存出诊请求
     *
     * @param body   请求体
     * @param entity Application对象
     * @return result
     */
    public R saveVisit(Map<String, String> body, Application entity) {
        // 新的出诊请求
        JSONObject visit = JSONObject.parseObject(JSON.toJSONString(entity));
        String time = visit.getString("time");
        String day = body.get("day");
        LocalDateTime localDateTime = LocalDateTimeUtil.parse(time);
        LocalDateTime offset = LocalDateTimeUtil.offset(localDateTime, Long.parseLong(day), ChronoUnit.DAYS);
        entity.setTime(offset);
        entity.setCreateTime(LocalDateTime.now());
        entity.setStatus(0);
        applicationMapper.insert(entity);

        // 同时生成出诊订单
        Order order = new Order();
        order.setApplicationId(entity.getId());
        order.setFee(body.get("fee"));
        order.setPaid(0);
        order.setCreateTime(entity.getCreateTime());
        order.setExpirationTime(LocalDateTimeUtil.offset(entity.getCreateTime(), 30, ChronoUnit.MINUTES));
        orderMapper.insert(order);
        return R.data(order);
    }

    /**
     * 保存预约请求
     *
     * @param entity Application对象
     * @return result
     */
    public R saveAppointment(Application entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setStatus(0);
        applicationMapper.insert(entity);
        return R.success();
    }

    @ApiOperation(value = "获取用户申请信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "用户id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/getApplicationList")
    public R getApplicationList(String userId) {
        List<Application> applicationList = applicationMapper.selectList(
                Wrappers.<Application>lambdaQuery()
                        .eq(Application::getUserId, userId)
                        .orderByDesc(Application::getCreateTime)
        );
        for (Application application : applicationList) {
            if (2 == application.getType()) {
                Order order = orderMapper.selectOne(
                        Wrappers.<Order>lambdaQuery()
                                .eq(Order::getApplicationId, application.getId())
                );
                application.setEtc(order);
            }
        }
        return R.data(applicationList);
    }

    @ApiOperation(value = "批准申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applicationId", paramType = "body", value = "申请id", required = true),
            @ApiImplicitParam(name = "remark", paramType = "body", value = "备注")
    })
    @PutMapping("/approve")
    public R approve(@RequestBody ReviewApplicationVo reviewApplication) {
        return R.status(applicationService.update(Wrappers.<Application>lambdaUpdate()
                .eq(Application::getId, reviewApplication.getApplicationId())
                .set(Application::getStatus, 1)
                .set(StrUtil.isNotEmpty(reviewApplication.getRemark()),
                        Application::getRemark,
                        reviewApplication.getRemark())
                .set(StrUtil.isEmpty(reviewApplication.getRemark()),
                        Application::getRemark,
                        "无")
        ));
    }

    @ApiOperation(value = "拒绝申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applicationId", paramType = "String", value = "申请id", required = true),
            @ApiImplicitParam(name = "remark", paramType = "body", value = "备注")
    })
    @PutMapping("/reject")
    public R reject(@RequestBody ReviewApplicationVo reviewApplication) {
        return R.status(applicationService.update(Wrappers.<Application>lambdaUpdate()
                .eq(Application::getId, reviewApplication.getApplicationId())
                .set(Application::getStatus, 2)
                .set(StrUtil.isNotEmpty(reviewApplication.getRemark()),
                        Application::getRemark,
                        reviewApplication.getRemark())
                .set(StrUtil.isEmpty(reviewApplication.getRemark()),
                        Application::getRemark,
                        "无")
        ));
    }

    @ApiOperation(value = "根据id获取申请信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applicationId", paramType = "String", value = "申请id", required = true)
    })
    @GetMapping("/{applicationId}")
    public R getById(@PathVariable String applicationId) {
        return R.data(applicationService.getUserApplication(applicationId));
    }
}
