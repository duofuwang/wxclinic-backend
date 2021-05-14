package com.dopoiv.clinic.project.config.service.impl;

import com.dopoiv.clinic.project.config.entity.Config;
import com.dopoiv.clinic.project.config.mapper.ConfigMapper;
import com.dopoiv.clinic.project.config.service.IConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangduofu
 * @since 2021-05-14
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements IConfigService {

}
