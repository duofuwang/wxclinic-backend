package com.dopoiv.clinic.common.web.domain;


import com.dopoiv.clinic.common.constant.HttpStatus;

/**
 * <b>功能名：</b>通用异常模板类<br>
 * <b>说明：</b><br>
 * <b>著作权：</b> Copyright (C) 2019   CORPORATION<br>
 * <b>修改履历：
 *
 * @author 2020-05-08 hufeng
 */
public enum ResultCode implements IResultCode {
    SUCCESS(HttpStatus.SUCCESS, "操作成功"),
    FAILURE(HttpStatus.ERROR, "操作失败"),
    PAY_FAILURE(HttpStatus.PAY_FAILURE, "支付失败"),
    QR_ERROR(HttpStatus.QR_ERROR,"二维码失效，请重新生成二维码"),
    BALANCE_INSUFFICIENT(HttpStatus.BALANCE_INSUFFICIENT, "退款失败，商户余额不足"),
    FAILURE_SERVICE(HttpStatus.SERVICE_ERROR, "业务异常"),
    LONIN_FAILURE(HttpStatus.SERVICE_ERROR, "登入失败"),
    LONIN_SUCCESS(HttpStatus.SUCCESS, "登入成功"),
    PASSWORD_ERROR(HttpStatus.SERVICE_ERROR, "用户密码错误"),
    UN_AUTHORIZED(HttpStatus.UNAUTHORIZED, "请求未授权"),
    CLIENT_UN_AUTHORIZED(HttpStatus.FORBIDDEN, "访问受限，授权过期"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "404 没找到请求"),
    MSG_NOT_READABLE(HttpStatus.BAD_REQUEST, "消息不能读取"),
    METHOD_NOT_SUPPORTED(HttpStatus.BAD_METHOD, "不支持当前请求方法"),
    MEDIA_TYPE_NOT_SUPPORTED(HttpStatus.UNSUPPORTED_TYPE, "不支持当前媒体类型"),
    REQ_REJECT(HttpStatus.FORBIDDEN, "请求被拒绝"),
    INTERNAL_SERVER_ERROR(HttpStatus.ERROR, "服务器异常"),
    PARAM_MISS(HttpStatus.BAD_REQUEST, "缺少必要的请求参数"),
    PARAM_TYPE_ERROR(HttpStatus.BAD_REQUEST, "请求参数类型错误"),
    PARAM_BIND_ERROR(HttpStatus.BAD_REQUEST, "请求参数绑定错误"),
    PARAM_VALID_ERROR(HttpStatus.BAD_REQUEST, "参数校验失败"),
    USER_JCAPTCHA_ERROR(HttpStatus.SERVICE_ERROR, "验证码错误"),
    USER_PASSWORD_NOT_MATCH(HttpStatus.SERVICE_ERROR, "用户不存在/密码错误"),
    USER_JCAPTCHA_EXPIRE(HttpStatus.SERVICE_ERROR, "验证码失效"),
    FILE_TYEP_ERROR(HttpStatus.FILE_UPLOAD_FAILURE, "文件类型错误"),
    LOGIN_USER(HttpStatus.LOGIN_USER, "新用户输入密码"),
    FILE_SIZE(HttpStatus.FILE_UPLOAD_FAILURE, "文件大小超出限制"),
    FILE_FAILURE(HttpStatus.FILE_UPLOAD_FAILURE, "上传文件失败"),
    BUSINESS_LIMIT_CONTROL(HttpStatus.BUSINESS_LIMIT_CONTROL, "当前用户短信发送过多，请稍后再试"),
    SMS_FAILURE(HttpStatus.SMS_FAILURE, "发送短信失败");
    final int code;
    final String message;


    private ResultCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public int getCode() {
        return this.code;
    }


}
