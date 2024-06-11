package com.mongs.play.client.publisher.annotation;

import com.mongs.play.client.publisher.code.PublishCode;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RealTimeMember {
    PublishCode[] codes();
}
