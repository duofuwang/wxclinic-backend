package com.dopoiv.clinic.project.suggestion.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.suggestion.entity.Suggestion;
import com.dopoiv.clinic.project.suggestion.mapper.SuggestionMapper;
import com.dopoiv.clinic.project.suggestion.service.ISuggestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangduofu
 * @since 2021-05-14
 */
@Service
public class SuggestionServiceImpl extends ServiceImpl<SuggestionMapper, Suggestion> implements ISuggestionService {

    @Autowired
    private SuggestionMapper suggestionMapper;

    @Override
    public IPage<Suggestion> getPageForQuery(PageDomain pageDomain, Suggestion params, String startDate, String endDate) {
        return suggestionMapper.selectPageForQuery(
                new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize()),
                params,
                startDate,
                endDate
        );
    }
}
