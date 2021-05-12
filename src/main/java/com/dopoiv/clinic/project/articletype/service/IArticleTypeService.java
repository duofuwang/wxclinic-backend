package com.dopoiv.clinic.project.articletype.service;

import com.dopoiv.clinic.project.articletype.entity.ArticleType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangduofu
 * @since 2021-05-12
 */
public interface IArticleTypeService extends IService<ArticleType> {

    /**
     * 获取所有文章类型
     *
     * @return 文章类型列表
     */
    List<ArticleType> getAllArticleType();
}
