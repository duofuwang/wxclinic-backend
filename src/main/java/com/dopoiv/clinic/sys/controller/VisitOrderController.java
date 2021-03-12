package com.dopoiv.clinic.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import com.dopoiv.clinic.sys.mapper.VisitOrderMapper;
import com.dopoiv.clinic.sys.entity.VisitOrder;

import org.springframework.web.bind.annotation.RestController;
import com.dopoiv.clinic.common.tools.BaseController;

/**
 * @author dov
 * @since 2021-03-03
 */
@RestController
@RequestMapping("/sys/visit-order")
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
    public Result page(
            Integer pageNum,
            Integer pageSize) {
        Result result = new Result();
        Page<VisitOrder> page = new Page<VisitOrder>(pageNum, pageSize);
        VisitOrder parms = new VisitOrder();
        QueryWrapper<VisitOrder> wrapper = new QueryWrapper<VisitOrder>(parms);

        result.setData(visitOrderMapper.selectPage(page, wrapper));
        return result;
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部VisitOrder信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public Result getAllItems() {
        Result result = new Result();
        VisitOrder parms = new VisitOrder();
        QueryWrapper<VisitOrder> wrapper = new QueryWrapper<VisitOrder>(parms);
        List<VisitOrder> visitOrderList = visitOrderMapper.selectList(wrapper);

        result.setData(visitOrderList);
        return result;
    }

    @ApiOperation(value = "保存修改VisitOrder信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Result save(@RequestBody VisitOrder entity) {
        Result result = new Result();
        if (entity.getId() == null) {
            visitOrderMapper.insert(entity);
        } else {
            visitOrderMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除VisitOrder，可以传入多个id用，隔开")
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
        visitOrderMapper.deleteBatchIds(deleteIds);
        return result;
    }

    @ApiOperation(value = "支付操作，根据id将出诊订单修改为已支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "String", value = "传入的id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/payment")
    public Result payment(String id) {
        Result result = new Result();

        UpdateWrapper<VisitOrder> visitOrderUpdateWrapper = new UpdateWrapper<>();
        visitOrderUpdateWrapper.eq("id", id).set("paid", 1);
        visitOrderMapper.update(null, visitOrderUpdateWrapper);

        return result;
    }

}
