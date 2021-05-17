package com.dopoiv.clinic.project.finance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.finance.entity.Finance;
import com.dopoiv.clinic.project.finance.mapper.FinanceMapper;
import com.dopoiv.clinic.project.finance.service.IFinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author doverwong
 * @date 2021/5/17 15:54
 */
@Service
public class FinanceServiceImpl extends ServiceImpl<FinanceMapper, Finance> implements IFinanceService {

    @Autowired
    private FinanceMapper financeMapper;

    @Override
    public IPage<Finance> getPageForQuery(PageDomain pageDomain, String startDate, String endDate, String sortBy) {

        Page<Finance> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        if ("day".equals(sortBy)) {
            return financeMapper.selectPageByDay(page, startDate, endDate);
        }
        if ("week".equals(sortBy)) {
            return financeMapper.selectPageByWeek(page, startDate, endDate);
        }
        if ("month".equals(sortBy)) {
            return financeMapper.selectPageByMonth(page, startDate, endDate);
        }
        if ("year".equals(sortBy)) {
            return financeMapper.selectPageByYear(page, startDate, endDate);
        }

        return new Page<>();
    }
}
