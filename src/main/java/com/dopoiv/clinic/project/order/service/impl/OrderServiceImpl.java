package com.dopoiv.clinic.project.order.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dopoiv.clinic.common.utils.SecurityUtil;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.order.entity.Order;
import com.dopoiv.clinic.project.order.mapper.OrderMapper;
import com.dopoiv.clinic.project.order.service.IOrderService;
import com.dopoiv.clinic.project.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangduofu
 * @since 2021-04-24
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public IPage<Order> getPageForQuery(PageDomain pageDomain, Order params, String startDate, String endDate) {
        return orderMapper.selectPageForQuery(
                new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize()),
                params,
                startDate,
                endDate
        );
    }

    @Override
    public IPage<Order> getUserOrderPage(PageDomain pageDomain) {

        User user = SecurityUtil.getUserInfo();
        Order order = new Order();
        order.setUserId(user.getId());

        IPage<Order> orderList = orderMapper.selectPageForQuery(
                new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize()),
                order,
                null,
                null
        );

        orderList.getRecords().forEach(item -> {
            LocalDateTime expirationTime = item.getExpirationTime();
            Duration duration = LocalDateTimeUtil.between(LocalDateTime.now(), expirationTime);
            item.setExpired(duration.toMinutes() < 0);
        });

        return orderList;
    }
}
