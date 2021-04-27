package com.dopoiv.clinic.project.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dopoiv.clinic.project.message.entity.Message;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangduofu
 * @since 2021-04-24
 */
public interface IMessageService extends IService<Message> {

    /**
     * 根据用户 id 获取最近的 num 条信息
     *
     * @param userId   用户 id
     * @param friendId 联系人 id
     * @param num      要获取的数量
     * @return List 消息列表
     */
    List<Message> getRecentMsg(String userId, String friendId, int num);

}
