package com.dopoiv.clinic.project.appointment.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.application.vo.UserApplicationVo;
import com.dopoiv.clinic.project.appointment.entity.Appointment;
import com.dopoiv.clinic.project.appointment.mapper.AppointmentMapper;
import com.dopoiv.clinic.project.appointment.service.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dov
 * @since 2021-04-24
 */
@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements IAppointmentService {

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Override
    public IPage<UserApplicationVo> pageForQuery(PageDomain pageDomain, UserApplicationVo params) {


        return null;
    }
}
