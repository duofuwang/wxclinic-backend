package com.dopoiv.clinic.project.appointment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dopoiv.clinic.project.appointment.entity.Appointment;
import com.dopoiv.clinic.project.appointment.mapper.AppointmentMapper;
import com.dopoiv.clinic.project.appointment.service.IAppointmentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dov
 * @since 2021-04-24
 */
@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements IAppointmentService {

}
