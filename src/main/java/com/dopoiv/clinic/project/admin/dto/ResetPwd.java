package com.dopoiv.clinic.project.admin.dto;

import lombok.Data;

/**
 * @author doverwong
 * @date 2021/5/13 22:52
 */
@Data
public class ResetPwd {

    private String userId;

    private String oldPassword;

    private String newPassword;
}
