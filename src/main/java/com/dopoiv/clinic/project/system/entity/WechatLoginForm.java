package com.dopoiv.clinic.project.system.entity;

import lombok.Data;

/**
 * 登录表单
 *
 * @author wangduofu
 * @date 2021/05/28
 */
@Data
public class WechatLoginForm {

    /**
     * 微信登录code
     */
    private String code;

    /**
     * 原始数据
     */
    private String rawData;

    /**
     * 加密的数据
     */
    private String encryptedData;

    /**
     * iv
     */
    private String iv;

    /**
     * 签名
     */
    private String signature;
}
