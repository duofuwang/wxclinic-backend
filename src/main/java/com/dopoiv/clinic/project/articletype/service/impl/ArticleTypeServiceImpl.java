package com.dopoiv.clinic.project.articletype.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dopoiv.clinic.project.articletype.entity.ArticleType;
import com.dopoiv.clinic.project.articletype.mapper.ArticleTypeMapper;
import com.dopoiv.clinic.project.articletype.service.IArticleTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangduofu
 * @since 2021-05-12
 */
@Service
public class ArticleTypeServiceImpl extends ServiceImpl<ArticleTypeMapper, ArticleType> implements IArticleTypeService {

    @Autowired
    private ArticleTypeMapper articleTypeMapper;

    @Override
    public List<ArticleType> getAllArticleType() {
        List<ArticleType> articleTypeList = articleTypeMapper.selectList(Wrappers.lambdaQuery());

        articleTypeList.forEach(parent -> {
            List<ArticleType> children = new ArrayList<>();
            articleTypeList.forEach(child -> {
                if (parent.getId().equals(child.getParentId())) {
                    children.add(child);
                }
            });
            parent.setChildren(children);
        });

        return articleTypeList.stream().filter(articleType -> "0".equals(articleType.getParentId())).collect(Collectors.toList());
    }
}
