package com.dopoiv.clinic.project.config.entity;

import com.dopoiv.clinic.common.tools.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Config 实体类
 *
 * @author wangduofu
 * @since 2021-05-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "Config对象", description = "")
public class Config extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "诊所名")
    private String name;

    @ApiModelProperty(value = "诊所电话")
    private String tel;

    @ApiModelProperty(value = "诊所地址")
    private String address;

    @ApiModelProperty(value = "申请费用")
    private String applicationFee;

    @ApiModelProperty(value = "启用")
    private Integer enabled;

    @ApiModelProperty(value = "逻辑删除")
    private Integer deleted;


}