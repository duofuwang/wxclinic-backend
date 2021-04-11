package com.dopoiv.clinic.project.emergency.entity;

import com.dopoiv.clinic.common.tools.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * @author duofuwang
 * @since 2021-04-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="Emergency对象", description="")
public class Emergency extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "定位地址")
    private String location;

    @ApiModelProperty(value = "纬度")
    private String latitude;

    @ApiModelProperty(value = "经度")
    private String longitude;

    @ApiModelProperty(value = "信息")
    private String message;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否呼救中")
    private Integer isCalling;
}
