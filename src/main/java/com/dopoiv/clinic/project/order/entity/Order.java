package com.dopoiv.clinic.project.order.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @author dov
 * @since 2021-03-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "Order对象", description = "")
@TableName("tb_order")
public class Order extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "出诊id")
    private String applicationId;

    @ApiModelProperty(value = "费用")
    private String fee;

    @ApiModelProperty(value = "支付状态，1：已支付，0：未支付")
    private Integer paid;

    @ApiModelProperty(value = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expirationTime;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;

    @ApiModelProperty(value = "用户id")
    @TableField(exist = false)
    private String userId;

    @ApiModelProperty(value = "昵称")
    @TableField(exist = false)
    private String nickname;

    @ApiModelProperty(value = "姓名")
    @TableField(exist = false)
    private String realName;

    @ApiModelProperty(value = "是否过期")
    @TableField(exist = false)
    private Boolean expired;

}
