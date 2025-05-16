package com.monglife.mongs.application.mong.port.aspect;

import com.monglife.mongs.application.mong.port.annotation.CreateScheduleTaskPort;
import com.monglife.mongs.application.mong.port.enums.MongSchedulerTypeCode;
import com.monglife.mongs.application.mong.port.exception.NotExistsMongException;
import com.monglife.mongs.application.mong.port.out.MongSchedulerPort;
import com.monglife.mongs.domain.mong.model.Mong;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class CreateScheduleTaskPortAspect {

    private final MongSchedulerPort mongSchedulerPort;

    @AfterReturning(value = "@annotation(createScheduleTaskPort)", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, CreateScheduleTaskPort createScheduleTaskPort, Object returnValue) {
        if (returnValue instanceof Mong mong) {
            // 몽 스케줄 실행 레벨이 적합하지 않는 경우 실행 하지 않음
            if (createScheduleTaskPort.level() != Integer.MIN_VALUE && createScheduleTaskPort.level() != mong.getLevel()) return;

            // 일회성 스케줄러 등록
            Arrays.stream(createScheduleTaskPort.value())
                    .forEach(mongSchedulerTypeCode -> mongSchedulerPort.createTaskPort(mong.getMongId(), mongSchedulerTypeCode));

            // 반복 스케줄러 등록
            Arrays.stream(createScheduleTaskPort.cycle())
                    .forEach(mongSchedulerTypeCode -> mongSchedulerPort.createCycleTaskPort(mong.getMongId(), mongSchedulerTypeCode));

            // 고정 시간 반복 스케줄러 등록
            Arrays.stream(createScheduleTaskPort.fixedTimeCycle())
                    .filter(MongSchedulerTypeCode::getIsFixedTime)
                    .forEach(mongSchedulerTypeCode -> {
                        if (mongSchedulerTypeCode == MongSchedulerTypeCode.SLEEP) {
                            mongSchedulerPort.createFixedTimeCycleTaskPort(mong.getMongId(), mongSchedulerTypeCode, mong.getSleepAt());
                        } else if (mongSchedulerTypeCode == MongSchedulerTypeCode.WAKEUP) {
                            mongSchedulerPort.createFixedTimeCycleTaskPort(mong.getMongId(), mongSchedulerTypeCode, mong.getWakeupAt());
                        }
                    });
        } else {
            throw new NotExistsMongException();
        }
    }
}
