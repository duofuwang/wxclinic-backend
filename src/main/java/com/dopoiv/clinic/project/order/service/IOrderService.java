package com.dopoiv.clinic.project.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.order.entity.Order;
import org.springframework.stereotype.Service;

/**
 * @author doverwong
 * @date 2021/5/14 22:16
 */
@Service
public interface IOrderService extends IService<Order> {

    /**
     * 获取订单分页信息
     *
     * @param pageDomain 分页
     * @param params     查询参数
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return 分页数据
     */
    IPage<Order> getPageForQuery(PageDomain pageDomain, Order params, String startDate, String endDate);

    /**
     * 分页获取用户订单
     *
     * @param pageDomain 页面域
     * @return {@link IPage<Order>}
     */
    IPage<Order> getUserOrderPage(PageDomain pageDomain);
}
