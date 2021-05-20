package com.dopoiv.clinic.project.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dopoiv.clinic.project.user.dto.NewVisitStatistics;
import com.dopoiv.clinic.project.user.entity.User;

import java.util.List;

/**
 * @author wangduofu
 * @since 2021-04-24
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 新用户统计
     *
     * @return {@link List<NewVisitStatistics>}
     */
    List<NewVisitStatistics> selectNewVisitStatistics();
}
