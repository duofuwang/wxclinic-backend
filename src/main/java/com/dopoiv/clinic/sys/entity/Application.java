package com.dopoiv.clinic.sys.entity;

import com.dopoiv.clinic.common.tools.BaseEntity;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author dov
 * @since 2021-03-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="Application对象", description="")
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

    @ApiModelProperty(value = "申请类型，预约：appointment；出诊：visit")
    private String type;

    @ApiModelProperty(value = "审核状态；0：拒绝，1：通过，-1：撤销")
    private Integer status;


}
