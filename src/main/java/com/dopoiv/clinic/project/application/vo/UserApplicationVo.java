package com.dopoiv.clinic.project.application.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author doverwong
 * @date 2021/5/9 20:05
 */
@Data
public class UserApplicationVo {

    private String id;

    private String userId;

    private String nickname;

    private String realName;

    private String phoneNumber;

    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime time;

    private String image;

    private String description;

    private Integer type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private Integer status;
}
