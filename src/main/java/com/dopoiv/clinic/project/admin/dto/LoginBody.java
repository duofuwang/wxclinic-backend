package com.dopoiv.clinic.project.admin.dto;

import lombok.Data;

/**
 * @author doverwong
 * @date 2021/5/7 15:48
 */
@Data
public class LoginBody {

    private String phoneNumber;

    private String password;
}
