package com.dopoiv.clinic.project.messageboard.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.messageboard.entity.MessageBoard;
import com.dopoiv.clinic.project.messageboard.mapper.MessageBoardMapper;
import com.dopoiv.clinic.project.messageboard.service.IMessageBoardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangduofu
 * @since 2021-05-14
 */
@Service
public class MessageBoardServiceImpl extends ServiceImpl<MessageBoardMapper, MessageBoard> implements IMessageBoardService {

    @Autowired
    private MessageBoardMapper messageBoardMapper;

    @Override
    public IPage<MessageBoard> getPageForQuery(PageDomain pageDomain, MessageBoard params, String startDate, String endDate) {
        return messageBoardMapper.selectPageForQuery(
                new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize()),
                params,
                startDate,
                endDate
        );
    }
}
