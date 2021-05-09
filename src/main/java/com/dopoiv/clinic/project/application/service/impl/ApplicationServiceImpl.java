package com.dopoiv.clinic.project.application.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.application.entity.Application;
import com.dopoiv.clinic.project.application.mapper.ApplicationMapper;
import com.dopoiv.clinic.project.application.service.IApplicationService;
import com.dopoiv.clinic.project.application.vo.UserApplicationVo;
import com.dopoiv.clinic.project.user.entity.User;
import com.dopoiv.clinic.project.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangduofu
 * @since 2021-04-24
 */
@Service
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application> implements IApplicationService {

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public IPage<UserApplicationVo> pageForQuery(PageDomain pageDomain, UserApplicationVo params, String startDate, String endDate) {
        return applicationMapper.selectPageForQuery(new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize()), params, startDate, endDate);
    }

    @Override
    public UserApplicationVo getUserApplication(String applicationId) {
        UserApplicationVo userApplication = new UserApplicationVo();
        Application application = applicationMapper.selectById(applicationId);
        User user = userMapper.selectById(application.getUserId());
        BeanUtil.copyProperties(user, userApplication);
        BeanUtil.copyProperties(application, userApplication);
        return userApplication;
    }
}
