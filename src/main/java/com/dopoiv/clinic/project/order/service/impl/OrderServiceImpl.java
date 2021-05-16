package com.dopoiv.clinic.project.order.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dopoiv.clinic.project.order.entity.Order;
import com.dopoiv.clinic.project.order.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangduofu
 * @since 2021-04-24
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IService<Order> {

}
