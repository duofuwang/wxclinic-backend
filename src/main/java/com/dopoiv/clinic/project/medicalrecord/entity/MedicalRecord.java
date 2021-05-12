package com.dopoiv.clinic.project.medicalrecord.entity;

import com.dopoiv.clinic.common.tools.BaseEntity;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * MedicalRecord 实体类
 *
 * @author wangduofu
 * @since 2021-05-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "MedicalRecord对象", description = "")
public class MedicalRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "医生id")
    private String doctorId;

    @ApiModelProperty(value = "主诉")
    private String chiefComplaint;

    @ApiModelProperty(value = "患者名字")
    private String patientName;

    @ApiModelProperty(value = "年龄")
    private Integer patientAge;

    @ApiModelProperty(value = "患者出生日期")
    private LocalDateTime patientDate;

    @ApiModelProperty(value = "患者住址")
    private String patientAddress;

    @ApiModelProperty(value = "患者病史")
    private String medicalHistory;

    @ApiModelProperty(value = "诊断")
    private String doctorDiagnosis;

    @ApiModelProperty(value = "治疗方法")
    private String treatmentMethod;

    @ApiModelProperty(value = "处方")
    private String prescription;

    @ApiModelProperty(value = "诊疗时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}