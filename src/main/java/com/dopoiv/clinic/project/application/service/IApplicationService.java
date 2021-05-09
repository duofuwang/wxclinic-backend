package com.dopoiv.clinic.project.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.application.entity.Application;
import com.dopoiv.clinic.project.application.vo.UserApplicationVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangduofu
 * @since 2021-04-24
 */
public interface IApplicationService extends IService<Application> {

    /**
     * 分页获取申请列表
     *
     * @param pageDomain 分页
     * @param params     查询参数
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return 分页数据
     */
    IPage<UserApplicationVo> pageForQuery(PageDomain pageDomain, UserApplicationVo params, String startDate, String endDate);

    /**
     * 根据id获取申请信息
     *
     * @param applicationId 申请id
     * @return 申请信息
     */
    UserApplicationVo getUserApplication(String applicationId);
}
