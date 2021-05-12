package com.dopoiv.clinic.project.article.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.article.entity.Article;
import com.dopoiv.clinic.project.article.mapper.ArticleMapper;
import com.dopoiv.clinic.project.article.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangduofu
 * @since 2021-05-11
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public IPage<Article> getPageForQuery(PageDomain pageDomain, Article params, String startDate, String endDate) {
        return articleMapper.selectPageForQuery(
                new Page<>(pageDomain.getPageNum(),pageDomain.getPageSize()),
                params,
                startDate,
                endDate
        );
    }
}
