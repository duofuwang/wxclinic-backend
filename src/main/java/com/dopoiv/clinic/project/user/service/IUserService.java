package com.dopoiv.clinic.project.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.user.entity.User;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangduofu
 * @since 2021-04-24
 */
public interface IUserService extends IService<User> {

    /**
     * 分页查询用户列表
     *
     * @param user       查询参数
     * @param pageDomain 分页数据
     * @return 用户分页列表
     */
    IPage<User> getPageForQuery(User user, PageDomain pageDomain);

    /**
     * 获得新的访问
     *
     * @return int
     */
    int getNewVisit();

}
