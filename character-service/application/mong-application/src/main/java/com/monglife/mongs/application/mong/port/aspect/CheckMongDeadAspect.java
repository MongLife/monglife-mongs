package com.monglife.mongs.application.mong.port.aspect;

import com.monglife.mongs.application.mong.port.annotation.CheckMongDead;
import com.monglife.mongs.application.mong.port.annotation.PublishMongPort;
import com.monglife.mongs.application.mong.port.enums.MongSchedulerTypeCode;
import com.monglife.mongs.application.mong.port.exception.NotExistsMongException;
import com.monglife.mongs.application.mong.port.out.MongPublishPort;
import com.monglife.mongs.application.mong.port.out.MongSchedulerPort;
import com.monglife.mongs.domain.mong.model.Mong;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckMongDeadAspect {

    private final MongSchedulerPort mongSchedulerPort;

    @Around(value = "@annotation(checkMongDead)")
    public Object afterReturning(ProceedingJoinPoint pointcut, CheckMongDead checkMongDead) throws Throwable {

        Object returnValue = pointcut.proceed();

        if (returnValue instanceof Mong mong) {
            if (mong.getSatiety() == 0D || mong.getHealthy() == 0D) {
                mongSchedulerPort.createTaskPort(mong.getMongId(), MongSchedulerTypeCode.DEAD);
            } else {
                mongSchedulerPort.deleteTaskPort(mong.getMongId(), MongSchedulerTypeCode.DEAD);
            }
        } else {
            throw new NotExistsMongException();
        }

        return returnValue;
    }
}
