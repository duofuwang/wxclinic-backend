package com.dopoiv.clinic.project.article.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.project.article.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
*
* @author wangduofu
* @since 2021-05-11
*/
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 分页查询文章列表
     *
     * @param page 分页
     * @param params 查询参数
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 文章分页数据
     */
    IPage<Article> selectPageForQuery(Page<Article> page, Article params, String startDate, String endDate);

}
