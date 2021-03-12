package com.dopoiv.clinic.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author doverwong
 * @date 2021/2/25 15:17
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor interceptor;

    /**
     * 重写添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加自定义拦截器，并拦截对应 url
        registry.addInterceptor(interceptor)
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/sys/user/wxlogin")
//                .addPathPatterns("/**");
                .addPathPatterns("/a");
    }
}
