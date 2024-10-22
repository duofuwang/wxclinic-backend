package com.dopoiv.clinic.project.application.entity;

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
 * Application 实体类
 *
 * @author wangduofu
 * @since 2021-04-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "Application对象", description = "")
public class Application extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "申请用户的id")
    private String userId;

    @ApiModelProperty(value = "问诊地址")
    private String address;

    @ApiModelProperty(value = "问诊时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime time;

    @ApiModelProperty(value = "病情描述")
    private String description;

    @ApiModelProperty(value = "图片url数组")
    private String image;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "申请类型，1预约 2出诊")
    private Integer type;


    @ApiModelProperty(value = "审核状态 0审核中 1通过 2拒绝 3撤销")
    private Integer status;

    @ApiModelProperty(value = "审核备注")
    private String remark;
}