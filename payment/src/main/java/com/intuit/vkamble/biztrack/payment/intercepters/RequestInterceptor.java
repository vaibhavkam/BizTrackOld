package com.intuit.vkamble.biztrack.payment.intercepters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import utils.RequestLog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static Logger log = LoggerFactory.getLogger(RequestInterceptor.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
        RequestLog requestLog = new RequestLog();
        requestLog.setRequest(request.getMethod(),request.getRequestURI(),request.getContentType(),request.getHeader("content-length"));
        requestLog.setResponse(response.getStatus(),response.getContentType());
        log.info(objectMapper.writeValueAsString(requestLog));
    }
}
