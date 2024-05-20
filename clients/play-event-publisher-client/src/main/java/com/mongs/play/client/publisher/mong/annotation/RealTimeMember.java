package com.mongs.play.client.publisher.mong.annotation;

import com.mongs.play.client.publisher.mong.code.PublishCode;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RealTimeMember {
    PublishCode[] code() default PublishCode.NULL;
}
