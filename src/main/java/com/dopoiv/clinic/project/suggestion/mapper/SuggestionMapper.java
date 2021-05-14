package com.dopoiv.clinic.project.suggestion.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.project.suggestion.entity.Suggestion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author wangduofu
 * @since 2021-05-14
 */
public interface SuggestionMapper extends BaseMapper<Suggestion> {

    /**
     * 分页查询 Suggestion
     *
     * @param page      分页
     * @param params    查询参数
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 分页数据
     */
    IPage<Suggestion> selectPageForQuery(Page<Suggestion> page, Suggestion params, String startDate, String endDate);
}
