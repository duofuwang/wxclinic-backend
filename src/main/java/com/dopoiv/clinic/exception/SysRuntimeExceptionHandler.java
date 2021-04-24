package com.dopoiv.clinic.exception;

import com.dopoiv.clinic.common.web.domain.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author doverwong
 * @date 2021/2/25 15:05
 */

@RestControllerAdvice
public class SysRuntimeExceptionHandler {

    @ExceptionHandler(TokenRuntimeException.class)
    public R tokenRuntimeException(TokenRuntimeException e) {
        e.printStackTrace();
        R result = new R();
        result.setCode(e.getCode());
        result.setMsg(e.getMessage());
        return result;
    }

    @ExceptionHandler(Exception.class)
    public R handlerException(Exception e){
        e.printStackTrace();
        return R.error();
    }
}