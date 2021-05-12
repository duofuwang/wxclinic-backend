package com.dopoiv.clinic.project.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.article.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangduofu
 * @since 2021-05-11
 */
public interface IArticleService extends IService<Article> {

    /**
     * 分页查询文章列表
     *
     * @param pageDomain 分页
     * @param params 查询参数
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 文章分页数据
     */
    IPage<Article> getPageForQuery(PageDomain pageDomain, Article params, String startDate, String endDate);
}
