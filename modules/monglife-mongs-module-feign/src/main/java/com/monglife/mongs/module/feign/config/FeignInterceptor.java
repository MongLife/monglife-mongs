package com.monglife.mongs.module.feign.config;

import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Slf4j
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        Request feignRequest = template.request();

        log.info("[FeignInterceptor] {} ===> {}", feignRequest.url(), feignRequest.body());

        if (requestAttributes != null) {
            HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

            if (request != null) {
                String passportJson = request.getHeader("passport");

                if (passportJson != null) {
                    template.header("passport", passportJson);
                }
            }
        }
    }
}