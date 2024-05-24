package com.mongs.play.app.management.internal.aspect;

import com.mongs.play.app.management.internal.annotation.ValidationEvolution;
import com.mongs.play.app.management.internal.service.ManagementInternalService;
import com.mongs.play.core.utils.ReflectionUtil;
import com.mongs.play.domain.mong.utils.MongUtil;
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
@Order(1)
@Aspect
@Component
@RequiredArgsConstructor
public class ValidationEvolutionAspect {

    private final ManagementInternalService managementInternalService;

    @Pointcut("execution(* com.mongs.play..*.*(..))")
    public void cut() {}

    @Around(value = "cut() && @annotation(annotation)")
    public Object validationEvolution(ProceedingJoinPoint joinPoint, ValidationEvolution annotation) throws Throwable {
        Object returnValue = joinPoint.proceed();

        if (!ObjectUtils.isEmpty(returnValue)) {
            Long mongId = (Long) ReflectionUtil.getField(returnValue, "mongId");
            Double expPercent = (Double) ReflectionUtil.getField(returnValue, "expPercent");
            String shiftCode = (String) ReflectionUtil.getField(returnValue, "shiftCode");

            if (MongUtil.isEvolutionReady(shiftCode, expPercent)) {
                var vo = managementInternalService.evolutionReady(mongId);
                ReflectionUtil.setField(returnValue, "shiftCode", vo.shiftCode());
            }
        }

        return returnValue;
    }
}
