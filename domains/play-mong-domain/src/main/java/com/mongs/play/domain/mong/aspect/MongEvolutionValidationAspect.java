package com.mongs.play.domain.mong.aspect;

import com.mongs.play.domain.mong.annotation.MongEvolutionValidation;
import com.mongs.play.domain.mong.entity.Mong;
import com.mongs.play.domain.mong.enums.MongShift;
import com.mongs.play.domain.mong.service.MongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(1)
@Aspect
@Component
@RequiredArgsConstructor
public class MongEvolutionValidationAspect {

    private final MongService mongService;

    @Pointcut("execution(* com.mongs.play.domain.mong..*.*(..))")
    public void cut() {}

    @Around(value = "cut() && @annotation(mongEvolutionValidation)")
    public Object afterReturnEvolutionCheck(ProceedingJoinPoint joinPoint, MongEvolutionValidation mongEvolutionValidation) throws Throwable {
        Mong returnMong = (Mong) joinPoint.proceed();

        if (returnMong.getGrade().evolutionExp <= returnMong.getExp() && !MongShift.EVOLUTION_READY.equals(returnMong.getShift())) {
            returnMong = mongService.evolutionReadyMong(returnMong.getId());
        }

        return returnMong;
    }
}
