package com.monglife.mongs.application.mong.port.out;

import com.monglife.mongs.application.mong.port.enums.MongSchedulerTypeCode;

import java.time.LocalTime;
import java.util.Optional;

public interface MongSchedulerPort {

    /**
     * 일회성 몽 스케줄 등록
     * @param mongId 몽 ID
     * @param mongSchedulerTypeCode 몽 스케줄 타입 코드
     */
    Optional<Long> createTaskPort(Long mongId, MongSchedulerTypeCode mongSchedulerTypeCode);

    /**
     * 반복성 몽 스케줄 등록
     * @param mongId 몽 ID
     * @param mongSchedulerTypeCode 몽 스케줄 타입 코드
     */
    Optional<Long> createCycleTaskPort(Long mongId, MongSchedulerTypeCode mongSchedulerTypeCode);

    /**
     * 고정 시간 반복성 몽 스케줄 등록
     * @param mongId 몽 ID
     * @param mongSchedulerTypeCode 몽 스케줄 타입 코드
     * @param time 고정 시간
     */
    Optional<Long> createFixedTimeCycleTaskPort(Long mongId, MongSchedulerTypeCode mongSchedulerTypeCode, LocalTime time);

    /**
     * @hidden
     * Task 일시 중지
     * @param mongId 몽 ID
     * @param mongSchedulerTypeCode 몽 스케줄 타입 코드
     */
    Optional<Long> pauseTask(Long mongId, MongSchedulerTypeCode mongSchedulerTypeCode);

    /**
     * @hidden
     * Task 재기동
     * @param mongId 몽 ID
     * @param mongSchedulerTypeCode 몽 스케줄 타입 코드
     */
    Optional<Long> resumeTask(Long mongId, MongSchedulerTypeCode mongSchedulerTypeCode);

    /**
     * 몽 스케줄 삭제
     * @param mongId 몽 ID
     * @param mongSchedulerTypeCode 몽 스케줄 타입 코드
     */
    void deleteTaskPort(Long mongId, MongSchedulerTypeCode mongSchedulerTypeCode);

    /**
     * 몽 스케줄 전체 삭제
     * @param mongId 몽 ID
     */
    void deleteAllTaskPort(Long mongId);
}
