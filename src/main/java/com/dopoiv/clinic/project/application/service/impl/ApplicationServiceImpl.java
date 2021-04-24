package com.dopoiv.clinic.project.application.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dopoiv.clinic.project.application.entity.Application;
import com.dopoiv.clinic.project.application.mapper.ApplicationMapper;
import com.dopoiv.clinic.project.application.service.IApplicationService;
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
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application> implements IApplicationService {

}
