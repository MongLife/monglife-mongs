package com.monglife.mongs.application.mong.port.aspect;

import com.monglife.mongs.application.mong.port.annotation.CheckMongDead;
import com.monglife.mongs.application.mong.port.enums.MongSchedulerType;
import com.monglife.mongs.application.mong.port.exception.NotExistsMongException;
import com.monglife.mongs.application.mong.port.out.MongSchedulerPort;
import com.monglife.mongs.domain.mong.model.Mong;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckMongDeadAspect {

    private final MongSchedulerPort mongSchedulerPort;

    @AfterReturning(value = "@annotation(checkMongDead)", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, CheckMongDead checkMongDead, Object returnValue) {

        if (returnValue instanceof Mong mong) {
            if (mong.getSatiety() == 0D || mong.getHealthy() == 0D) {
                mongSchedulerPort.createTaskPort(mong.getMongId(), mong.getAccountId(), MongSchedulerType.DEAD);
            } else {
                mongSchedulerPort.deleteTaskPort(mong.getMongId(), MongSchedulerType.DEAD);
            }
        } else {
            throw new NotExistsMongException();
        }
    }
}
