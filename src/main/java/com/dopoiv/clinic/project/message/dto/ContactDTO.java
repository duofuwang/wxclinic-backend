package com.dopoiv.clinic.project.message.dto;

import com.dopoiv.clinic.common.tools.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 联系人和最后一条聊天记录
 * @author doverwong
 * @date 2021/3/5 16:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="ContactList对象", description="")
public class ContactDTO extends BaseEntity {

    @ApiModelProperty(value = "联系人的id")
    private String friendId;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String avatarUrl;

    @ApiModelProperty(value = "消息类型")
    private String type;

    @ApiModelProperty(value = "未读消息数量")
    private String unreadNum;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
