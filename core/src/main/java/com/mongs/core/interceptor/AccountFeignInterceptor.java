package com.mongs.core.interceptor;

import com.mongs.core.security.principal.PassportDetail;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public final class AccountFeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PassportDetail passportDetail = (PassportDetail) principal;
        template.header("passport", passportDetail.getPassportJson());
    }
}