package com.dopoiv.clinic.project.config.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.project.config.mapper.ConfigMapper;
import com.dopoiv.clinic.project.config.entity.Config;

import com.dopoiv.clinic.common.tools.BaseController;

/**
 * <p>
 * 控制器类
 * </p>
 *
 * @author wangduofu
 * @since 2021-05-14
 */
@RestController
@RequestMapping("/config")
public class ConfigController extends BaseController {
    @Autowired
    private ConfigMapper configMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取Config信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public R page(
            Integer pageNum,
            Integer pageSize) {
        Page<Config> page = new Page<>(pageNum, pageSize);
        Config params = new Config();
        QueryWrapper<Config> wrapper = new QueryWrapper<>(params);

        return R.data(configMapper.selectPage(page, wrapper));
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Config信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public R getAllItems() {
        Config params = new Config();
        QueryWrapper<Config> wrapper = new QueryWrapper<>(params);
        List<Config> configList = configMapper.selectList(wrapper);

        return R.data(configList);
    }

    @ApiOperation(value = "保存修改Config信息")
    @PutMapping("/save")
    public R save(@RequestBody Config entity) {
        if (entity.getId() == null) {
            configMapper.insert(entity);
        } else {
            configMapper.updateById(entity);
        }
        return R.success();
    }

    @ApiOperation(value = "按id删除Config，可以传入多个id用，隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public R delete(String ids) {
        List<String> deleteIds = new ArrayList<>(Arrays.asList(ids.split(",")));
        configMapper.deleteBatchIds(deleteIds);
        return R.success();
    }

    @ApiOperation(value = "获取诊所配置")
    @GetMapping
    public R getConfig() {
        return R.data(configMapper.selectOne(Wrappers.<Config>lambdaQuery().last("LIMIT 1")));
    }
}
