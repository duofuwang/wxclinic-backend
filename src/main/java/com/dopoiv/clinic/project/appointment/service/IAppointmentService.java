package com.dopoiv.clinic.project.appointment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.application.vo.UserApplicationVo;
import com.dopoiv.clinic.project.appointment.entity.Appointment;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangduofu
 * @since 2021-04-24
 */
public interface IAppointmentService extends IService<Appointment> {

    IPage<UserApplicationVo> pageForQuery(PageDomain pageDomain, UserApplicationVo params);
}
