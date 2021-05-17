package com.dopoiv.clinic.project.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.finance.entity.Finance;

/**
 * @author doverwong
 * @date 2021/5/17 15:54
 */
public interface IFinanceService extends IService<Finance> {

    IPage<Finance> getPageForQuery(PageDomain pageDomain, String startDate, String endDate, String sortBy);
}
