package com.dopoiv.clinic.project.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dopoiv.clinic.project.message.entity.Message;
import com.dopoiv.clinic.project.message.mapper.MessageMapper;
import com.dopoiv.clinic.project.message.service.IMessageService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangduofu
 * @since 2021-04-24
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

}
