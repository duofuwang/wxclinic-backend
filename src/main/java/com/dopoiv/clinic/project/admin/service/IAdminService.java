package com.dopoiv.clinic.project.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.admin.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dopoiv.clinic.project.user.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangduofu
 * @since 2021-05-07
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 分页查询管理员列表
     * @param pageDomain 分页
     * @param params 查询参数
     * @return 分页数据
     */
    IPage<Admin> pageForQuery(PageDomain pageDomain, Admin params);

}
