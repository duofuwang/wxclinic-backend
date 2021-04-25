package com.dopoiv.clinic.common.client;

import com.dtflys.forest.annotation.*;

import java.util.Map;

/**
 * @author dov
 * @date 2020/12/27 19:54
 */
public interface MyHttpClient {

    /**
     * 常规 Get 方法
     *
     * @param requestUrl 请求的 url
     * @param params     请求附带的参数
     * @return 响应体
     */
    @GetRequest(
            url = "${requestUrl}"
    )
    @LogEnabled(false)
    String doGet(@DataVariable("requestUrl") String requestUrl, @Query Map<String, String> params);

    /**
     * 常规 Post 方法
     *
     * @param requestUrl 请求的 url
     * @param params     请求附带的参数
     * @return 响应体
     */
    @PostRequest(
            url = "${requestUrl}"
    )
    @LogEnabled(false)
    String doPost(@DataVariable("requestUrl") String requestUrl, @Query Map<String, String> params);

}
