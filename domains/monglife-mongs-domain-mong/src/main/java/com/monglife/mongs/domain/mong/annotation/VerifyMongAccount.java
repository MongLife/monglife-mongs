package com.monglife.mongs.domain.mong.annotation;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VerifyMongAccount {

    String accountIdFieldName() default "accountId";

    String mongIdFieldName() default "mongId";
}
