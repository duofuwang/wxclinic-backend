package com.dopoiv.clinic.exception;

import com.dopoiv.clinic.common.constants.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author doverwong
 * @date 2021/2/25 15:05
 */

@RestControllerAdvice
public class SysRuntimeExceptionHandler {

    @ExceptionHandler(TokenRuntimeException.class)
    public Result tokenRuntimeException(TokenRuntimeException e) {
        e.printStackTrace();
        Result result = new Result();
        result.setStatusCode(e.getCode().toString());
        result.setMessage(e.getMessage());
        return result;
    }

    @ExceptionHandler(Exception.class)
    public Result handlerException(Exception e){
        e.printStackTrace();
        Result result = new Result();
        result.addError();
        return result;
    }
}