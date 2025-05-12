package com.monglife.mongs.application.mong.port.annotation;

import com.monglife.mongs.application.mong.port.enums.MongSchedulerTypeCode;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CreateScheduleTaskPort {

    MongSchedulerTypeCode[] value() default {};

    MongSchedulerTypeCode[] cycle() default {};

    MongSchedulerTypeCode[] fixedTimeCycle() default {};

    int level() default Integer.MIN_VALUE;
}
