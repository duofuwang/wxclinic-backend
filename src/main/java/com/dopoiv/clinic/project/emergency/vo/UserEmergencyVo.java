package com.dopoiv.clinic.project.emergency.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author dov
 */
@Data
public class UserEmergencyVo {

    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 电话号码
     */
    private String phoneNumber;

    /**
     * 消息
     */
    private String message;

    /**
     * 位置
     */
    private String location;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 状态
     */
    private Integer status;
}
