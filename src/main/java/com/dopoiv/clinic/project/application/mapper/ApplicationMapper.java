package com.dopoiv.clinic.project.application.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.project.application.entity.Application;
import com.dopoiv.clinic.project.application.vo.UserApplicationVo;

/**
*
* @author wangduofu
* @since 2021-04-24
*/
public interface ApplicationMapper extends BaseMapper<Application> {

    /**
     * 分页查询申请列表
     *
     * @param page 分页
     * @param params 查询参数
     * @return 分页数据
     */
    IPage<UserApplicationVo> selectPageForQuery(Page<UserApplicationVo> page, UserApplicationVo params, String startDate, String endDate);
}
