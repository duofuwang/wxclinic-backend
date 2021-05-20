package com.dopoiv.clinic.project.message.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 *
 * @author dov
 * @since 2021-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="Message对象", description="")
public class Message extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "发送者id")
    private String fromId;

    @ApiModelProperty(value = "接收者id")
    private String toId;

    @ApiModelProperty(value = "类型，文字、语音、位置、图片")
    private String type;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "签收状态，1：已读，0：未读")
    private Integer sign;

    @TableField(exist = false)
    private Integer mine;

    @TableField(exist = false)
    private String name;

}
