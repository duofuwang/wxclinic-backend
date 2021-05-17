package com.dopoiv.clinic.project.medicalrecord.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.utils.SecurityUtil;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.medicalrecord.entity.MedicalRecord;
import com.dopoiv.clinic.project.medicalrecord.mapper.MedicalRecordMapper;
import com.dopoiv.clinic.project.medicalrecord.service.IMedicalRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dopoiv.clinic.project.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangduofu
 * @since 2021-05-12
 */
@Service
public class MedicalRecordServiceImpl extends ServiceImpl<MedicalRecordMapper, MedicalRecord> implements IMedicalRecordService {

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    @Override
    public IPage<MedicalRecord> getPageForQuery(PageDomain pageDomain, MedicalRecord params, String startDate, String endDate) {
        return medicalRecordMapper.selectPageForQuery(
                new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize()),
                params,
                startDate,
                endDate
        );
    }

    @Override
    public IPage<MedicalRecord> getUserMedicalRecordPage(PageDomain pageDomain) {
        User user = SecurityUtil.getUserInfo();
        IPage<MedicalRecord> medicalRecordPage = medicalRecordMapper.selectPage(
                new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize()),
                Wrappers.<MedicalRecord>lambdaQuery().eq(MedicalRecord::getUserId, user.getId())
        );
        return medicalRecordPage;
    }
}
