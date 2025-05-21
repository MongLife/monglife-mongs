package com.monglife.mongs.application.battle.port.annotation;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EndMatch {
}
