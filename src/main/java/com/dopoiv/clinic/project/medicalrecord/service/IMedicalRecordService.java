package com.dopoiv.clinic.project.medicalrecord.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.medicalrecord.entity.MedicalRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangduofu
 * @since 2021-05-12
 */
public interface IMedicalRecordService extends IService<MedicalRecord> {

    /**
     * 获取病历分页数据
     *
     * @param pageDomain 分页
     * @param params     查询参数
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return 分页数据
     */
    IPage<MedicalRecord> getPageForQuery(PageDomain pageDomain, MedicalRecord params, String startDate, String endDate);

    IPage<MedicalRecord> getUserMedicalRecordPage(PageDomain pageDomain);

}
