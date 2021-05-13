package com.dopoiv.clinic.project.medicalrecord.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.project.medicalrecord.entity.MedicalRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
*
* @author wangduofu
* @since 2021-05-12
*/
public interface MedicalRecordMapper extends BaseMapper<MedicalRecord> {

    /**
     * 查询病历分页数据
     * @param page 分页
     * @param params 查询参数
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 分页数据
     */
    IPage<MedicalRecord> selectPageForQuery(Page<MedicalRecord> page, MedicalRecord params, String startDate, String endDate);
}
