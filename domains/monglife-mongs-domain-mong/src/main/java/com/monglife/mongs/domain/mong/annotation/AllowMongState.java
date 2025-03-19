package com.monglife.mongs.domain.mong.annotation;

import com.monglife.mongs.domain.mong.enums.MongStateCode;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AllowMongState {

    MongStateCode[] stateCodes() default {};

    String mongIdFieldName() default "mongId";
}
