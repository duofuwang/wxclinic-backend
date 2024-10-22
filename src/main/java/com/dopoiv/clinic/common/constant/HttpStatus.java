package com.dopoiv.clinic.common.constant;

/**
 * http 状态码
 * @author dov
 * @date 2021/4/23 20:05
 */
public class HttpStatus {

    /**
     * 操作成功
     */
    public static final int SUCCESS = 200;

    /**
     * 对象创建成功
     */
    public static final int CREATED = 201;

    /**
     * 请求已经被接受
     */
    public static final int ACCEPTED = 202;

    /**
     * 操作已经执行成功，但是没有返回数据
     */
    public static final int NO_CONTENT = 204;

    /**
     * 资源已被移除
     */
    public static final int MOVED_PERM = 301;

    /**
     * 重定向
     */
    public static final int SEE_OTHER = 303;

    /**
     * 资源没有被修改
     */
    public static final int NOT_MODIFIED = 304;

    /**
     * 消息不能读取，参数错误
     */
    public static final int BAD_REQUEST = 400;

    /**
     * 未授权
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 访问受限，授权过期
     */
    public static final int FORBIDDEN = 403;

    /**
     * 资源，服务未找到
     */
    public static final int NOT_FOUND = 404;

    /**
     * 不允许的http方法
     */
    public static final int BAD_METHOD = 405;

    /**
     * 资源冲突，或者资源被锁
     */
    public static final int CONFLICT = 409;

    /**
     * 不支持的数据，媒体类型
     */
    public static final int UNSUPPORTED_TYPE = 415;

    /**
     * 系统内部错误
     */
    public static final int ERROR = 500;

    /**
     * 接口未实现
     */
    public static final int NOT_IMPLEMENTED = 501;

    /**
     * 业务异常
     */
    public static final int SERVICE_ERROR = 600;

    /**
     * 图片上传失败
     */
    public static final int FILE_UPLOAD_FAILURE = 601;
    /**
     * 用户登入输入密码
     */
    public static final int LOGIN_USER = 602;

    /**
     * 支付失败
     */
    public static final int PAY_FAILURE = 603;


    /**
     * 退款失败
     */
    public static final int REFUND_FAILURE = 604;

    /**
     * 退款失败
     */
    public static final int BALANCE_INSUFFICIENT = 605;
    /**
     * 二维码错误
     */
    public static final int QR_ERROR = 606;
    /**
     * 短信发送过多
     */
    public static final int BUSINESS_LIMIT_CONTROL = 607;
    /**
     * 短信发送失败
     */
    public static final int SMS_FAILURE = 608;

    /**
     * 库存不足
     */
    public static final int LOW_STOCKS = 609;
}
