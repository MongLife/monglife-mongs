package com.mongs.play.domain.mong.aspect;

import com.mongs.play.domain.mong.annotation.MongStateValidation;
import com.mongs.play.domain.mong.entity.Mong;
import com.mongs.play.domain.mong.service.MongService;
import com.mongs.play.domain.mong.vo.MongVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(2)
@Aspect
@Component
@RequiredArgsConstructor
public class MongStateValidationAspect {

    private final MongService mongService;

    @Pointcut("execution(* com.mongs.play.domain.mong..*.*(..))")
    public void cut() {}

    @Around(value = "cut() && @annotation(mongStateValidation)")
    public Object afterReturnStateValidation(ProceedingJoinPoint joinPoint, MongStateValidation mongStateValidation) throws Throwable {

        Object returnValue = joinPoint.proceed();

        if (returnValue instanceof MongVo mongVo) {
            // TODO(state 변경 로직 생성)
        }

        return returnValue;
    }
}
