package com.dopoiv.clinic.project.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.tools.BaseController;
import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.project.order.entity.VisitOrder;
import com.dopoiv.clinic.project.order.mapper.VisitOrderMapper;
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
public class VisitOrderController extends BaseController {
    @Autowired
    private VisitOrderMapper visitOrderMapper;

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
        Page<VisitOrder> page = new Page<>(pageNum, pageSize);
        VisitOrder params = new VisitOrder();
        QueryWrapper<VisitOrder> wrapper = new QueryWrapper<>(params);

        return R.data(visitOrderMapper.selectPage(page, wrapper));
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部VisitOrder信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public R getAllItems() {
        VisitOrder params = new VisitOrder();
        QueryWrapper<VisitOrder> wrapper = new QueryWrapper<>(params);
        List<VisitOrder> visitOrderList = visitOrderMapper.selectList(wrapper);

        return R.data(visitOrderList);
    }

    @ApiOperation(value = "保存修改VisitOrder信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public R save(@RequestBody VisitOrder entity) {
        if (entity.getId() == null) {
            visitOrderMapper.insert(entity);
        } else {
            visitOrderMapper.updateById(entity);
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
        visitOrderMapper.deleteBatchIds(deleteIds);
        return R.success();
    }

    @ApiOperation(value = "支付操作，根据id将出诊订单修改为已支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "String", value = "传入的id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/payment")
    public R payment(String id) {

        UpdateWrapper<VisitOrder> visitOrderUpdateWrapper = new UpdateWrapper<>();
        visitOrderUpdateWrapper.eq("id", id).set("paid", 1);
        visitOrderMapper.update(null, visitOrderUpdateWrapper);

        return R.success();
    }
}
