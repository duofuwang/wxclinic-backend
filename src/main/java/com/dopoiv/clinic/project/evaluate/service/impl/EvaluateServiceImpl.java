package com.dopoiv.clinic.project.evaluate.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.evaluate.entity.Evaluate;
import com.dopoiv.clinic.project.evaluate.mapper.EvaluateMapper;
import com.dopoiv.clinic.project.evaluate.service.IEvaluateService;
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
public class EvaluateServiceImpl extends ServiceImpl<EvaluateMapper, Evaluate> implements IEvaluateService {

    @Autowired
    private EvaluateMapper evaluateMapper;

    @Override
    public IPage<Evaluate> getPageForQuery(PageDomain pageDomain, Evaluate params, String startDate, String endDate) {
        return evaluateMapper.selectPageForQuery(
                new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize()),
                params,
                startDate,
                endDate
        );
    }
}
