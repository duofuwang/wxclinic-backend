package com.dopoiv.clinic.project.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dopoiv.clinic.project.message.dto.ContactDTO;
import com.dopoiv.clinic.project.message.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author dov
 * @since 2021-02-28
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

    /**
     * 根据用户 id 获取最近的 num 条信息
     * @param userId 用户 id
     * @param friendId 联系人 id
     * @param num 要获取的数量
     * @return List 消息列表
     */
    @Select("SELECT * FROM message\n" +
            "WHERE (from_id = #{userId} AND to_id = #{friendId})\n" +
            "OR (from_id = #{friendId} AND to_id = #{userId})\n" +
            "ORDER BY create_time")
    List<Message> getRecentMsg(@Param("userId") String userId, @Param("friendId") String friendId, @Param("num") int num);

    /**
     * 获取用户未读消息
     * @param userId 用户 id
     * @return List 未读消息列表
     */
    @Select("<script>" +
            "SELECT * from message\n" +
            "WHERE message.to_id = #{userId}\n" +
            "AND message.sign = 0" +
            "</script>")
    List<Message> getUnreadMessage(@Param("userId") String userId);

    /**
     * 获取最近会话列表和最后一条聊天记录
     * @param userId 用户 id
     * @return List 会话列表
     */
    @Select("SELECT count(sign = 0 or null) AS unread_num,friend_id,uni_table.content,uni_table.create_time,type,nick_name,avatar_url\n" +
            "FROM (\n" +
            "SELECT to_id as friend_id,content,1 as sign,create_time,type\n" +
            "FROM message WHERE (from_id = #{userId}) AND (to_id <> #{userId})\n" +
            "UNION\n" +
            "SELECT from_id as friend_id,content,sign,create_time,type\n" +
            "FROM message WHERE (from_id <> #{userId}) AND (to_id = #{userId})\n" +
            "ORDER BY create_time desc\n" +
            ") AS uni_table\n" +
            "INNER JOIN user u on friend_id = u.id GROUP BY friend_id ORDER BY create_time DESC")
    List<ContactDTO> getContactList(@Param("userId") String userId);

}
