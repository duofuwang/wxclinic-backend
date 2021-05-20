package com.dopoiv.clinic.project.emergency.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.emergency.entity.Emergency;
import com.dopoiv.clinic.project.emergency.vo.UserEmergencyVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author duofuwang
 * @since 2021-04-24
 */
public interface IEmergencyService extends IService<Emergency> {

    /**
     * 分页查询申请列表
     *
     * @param pageDomain 分页
     * @param params     查询参数
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return 分页数据
     */
    IPage<UserEmergencyVo> pageForQuery(PageDomain pageDomain, UserEmergencyVo params, String startDate, String endDate);

    /**
     * 获取呼救信息
     * @param emergencyId 呼救id
     * @return 呼救信息
     */
    UserEmergencyVo getUserEmergency(String emergencyId);

    /**
     * 获取用户紧急呼救列表
     *
     * @param pageDomain 页面域
     * @return {@link IPage<Emergency>}
     */
    IPage<UserEmergencyVo> getEmergencyList(PageDomain pageDomain);
}
