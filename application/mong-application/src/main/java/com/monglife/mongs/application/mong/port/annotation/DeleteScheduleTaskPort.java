package com.monglife.mongs.application.mong.port.annotation;

import com.monglife.mongs.application.mong.port.enums.MongSchedulerTypeCode;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DeleteScheduleTaskPort {

    MongSchedulerTypeCode[] value() default {};
}
