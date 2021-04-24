package com.dopoiv.clinic.common.web.domain;

import java.io.Serializable;

/**
 * @author doverwong
 * @date 2021/4/23 20:27
 */
public interface IResultCode extends Serializable {

    String getMessage();

    int getCode();
}
