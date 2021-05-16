package com.dopoiv.clinic.project.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.tools.BaseController;
import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.project.order.entity.Order;
import com.dopoiv.clinic.project.order.mapper.OrderMapper;
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
 * <p>
 * 控制器类
 * </p>
 *
 * @author wangduofu
 * @since 2021-04-24
 */
@RestController
@RequestMapping("/order/visit-order")
public class OrderController extends BaseController {
    @Autowired
    private OrderMapper orderMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取VisitOrder信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public R page(
            Integer pageNum,
            Integer pageSize) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        Order params = new Order();
        QueryWrapper<Order> wrapper = new QueryWrapper<>(params);

        return R.data(orderMapper.selectPage(page, wrapper));
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部VisitOrder信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public R getAllItems() {
        Order params = new Order();
        QueryWrapper<Order> wrapper = new QueryWrapper<>(params);
        List<Order> orderList = orderMapper.selectList(wrapper);

        return R.data(orderList);
    }

    @ApiOperation(value = "保存修改VisitOrder信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public R save(@RequestBody Order entity) {
        if (entity.getId() == null) {
            orderMapper.insert(entity);
        } else {
            orderMapper.updateById(entity);
        }
        return R.success();
    }

    @ApiOperation(value = "按id删除VisitOrder，可以传入多个id用，隔开")
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

        UpdateWrapper<Order> orderUpdateWrapper = new UpdateWrapper<>();
        orderUpdateWrapper.eq("id", id).set("paid", 1);
        orderMapper.update(null, orderUpdateWrapper);

        return R.success();
    }
}
