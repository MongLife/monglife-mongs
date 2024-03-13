package com.mongs.management.config;

import com.mongs.core.security.principal.PassportDetail;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public final class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PassportDetail passportDetail = (PassportDetail)principal;
        template.header("passport", passportDetail.getPassportJson());
        log.info("url: {}", template.request().url());
    }
}