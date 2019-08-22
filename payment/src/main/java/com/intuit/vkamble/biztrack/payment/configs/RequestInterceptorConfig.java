package com.intuit.vkamble.biztrack.payment.configs;

import com.intuit.vkamble.biztrack.payment.intercepters.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RequestInterceptorConfig implements WebMvcConfigurer {

    private RequestInterceptor requestInterceptor;

    @Autowired
    public RequestInterceptorConfig(RequestInterceptor requestInterceptor){
        this.requestInterceptor = requestInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor);
    }
}
