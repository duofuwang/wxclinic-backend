package com.dopoiv.clinic.common.client;

import com.dtflys.forest.annotation.DataVariable;
import com.dtflys.forest.annotation.GetRequest;
import com.dtflys.forest.annotation.PostRequest;
import com.dtflys.forest.annotation.Query;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * @author doverwong
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
    String doPost(@DataVariable("requestUrl") String requestUrl, @Query Map<String, String> params);

}
