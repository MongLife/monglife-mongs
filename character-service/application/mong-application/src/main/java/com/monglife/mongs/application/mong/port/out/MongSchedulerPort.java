package com.monglife.mongs.application.mong.port.out;

import com.monglife.mongs.application.mong.port.enums.SchedulerType;

import java.time.LocalTime;
import java.util.Optional;

public interface MongSchedulerPort {

    /**
     * 일회성 몽 스케줄 등록
     * @param mongId 몽 ID
     * @param schedulerType 몽 스케줄 타입 코드
     */
    Optional<Long> createTaskPort(Long mongId, SchedulerType schedulerType);

    /**
     * 반복성 몽 스케줄 등록
     * @param mongId 몽 ID
     * @param schedulerType 몽 스케줄 타입 코드
     */
    Optional<Long> createCycleTaskPort(Long mongId, SchedulerType schedulerType);

    /**
     * 고정 시간 반복성 몽 스케줄 등록
     * @param mongId 몽 ID
     * @param schedulerType 몽 스케줄 타입 코드
     * @param time 고정 시간
     */
    Optional<Long> createFixedTimeCycleTaskPort(Long mongId, SchedulerType schedulerType, LocalTime time);

    /**
     * 몽 스케줄 삭제
     * @param mongId 몽 ID
     * @param schedulerType 몽 스케줄 타입 코드
     */
    void deleteTaskPort(Long mongId, SchedulerType schedulerType);

    /**
     * 몽 스케줄 전체 삭제
     * @param mongId 몽 ID
     */
    void deleteAllTaskPort(Long mongId);
}
