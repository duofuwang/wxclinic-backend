package com.dopoiv.clinic.project.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.project.finance.entity.Finance;

/**
 * @author doverwong
 * @date 2021/5/17 15:35
 */
public interface FinanceMapper extends BaseMapper<Finance> {

    /**
     * 分页查询财务统计数据 按天
     *
     * @param page      页面
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return {@link IPage<Finance>}
     */
    IPage<Finance> selectPageByDay(Page<Finance> page, String startDate, String endDate);

    /**
     * 分页查询财务统计数据 按周
     *
     * @param page      页面
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return {@link IPage<Finance>}
     */
    IPage<Finance> selectPageByWeek(Page<Finance> page, String startDate, String endDate);

    /**
     * 分页查询财务统计数据 按月
     *
     * @param page      页面
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return {@link IPage<Finance>}
     */
    IPage<Finance> selectPageByMonth(Page<Finance> page, String startDate, String endDate);

    /**
     * 分页查询财务统计数据 按年
     *
     * @param page      页面
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return {@link IPage<Finance>}
     */
    IPage<Finance> selectPageByYear(Page<Finance> page, String startDate, String endDate);
}
