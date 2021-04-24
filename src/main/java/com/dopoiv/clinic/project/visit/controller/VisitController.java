package com.dopoiv.clinic.project.visit.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.tools.BaseController;
import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.project.order.entity.VisitOrder;
import com.dopoiv.clinic.project.order.mapper.VisitOrderMapper;
import com.dopoiv.clinic.project.user.controller.UserController;
import com.dopoiv.clinic.project.visit.entity.Visit;
import com.dopoiv.clinic.project.visit.mapper.VisitMapper;
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
@RequestMapping("/visit")
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
    public R page(
            Integer pageNum,
            Integer pageSize) {
        Page<Visit> page = new Page<>(pageNum, pageSize);
        Visit params = new Visit();
        QueryWrapper<Visit> wrapper = new QueryWrapper<>(params);

        return R.data(visitMapper.selectPage(page, wrapper));
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Visit信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public R getAllItems() {
        Visit params = new Visit();
        QueryWrapper<Visit> wrapper = new QueryWrapper<>(params);
        List<Visit> visitList = visitMapper.selectList(wrapper);

        return R.data(visitList);
    }

    @ApiOperation(value = "保存修改Visit信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public R save(@RequestBody Map<String, String> body) {
        Visit entity = JSONObject.parseObject(JSONObject.toJSONString(body), Visit.class);
        if (!userController.exists(entity.getUserId())) {
            return R.error("用户不存在");
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
            return R.data(visitOrder);
        } else {
            visitMapper.updateById(entity);
        }
        return R.success();
    }

    @ApiOperation(value = "按id删除Visit，可以传入多个id用，隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public R delete(String ids) {
        List<String> deleteIds = new ArrayList<>(Arrays.asList(ids.split(",")));
        visitMapper.deleteBatchIds(deleteIds);
        return R.success();
    }
}
