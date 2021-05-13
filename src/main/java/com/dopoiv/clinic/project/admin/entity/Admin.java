package com.dopoiv.clinic.project.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.dopoiv.clinic.common.tools.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Admin 实体类
 *
 * @author wangduofu
 * @since 2021-05-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "Admin对象", description = "")
public class Admin extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "账号等级")
    private Integer level;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户头像")
    private String avatarUrl;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户真实姓名")
    private String realName;

    @TableField(exist = false)
    @ApiModelProperty(value = "性别")
    private Integer gender;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户绑定的手机号")
    private String phoneNumber;

    @TableField(exist = false)
    @ApiModelProperty(value = "上次登录时间")
    private String lastVisitTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "账号状态")
    private Integer status;
}