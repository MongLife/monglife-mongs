package com.monglife.mongs.domain.mong.annotation;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MongAccountCheck {

    String accountId() default "accountId";

    String mongId() default "mongId";
}
