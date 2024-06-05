package com.mongs.play.app.management.external.aspect;

import com.mongs.play.app.management.external.annotation.ValidationDead;
import com.mongs.play.core.utils.ReflectionUtil;
import com.mongs.play.module.feign.service.ManagementWorkerFeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Slf4j
@Order(0)
@Aspect
@Component
@RequiredArgsConstructor
public class ValidationDeadAspect {

    private final ManagementWorkerFeignService managementWorkerFeignService;

    @Pointcut("execution(* com.mongs.play..*.*(..))")
    public void cut() {}

    @Around(value = "cut() && @annotation(annotation)")
    public Object validationDead(ProceedingJoinPoint joinPoint, ValidationDead annotation) throws Throwable {
        Object returnValue = joinPoint.proceed();

        if (!ObjectUtils.isEmpty(returnValue)) {
            Long mongId = (Long) ReflectionUtil.getField(returnValue, "mongId");
            Double healthy = (Double) ReflectionUtil.getField(returnValue, "healthy");
            Double satiety = (Double) ReflectionUtil.getField(returnValue, "satiety");
            Boolean isDeadSchedule = (Boolean) ReflectionUtil.getField(returnValue, "isDeadSchedule");

            if (!ObjectUtils.isEmpty(mongId) && !ObjectUtils.isEmpty(healthy) && !ObjectUtils.isEmpty(satiety)) {
                if (Boolean.FALSE.equals(isDeadSchedule) && (satiety <= 0 || healthy <= 0)) {
                    managementWorkerFeignService.deadSchedule(mongId);
                } else if(Boolean.TRUE.equals(isDeadSchedule) && (satiety > 0 && healthy > 0)) {
                    managementWorkerFeignService.deadScheduleStop(mongId);
                }
            }
        }

        return returnValue;
    }
}
