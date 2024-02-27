package com.mongs.collection.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockPassportDetail {
    long id() default 1L;
    String email() default "test@test.com";
    String name() default "testName";
    String role() default "NORMAL";
}