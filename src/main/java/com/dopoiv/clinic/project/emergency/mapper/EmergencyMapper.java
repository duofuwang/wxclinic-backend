package com.dopoiv.clinic.project.emergency.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.project.emergency.dto.EmergencyStatistics;
import com.dopoiv.clinic.project.emergency.entity.Emergency;
import com.dopoiv.clinic.project.emergency.vo.UserEmergencyVo;

import java.util.List;

/**
 * @author duofuwang
 * @since 2021-04-10
 */
public interface EmergencyMapper extends BaseMapper<Emergency> {

    /**
     * 分页查询呼救列表
     *
     * @param page 分页
     * @param params 查询参数
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 分页数据
     */
    IPage<UserEmergencyVo> selectPageForQuery(Page<UserEmergencyVo> page, UserEmergencyVo params, String startDate, String endDate);

    /**
     * 呼救统计
     *
     * @return {@link List<EmergencyStatistics>}
     */
    List<EmergencyStatistics> selectEmergencyStatistics();
}
