package com.mongs.play.module.kafka.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SendCommit {
    String topic();
}
