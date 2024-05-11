package com.mongs.play.domain.mong.aspect;

import com.mongs.play.domain.mong.annotation.MongEvolutionValidation;
import com.mongs.play.domain.mong.entity.Mong;
import com.mongs.play.domain.mong.enums.MongShift;
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

        Object returnValue = joinPoint.proceed();

        if (returnValue instanceof MongVo mongVo) {

            Mong mong = mongVo.mong();

            if (mong.getGrade().evolutionExp <= mong.getExp() &&
                    !MongShift.GRADUATE_READY.equals(mong.getShift()) &&
                    !MongShift.EVOLUTION_READY.equals(mong.getShift())
            ) {
                returnValue = mongVo.toBuilder()
                        .mong(mongService.evolutionReadyMong(mong.getId()).mong())
                        .build();
            }
        }

        return returnValue;
    }
}
