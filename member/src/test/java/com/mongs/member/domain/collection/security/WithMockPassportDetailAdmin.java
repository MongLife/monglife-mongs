package com.mongs.member.domain.collection.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomAdminSecurityContextFactory.class)
public @interface WithMockPassportDetailAdmin {
    long id() default 1L;
    String email() default "-";
    String name() default "-";
    String role() default "ADMIN";
}