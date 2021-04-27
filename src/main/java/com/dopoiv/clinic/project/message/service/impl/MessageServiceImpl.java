package com.dopoiv.clinic.project.message.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dopoiv.clinic.project.message.entity.Message;
import com.dopoiv.clinic.project.message.mapper.MessageMapper;
import com.dopoiv.clinic.project.message.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangduofu
 * @since 2021-04-24
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<Message> getRecentMsg(String userId, String friendId, int num) {
        return messageMapper.selectList(
                Wrappers.<Message>lambdaQuery()
                        .eq(Message::getFromId, userId)
                        .eq(Message::getToId, friendId)
                        .or()
                        .eq(Message::getFromId, friendId)
                        .eq(Message::getToId, userId)
                        .orderByAsc(Message::getCreateTime)
                        .last(ObjectUtil.isNotNull(num), "LIMIT" + num)
        );
    }
}
