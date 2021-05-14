package com.dopoiv.clinic.project.suggestion.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.suggestion.service.ISuggestionService;
import com.dopoiv.clinic.project.user.entity.User;
import com.dopoiv.clinic.project.user.service.IUserService;
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
import com.dopoiv.clinic.project.suggestion.mapper.SuggestionMapper;
import com.dopoiv.clinic.project.suggestion.entity.Suggestion;

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
@RequestMapping("/suggestion")
public class SuggestionController extends BaseController {
    @Autowired
    private SuggestionMapper suggestionMapper;

    @Autowired
    private ISuggestionService suggestionService;

    @Autowired
    private IUserService userService;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取Suggestion信息")
    @GetMapping("/page")
    public R page(Suggestion params, String startDate, String endDate) {
        PageDomain pageDomain = startMybatisPlusPage();
        return R.data(suggestionService.getPageForQuery(pageDomain, params, startDate, endDate));
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Suggestion信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public R getAllItems() {
        Suggestion params = new Suggestion();
        QueryWrapper<Suggestion> wrapper = new QueryWrapper<>(params);
        List<Suggestion> suggestionList = suggestionMapper.selectList(wrapper);

        return R.data(suggestionList);
    }

    @ApiOperation(value = "保存修改Suggestion信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public R save(@RequestBody Suggestion entity) {
        if (entity.getId() == null) {
            suggestionMapper.insert(entity);
        } else {
            suggestionMapper.updateById(entity);
        }
        return R.success();
    }

    @ApiOperation(value = "按id删除Suggestion，可以传入多个id用，隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public R delete(String ids) {
        List<String> deleteIds = new ArrayList<>(Arrays.asList(ids.split(",")));
        suggestionMapper.deleteBatchIds(deleteIds);
        return R.success();
    }

    @ApiOperation(value = "按id获取Suggestion")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "path", value = "id", required = true)
    })
    @GetMapping("/{id}")
    public R getById(@PathVariable String id) {
        Suggestion suggestion = suggestionService.getById(id);
        User user = userService.getById(suggestion.getUserId());
        suggestion.setNickname(user.getNickname()).setRealName(user.getRealName());
        return R.data(suggestion);
    }
}
