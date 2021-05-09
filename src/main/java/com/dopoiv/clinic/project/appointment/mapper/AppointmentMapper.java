package com.dopoiv.clinic.project.appointment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.project.application.vo.UserApplicationVo;
import com.dopoiv.clinic.project.appointment.entity.Appointment;

/**
 * @author dov
 * @since 2021-03-02
 */
public interface AppointmentMapper extends BaseMapper<Appointment> {

    IPage<UserApplicationVo> pageForQuery(Page page, UserApplicationVo params);

}
