package com.dopoiv.clinic.project.emergency.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.emergency.entity.Emergency;
import com.dopoiv.clinic.project.emergency.mapper.EmergencyMapper;
import com.dopoiv.clinic.project.emergency.service.IEmergencyService;
import com.dopoiv.clinic.project.emergency.vo.UserEmergencyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author duofuwang
 * @since 2021-04-24
 */
@Service
public class EmergencyServiceImpl extends ServiceImpl<EmergencyMapper, Emergency> implements IEmergencyService {

    @Autowired
    private EmergencyMapper emergencyMapper;

    @Override
    public IPage<UserEmergencyVo> pageForQuery(PageDomain pageDomain, UserEmergencyVo params, String startDate, String endDate) {
        return emergencyMapper.selectPageForQuery(
                new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize()),
                params,
                startDate,
                endDate
        );
    }
}
