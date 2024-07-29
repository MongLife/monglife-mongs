package com.mongs.play.client.publisher.event.annotation;

import com.mongs.play.client.publisher.event.code.PublishEventCode;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RealTimeMember {
    PublishEventCode[] codes();
}
