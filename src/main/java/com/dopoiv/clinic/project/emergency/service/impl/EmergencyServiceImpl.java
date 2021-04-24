package com.dopoiv.clinic.project.emergency.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dopoiv.clinic.project.emergency.entity.Emergency;
import com.dopoiv.clinic.project.emergency.mapper.EmergencyMapper;
import com.dopoiv.clinic.project.emergency.service.IEmergencyService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author duofuwang
 * @since 2021-04-24
 */
@Service
public class EmergencyServiceImpl extends ServiceImpl<EmergencyMapper, Emergency> implements IEmergencyService {

}
