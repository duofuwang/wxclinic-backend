package com.dopoiv.clinic.project.messageboard.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.project.messageboard.entity.MessageBoard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
*
* @author wangduofu
* @since 2021-05-14
*/
public interface MessageBoardMapper extends BaseMapper<MessageBoard> {

    IPage<MessageBoard> selectPageForQuery(Page<MessageBoard> page, MessageBoard params, String startDate, String endDate);
}
