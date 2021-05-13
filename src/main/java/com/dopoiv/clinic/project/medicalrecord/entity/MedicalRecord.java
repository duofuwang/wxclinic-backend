package com.dopoiv.clinic.project.medicalrecord.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
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

    @ApiModelProperty(value = "医生姓名")
    @TableField(exist = false)
    private String doctorName;

    @ApiModelProperty(value = "主诉")
    private String chiefComplaint;

    @ApiModelProperty(value = "患者名字")
    private String patientName;

    @ApiModelProperty(value = "年龄")
    private String patientAge;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "逻辑删除 1删除 0不删除")
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}