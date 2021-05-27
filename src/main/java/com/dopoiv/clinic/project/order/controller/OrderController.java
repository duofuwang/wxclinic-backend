package com.dopoiv.clinic.project.order.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.tools.BaseController;
import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.application.entity.Application;
import com.dopoiv.clinic.project.application.service.IApplicationService;
import com.dopoiv.clinic.project.order.entity.Order;
import com.dopoiv.clinic.project.order.mapper.OrderMapper;
import com.dopoiv.clinic.project.order.service.IOrderService;
import com.dopoiv.clinic.project.user.entity.User;
import com.dopoiv.clinic.project.user.service.IUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
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
@RequestMapping("/order")
public class OrderController extends BaseController {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IApplicationService applicationService;

    @Autowired
    private IUserService userService;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取 Order 信息")
    @GetMapping("/page")
    public R page(Order params, String startDate, String endDate) {
        PageDomain pageDomain = startMybatisPlusPage();
        return R.data(orderService.getPageForQuery(pageDomain, params, startDate, endDate));
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部 Order 信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public R getAllItems() {
        Order params = new Order();
        QueryWrapper<Order> wrapper = new QueryWrapper<>(params);
        List<Order> orderList = orderMapper.selectList(wrapper);

        return R.data(orderList);
    }

    @ApiOperation(value = "保存修改 Order 信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public R save(@RequestBody Order entity) {
        if (entity.getId() == null) {
            orderMapper.insert(entity);
        } else {
            orderMapper.updateById(entity);
        }
        return R.success();
    }

    @ApiOperation(value = "按id删除 Order ，可以传入多个id用，隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public R delete(String ids) {
        List<String> deleteIds = new ArrayList<>(Arrays.asList(ids.split(",")));
        orderMapper.deleteBatchIds(deleteIds);
        return R.success();
    }

    @ApiOperation(value = "支付操作，根据id将出诊订单修改为已支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "String", value = "传入的id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/payment")
    public R payment(String id) {
        return R.status(orderService.update(
                Wrappers.<Order>lambdaUpdate()
                        .eq(Order::getId, id)
                        .set(Order::getPaid, 1)
        ));
    }

    @ApiOperation(value = "按id获取订单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "String", value = "传入的id", required = true)
    })
    @GetMapping("/{id}")
    public R getById(@PathVariable String id) {
        Order order = orderService.getById(id);
        Application application = applicationService.getById(order.getApplicationId());
        if (ObjectUtil.isNotNull(application)) {
            User user = userService.getById(application.getUserId());
            if (ObjectUtil.isNotNull(user)) {
                order.setNickname(user.getNickname())
                        .setRealName(user.getRealName())
                        .setUserId(user.getId());
            }
        }
        LocalDateTime expirationTime = order.getExpirationTime();
        Duration duration = LocalDateTimeUtil.between(LocalDateTime.now(), expirationTime);
        order.setExpired(duration.toMinutes() < 0);
        return R.data(order);
    }

    @ApiOperation(value = "获取用户订单信息")
    @GetMapping("/getUserOrderList")
    public R getUserOrderList() {
        PageDomain pageDomain = startMybatisPlusPage();
        return R.data(orderService.getUserOrderPage(pageDomain));
    }

    @ApiOperation(value = "取消订单")
    @PostMapping("/cancel")
    public R cancelOrder(String ids) {
        List<String> deleteIds = new ArrayList<>(Arrays.asList(ids.split(",")));
        deleteIds.forEach(id -> orderService.update(
                Wrappers.<Order>lambdaUpdate()
                        .eq(Order::getId, id)
                        .set(Order::getPaid, 2)
        ));
        return R.success();
    }
}
