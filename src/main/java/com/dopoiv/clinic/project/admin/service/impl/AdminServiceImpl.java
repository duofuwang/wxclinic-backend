package com.dopoiv.clinic.project.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.admin.entity.Admin;
import com.dopoiv.clinic.project.admin.mapper.AdminMapper;
import com.dopoiv.clinic.project.admin.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangduofu
 * @since 2021-05-07
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public IPage<Admin> pageForQuery(PageDomain pageDomain, Admin params) {
        return adminMapper.getAdminList(new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize()), params);
    }
}
