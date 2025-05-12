package com.monglife.mongs.application.mong.port.aspect;

import com.monglife.mongs.application.mong.port.annotation.DeleteScheduleTaskPort;
import com.monglife.mongs.application.mong.port.exception.NotExistsMongException;
import com.monglife.mongs.application.mong.port.out.MongSchedulerPort;
import com.monglife.mongs.domain.model.Mong;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class DeleteScheduleTaskPortAspect {

    private final MongSchedulerPort mongSchedulerPort;

    /**
     * 몽 스케줄 삭제 Aspect
     */
    @AfterReturning(value = "@annotation(deleteScheduleTaskPort)", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, DeleteScheduleTaskPort deleteScheduleTaskPort, Object returnValue) {
        if (returnValue instanceof Mong mong) {
            if (deleteScheduleTaskPort.value().length == 0) {
                // 모든 스케줄 삭제
                mongSchedulerPort.deleteAllTaskPort(mong.getMongId());
            } else {
                // 명시 된 스케줄 삭제
                Arrays.stream(deleteScheduleTaskPort.value()).forEach(mongSchedulerTypeCode ->
                    mongSchedulerPort.deleteTaskPort(mong.getMongId(), mongSchedulerTypeCode));
            }
        } else {
            throw new NotExistsMongException();
        }
    }
}
