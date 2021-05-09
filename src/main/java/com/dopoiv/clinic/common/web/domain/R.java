package com.dopoiv.clinic.common.web.domain;

import cn.hutool.core.lang.Dict;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.common.web.TableDataInfo;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author doverwong
 * @date 2021/4/23 20:15
 */
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;

    private T data;

    private boolean success;

    private String msg;

    private R(IResultCode resultCode) {
        this(resultCode, null, resultCode.getMessage());
    }

    private R(IResultCode resultCode, String msg) {
        this(resultCode, null, msg);
    }

    private R(IResultCode resultCode, T data) {
        this(resultCode, data, resultCode.getMessage());
    }

    private R(IResultCode resultCode, T data, String msg) {
        this(resultCode.getCode(), data, msg);
    }

    private R(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.success = ResultCode.SUCCESS.code == code;
    }

    public static boolean isSuccess(@Nullable R<?> result) {
        return (Boolean) Optional.ofNullable(result).map((x) -> {
            return nullSafeEquals(ResultCode.SUCCESS.code, x.code);
        }).orElse(Boolean.FALSE);
    }

    public static boolean isNotSuccess(@Nullable R<?> result) {
        return !isSuccess(result);
    }

    public static <T> R<T> data(T data) {
        return data(data, ResultCode.SUCCESS.message);
    }

    /**
     * 使用这种默认返回 Dict格式
     * @param tableDataInfo 数据源
     * @param pageDomain  分页信息
     * @return
     */
    public static R<Dict> data(TableDataInfo tableDataInfo, PageDomain pageDomain) {
        return data(Dict.create().set("total", tableDataInfo.getTotal()).set("pageNo", pageDomain.getPageNum()).set("pageSize", pageDomain.getPageSize()).set("records", tableDataInfo.getRows()), ResultCode.SUCCESS.message);
    }


    public static <T> R<T> data(T data, String msg) {
        return data(ResultCode.SUCCESS.code, data, msg);
    }

    public static <T> R<T> data(int code, T data, String msg) {
        return new R(code, data, data == null ? "暂无承载数据" : msg);
    }

    public static <T> R<T> success() {
        return new R(ResultCode.SUCCESS);
    }

    public static <T> R<T> success(String msg) {
        return new R(ResultCode.SUCCESS, msg);
    }

    public static <T> R<T> success(int code, String msg) {
        return new R(code, (Object) null, msg);
    }


    public static <T> R<T> success(IResultCode resultCode) {
        return new R(resultCode);
    }

    public static <T> R<T> success(IResultCode resultCode, String msg) {
        return new R(resultCode, msg);
    }

    public static <T> R<T> error(String msg) {
        return new R(ResultCode.FAILURE, msg);
    }

    public static <T> R<T> error() {
        return new R(ResultCode.FAILURE);
    }

    public static <T> R<T> error(int code, String msg) {
        return new R(code, (Object) null, msg);
    }

    public static <T> R<T> error(IResultCode resultCode) {
        return new R(resultCode);
    }

    public static <T> R<T> error(IResultCode resultCode, String msg) {
        return new R(resultCode, msg);
    }

    public static <T> R<T> status(boolean flag) {
        return flag ? success(ResultCode.SUCCESS) : error(ResultCode.FAILURE);
    }

    public int getCode() {
        return this.code;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public T getData() {
        return this.data;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "R(code=" + this.getCode() + ", success=" + this.isSuccess() + ", data=" + this.getData() + ", msg=" + this.getMsg() + ")";
    }

    public static boolean nullSafeEquals(@Nullable Object o1, @Nullable Object o2) {
        if (o1 == o2) {
            return true;
        } else if (o1 != null && o2 != null) {
            if (o1.equals(o2)) {
                return true;
            } else {
                return o1.getClass().isArray() && o2.getClass().isArray() ? arrayEquals(o1, o2) : false;
            }
        } else {
            return false;
        }
    }

    private static boolean arrayEquals(Object o1, Object o2) {
        if (o1 instanceof Object[] && o2 instanceof Object[]) {
            return Arrays.equals((Object[]) ((Object[]) o1), (Object[]) ((Object[]) o2));
        } else if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
            return Arrays.equals((boolean[]) ((boolean[]) o1), (boolean[]) ((boolean[]) o2));
        } else if (o1 instanceof byte[] && o2 instanceof byte[]) {
            return Arrays.equals((byte[]) ((byte[]) o1), (byte[]) ((byte[]) o2));
        } else if (o1 instanceof char[] && o2 instanceof char[]) {
            return Arrays.equals((char[]) ((char[]) o1), (char[]) ((char[]) o2));
        } else if (o1 instanceof double[] && o2 instanceof double[]) {
            return Arrays.equals((double[]) ((double[]) o1), (double[]) ((double[]) o2));
        } else if (o1 instanceof float[] && o2 instanceof float[]) {
            return Arrays.equals((float[]) ((float[]) o1), (float[]) ((float[]) o2));
        } else if (o1 instanceof int[] && o2 instanceof int[]) {
            return Arrays.equals((int[]) ((int[]) o1), (int[]) ((int[]) o2));
        } else if (o1 instanceof long[] && o2 instanceof long[]) {
            return Arrays.equals((long[]) ((long[]) o1), (long[]) ((long[]) o2));
        } else {
            return o1 instanceof short[] && o2 instanceof short[] ? Arrays.equals((short[]) ((short[]) o1), (short[]) ((short[]) o2)) : false;
        }
    }

    public R() {
    }
}
