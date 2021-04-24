package com.dopoiv.clinic.project.application.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.tools.BaseController;
import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.project.application.entity.Application;
import com.dopoiv.clinic.project.application.mapper.ApplicationMapper;
import com.dopoiv.clinic.project.order.entity.VisitOrder;
import com.dopoiv.clinic.project.order.mapper.VisitOrderMapper;
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
    UserController userController;

    @Autowired
    VisitOrderMapper visitOrderMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取Application信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public R page(
            Integer pageNum,
            Integer pageSize) {
        Page<Application> page = new Page<>(pageNum, pageSize);
        Application params = new Application();
        QueryWrapper<Application> wrapper = new QueryWrapper<>(params);

        return R.data(applicationMapper.selectPage(page, wrapper));
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
        if (!userController.exists(application.getUserId())) {
            return R.error("用户不存在");
        }
        if ("visit".equals(application.getType())) {
            return saveVisit(body, application);
        }

        if ("appointment".equals(application.getType())) {
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
        applicationMapper.insert(entity);

        // 同时生成出诊订单
        VisitOrder visitOrder = new VisitOrder();
        visitOrder.setVisitId(entity.getId());
        visitOrder.setFee(body.get("fee"));
        visitOrder.setPaid(0);
        visitOrder.setCreateTime(entity.getCreateTime());
        visitOrder.setExpirationTime(LocalDateTimeUtil.offset(entity.getCreateTime(), 30, ChronoUnit.MINUTES));
        visitOrderMapper.insert(visitOrder);
        return R.data(visitOrder);
    }

    /**
     * 保存预约请求
     *
     * @param entity Application对象
     * @return result
     */
    public R saveAppointment(Application entity) {
        entity.setCreateTime(LocalDateTime.now());
        applicationMapper.insert(entity);
        return R.success();
    }

    @ApiOperation(value = "获取用户申请信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "用户id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/getApplicationList")
    public R getApplicationList(String userId) {
        if (!userController.exists(userId)) {
            return R.error("用户不存在");
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
        return R.data(applicationList);
    }
}
