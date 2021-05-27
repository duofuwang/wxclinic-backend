package com.dopoiv.clinic.project.user.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.user.entity.User;
import com.dopoiv.clinic.project.user.mapper.UserMapper;
import com.dopoiv.clinic.project.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangduofu
 * @since 2021-04-24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public IPage<User> getPageForQuery(User user, PageDomain pageDomain) {
        return userMapper.selectPage(
                new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize()),
                Wrappers.<User>lambdaQuery()
                        .like(ObjectUtil.isNotEmpty(user.getNickname()), User::getNickname, user.getNickname())
                        .like(ObjectUtil.isNotEmpty(user.getRealName()), User::getRealName, user.getRealName())
                        .eq(ObjectUtil.isNotEmpty(user.getPhoneNumber()), User::getPhoneNumber, user.getPhoneNumber())
                        .eq(ObjectUtil.isNotEmpty(user.getStatus()), User::getStatus, user.getStatus())
        );
    }

    @Override
    public int getNewVisitNum() {

        // 差距一个月
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime offset = LocalDateTimeUtil.offset(now, -1, ChronoUnit.MONTHS);

        return userMapper.selectCount(
                Wrappers.<User>lambdaQuery()
                        .between(User::getCreateTime, offset, now)
        );
    }
}
