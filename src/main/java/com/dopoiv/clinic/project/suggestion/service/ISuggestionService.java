package com.dopoiv.clinic.project.suggestion.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.suggestion.entity.Suggestion;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangduofu
 * @since 2021-05-14
 */
public interface ISuggestionService extends IService<Suggestion> {

    /**
     * 分页获取 Suggestion
     *
     * @param pageDomain 分页
     * @param params     查询参数
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return 分页数据
     */
    IPage<Suggestion> getPageForQuery(PageDomain pageDomain, Suggestion params, String startDate, String endDate);
}
