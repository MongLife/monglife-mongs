package com.monglife.mongs.application.mong.port.annotation;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckMongDead {
}
