package com.dopoiv.clinic.project.evaluate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.project.evaluate.entity.Evaluate;

/**
 * @author wangduofu
 * @since 2021-05-14
 */
public interface EvaluateMapper extends BaseMapper<Evaluate> {

    /**
     * 分页查询 Evaluate
     *
     * @param page      分页
     * @param params    查询参数
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 分页数据
     */
    IPage<Evaluate> selectPageForQuery(Page<Evaluate> page, Evaluate params, String startDate, String endDate);

}
