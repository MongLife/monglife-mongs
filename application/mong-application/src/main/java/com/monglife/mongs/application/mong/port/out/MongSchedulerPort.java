package com.monglife.mongs.application.mong.port.out;

import com.monglife.mongs.application.mong.port.enums.MongSchedulerTypeCode;

import java.time.LocalTime;

public interface MongSchedulerPort {

    /**
     * 일회성 몽 스케줄 등록
     * @param mongId 몽 ID
     * @param mongSchedulerTypeCode 몽 스케줄 타입 코드
     */
    void createTaskPort(Long mongId, MongSchedulerTypeCode mongSchedulerTypeCode);

    /**
     * 반복성 몽 스케줄 등록
     * @param mongId 몽 ID
     * @param mongSchedulerTypeCode 몽 스케줄 타입 코드
     */
    void createCycleTaskPort(Long mongId, MongSchedulerTypeCode mongSchedulerTypeCode);

    /**
     * 고정 시간 반복성 몽 스케줄 등록
     * @param mongId 몽 ID
     * @param mongSchedulerTypeCode 몽 스케줄 타입 코드
     * @param time 고정 시간
     */
    void createFixedTimeCycleTaskPort(Long mongId, MongSchedulerTypeCode mongSchedulerTypeCode, LocalTime time);

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
