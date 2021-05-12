package com.dopoiv.clinic.project.articletype.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.project.articletype.service.IArticleTypeService;
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
import com.dopoiv.clinic.project.articletype.mapper.ArticleTypeMapper;
import com.dopoiv.clinic.project.articletype.entity.ArticleType;

import com.dopoiv.clinic.common.tools.BaseController;

/**
 * <p>
 * 控制器类
 * </p>
 *
 * @author wangduofu
 * @since 2021-05-12
 */
@RestController
@RequestMapping("/article-type")
public class ArticleTypeController extends BaseController {
    @Autowired
    private ArticleTypeMapper articleTypeMapper;

    @Autowired
    private IArticleTypeService articleTypeService;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取ArticleType信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public R page(
            Integer pageNum,
            Integer pageSize) {
        Page<ArticleType> page = new Page<>(pageNum, pageSize);
        ArticleType params = new ArticleType();
        QueryWrapper<ArticleType> wrapper = new QueryWrapper<>(params);

        return R.data(articleTypeMapper.selectPage(page, wrapper));
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部ArticleType信息")
    @GetMapping("/getAllItems")
    public R getAllItems() {
        return R.data(articleTypeService.getAllArticleType());
    }

    @ApiOperation(value = "按id获取ArticleType信息")
    @GetMapping("/{id}")
    public R getById(@PathVariable String id) {
        return R.data(articleTypeService.getById(id));
    }

    @ApiOperation(value = "保存修改ArticleType信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public R save(@RequestBody ArticleType entity) {
        if (entity.getId() == null) {
            articleTypeMapper.insert(entity);
        } else {
            articleTypeMapper.updateById(entity);
        }
        return R.success();
    }

    @ApiOperation(value = "按id删除ArticleType，可以传入多个id用，隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public R delete(String ids) {
        List<String> deleteIds = new ArrayList<>(Arrays.asList(ids.split(",")));
        articleTypeMapper.deleteBatchIds(deleteIds);
        return R.success();
    }
}
