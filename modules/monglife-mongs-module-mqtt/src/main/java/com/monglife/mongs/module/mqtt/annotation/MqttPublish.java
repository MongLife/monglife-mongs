package com.monglife.mongs.module.mqtt.annotation;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MqttPublish {

    String value() default "";
}
