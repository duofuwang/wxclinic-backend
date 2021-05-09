package com.dopoiv.clinic.project.user.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.dopoiv.clinic.common.tools.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * User 实体类
 *
 * @author wangduofu
 * @since 2021-04-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "User对象", description = "")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "微信用户唯一标识 ")
    @JsonIgnore
    private String openId;

    @ApiModelProperty(value = "与openid、session_key进行关联的自定义登录态")
    private String token;

    @ApiModelProperty(value = "微信会话密钥")
    @JsonIgnore
    private String sessionKey;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String avatarUrl;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "上次登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastVisitTime;

    @ApiModelProperty(value = "用户性别，1是男性，2是女性，0是未知")
    private Integer gender;

    @ApiModelProperty(value = "用户所在省份")
    private String province;

    @ApiModelProperty(value = "用户所在城市")
    private String city;

    @ApiModelProperty(value = "用户所在区")
    private String district;

    @ApiModelProperty(value = "用户所在国家")
    private String country;

    @ApiModelProperty(value = "用户的语言，简体中文为 zh_CN")
    private String language;

    @ApiModelProperty(value = "用户绑定的手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "区号")
    private String countryCode;

    @ApiModelProperty(value = "没有区号的手机号")
    private String purePhoneNumber;

    @ApiModelProperty(value = "用户真实姓名")
    private String realName;

    @ApiModelProperty(value = "用户身份证号")
    private String identityNumber;

    @ApiModelProperty(value = "出生年月")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate birthday;

    @ApiModelProperty(value = "身高")
    private Double height;

    @ApiModelProperty(value = "体重")
    private Double weight;

    @ApiModelProperty(value = "状态 1启用 0禁用")
    private Integer status;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除 1删除 0不删除")
    private Integer deleted;
}