package com.dopoiv.clinic.project.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dopoiv.clinic.project.message.dto.ContactDTO;
import com.dopoiv.clinic.project.message.dto.MessageStatistics;
import com.dopoiv.clinic.project.message.entity.Message;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author dov
 * @since 2021-02-28
 */
public interface MessageMapper extends BaseMapper<Message> {

    /**
     * 根据用户 id 获取最近的 num 条信息
     *
     * @param userId   用户 id
     * @param friendId 联系人 id
     * @param num      要获取的数量
     * @return List 消息列表
     */
    List<Message> getRecentMsg(@Param("userId") String userId, @Param("friendId") String friendId, @Param("num") int num);

    /**
     * 获取用户未读消息
     *
     * @param userId 用户 id
     * @return List 未读消息列表
     */
    List<Message> getUnreadMessage(@Param("userId") String userId);

    /**
     * 获取最近会话列表和最后一条聊天记录
     *
     * @param userId 用户 id
     * @return List 会话列表
     */
    List<ContactDTO> getContactList(@Param("userId") String userId);

    /**
     * 一月内消息发送统计情况
     *
     * @return {@link List<MessageStatistics>}
     */
    List<MessageStatistics> selectMessageStatistics();

}
