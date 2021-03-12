package com.dopoiv.clinic.exception;

import lombok.Data;

/**
 * @author doverwong
 * @date 2021/2/25 15:07
 */

@Data
public class TokenRuntimeException extends RuntimeException {

    private Integer code = 401;
    private String msg;

    public TokenRuntimeException(String msg) {
        this.msg = msg;
    }

    public TokenRuntimeException(Integer code, String msg) {
        this.msg = msg;
    }

}
