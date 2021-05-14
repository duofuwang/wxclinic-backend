package com.dopoiv.clinic.project.evaluate.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.evaluate.entity.Evaluate;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangduofu
 * @since 2021-05-14
 */
public interface IEvaluateService extends IService<Evaluate> {

    /**
     * 分页获取 Suggestion
     *
     * @param pageDomain 分页
     * @param params     查询参数
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return 分页数据
     */
    IPage<Evaluate> getPageForQuery(PageDomain pageDomain, Evaluate params, String startDate, String endDate);
}
