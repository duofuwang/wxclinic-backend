package com.dopoiv.clinic.websocket;

import com.dopoiv.clinic.common.tools.BaseEntity;
import com.dopoiv.clinic.project.message.entity.Message;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author doverwong
 * @date 2021/2/28 12:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "message包装对象")
public class DataContent extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "动作类型")
    private Integer action;

    @ApiModelProperty(value = "消息实体")
    private Message message;

    @ApiModelProperty(value = "扩展字段")
    private String extend;
}
