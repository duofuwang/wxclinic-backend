package com.dopoiv.clinic.project.article.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.article.service.IArticleService;
import com.dopoiv.clinic.project.user.entity.User;
import com.dopoiv.clinic.project.user.mapper.UserMapper;
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
import com.dopoiv.clinic.project.article.mapper.ArticleMapper;
import com.dopoiv.clinic.project.article.entity.Article;

import com.dopoiv.clinic.common.tools.BaseController;

/**
 * <p>
 * 控制器类
 * </p>
 *
 * @author wangduofu
 * @since 2021-05-11
 */
@RestController
@RequestMapping("/article")
public class ArticleController extends BaseController {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private IArticleService articleService;

    @Autowired
    private UserMapper userMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取Article信息")
    @GetMapping("/page")
    public R page(Article params, String startDate, String endDate) {
        PageDomain pageDomain = startMybatisPlusPage();
        return R.data(articleService.getPageForQuery(pageDomain, params, startDate, endDate));
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Article信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public R getAllItems() {
        Article params = new Article();
        QueryWrapper<Article> wrapper = new QueryWrapper<>(params);
        List<Article> articleList = articleMapper.selectList(wrapper);

        return R.data(articleList);
    }

    @ApiOperation(value = "保存修改Article信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public R save(@RequestBody Article entity) {
        if (entity.getId() == null) {
            articleMapper.insert(entity);
        } else {
            articleMapper.updateById(entity);
        }
        return R.success();
    }

    @ApiOperation(value = "按id删除Article，可以传入多个id用，隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public R delete(String ids) {
        List<String> deleteIds = new ArrayList<>(Arrays.asList(ids.split(",")));
        articleMapper.deleteBatchIds(deleteIds);
        return R.success();
    }

    @ApiOperation(value = "按id获取文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", paramType = "path", value = "文章id", required = true)
    })
    @GetMapping("/{articleId}")
    public R getById(@PathVariable String articleId) {
        Article article = articleService.getById(articleId);
        User user = userMapper.selectById(article.getUserId());
        article.setAuthor(user.getNickname());
        return R.data(article);
    }
}
