package com.dopoiv.clinic.project.evaluate.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dopoiv.clinic.common.tools.BaseController;
import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.evaluate.entity.Evaluate;
import com.dopoiv.clinic.project.evaluate.mapper.EvaluateMapper;
import com.dopoiv.clinic.project.evaluate.service.IEvaluateService;
import com.dopoiv.clinic.project.user.entity.User;
import com.dopoiv.clinic.project.user.service.IUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 控制器类
 * </p>
 *
 * @author wangduofu
 * @since 2021-05-14
 */
@RestController
@RequestMapping("/evaluate")
public class EvaluateController extends BaseController {
    @Autowired
    private EvaluateMapper evaluateMapper;

    @Autowired
    private IEvaluateService evaluateService;

    @Autowired
    private IUserService userService;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取Evaluate信息")
    @GetMapping("/page")
    public R page(Evaluate params, String startDate, String endDate) {
        PageDomain pageDomain = startMybatisPlusPage();
        return R.data(evaluateService.getPageForQuery(pageDomain, params, startDate, endDate));
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Evaluate信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public R getAllItems() {
        Evaluate params = new Evaluate();
        QueryWrapper<Evaluate> wrapper = new QueryWrapper<>(params);
        List<Evaluate> evaluateList = evaluateMapper.selectList(wrapper);

        return R.data(evaluateList);
    }

    @ApiOperation(value = "保存修改Evaluate信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public R save(@RequestBody Evaluate entity) {
        if (entity.getId() == null) {
            evaluateMapper.insert(entity);
        } else {
            evaluateMapper.updateById(entity);
        }
        return R.success();
    }

    @ApiOperation(value = "按id删除Evaluate，可以传入多个id用，隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public R delete(String ids) {
        List<String> deleteIds = new ArrayList<>(Arrays.asList(ids.split(",")));
        evaluateMapper.deleteBatchIds(deleteIds);
        return R.success();
    }

    @ApiOperation(value = "按id获取Evaluate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "path", value = "id", required = true)
    })
    @GetMapping("/{id}")
    public R getById(@PathVariable String id) {
        Evaluate evaluate = evaluateService.getById(id);
        User user = userService.getById(evaluate.getUserId());
        evaluate.setNickname(user.getNickname()).setRealName(user.getRealName());
        return R.data(evaluate);
    }
}
