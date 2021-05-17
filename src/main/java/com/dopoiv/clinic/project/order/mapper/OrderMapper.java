package com.dopoiv.clinic.project.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.project.order.entity.Order;

/**
 * @author dov
 * @since 2021-03-03
 */
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 分页查询订单
     *
     * @param page      分页
     * @param params    参数个数
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return {@link IPage<Order>}
     */
    IPage<Order> selectPageForQuery(Page<Order> page, Order params, String startDate, String endDate);
}
