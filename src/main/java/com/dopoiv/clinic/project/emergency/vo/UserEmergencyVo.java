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

    private String userId;

    private String nickname;

    private String realName;

    private String phoneNumber;

    private String message;

    private String location;

    private String latitude;

    private String longitude;

    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private Integer status;
}
