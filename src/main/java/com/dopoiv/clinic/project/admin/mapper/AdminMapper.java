package com.dopoiv.clinic.project.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.project.admin.entity.Admin;

import java.util.List;

/**
*
* @author wangduofu
* @since 2021-05-07
*/
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 分页查询管理员列表
     * @param page 分页
     * @param params 查询参数
     * @return 分页列表
     */
    IPage<Admin> getAdminList(Page<Admin> page, Admin params);

    /**
     * 获取所有管理员列表
     *
     * @return 管理员列表
     */
    List<Admin> selectAll();

}
