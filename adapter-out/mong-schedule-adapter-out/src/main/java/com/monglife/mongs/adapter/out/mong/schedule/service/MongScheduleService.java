package com.monglife.mongs.adapter.out.mong.schedule.service;

import com.monglife.mongs.application.mong.port.enums.MongSchedulerTypeCode;
import com.monglife.mongs.application.mong.port.out.MongSchedulerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class MongScheduleService implements MongSchedulerPort {

    /**
     * 일회성 몽 스케줄 등록
     * @param mongId 몽 ID
     * @param mongSchedulerTypeCode 몽 스케줄 타입 코드
     */
    @Override
    public void createTaskPort(Long mongId, MongSchedulerTypeCode mongSchedulerTypeCode) {

    }

    /**
     * 반복성 몽 스케줄 등록
     * @param mongId 몽 ID
     * @param mongSchedulerTypeCode 몽 스케줄 타입 코드
     */
    @Override
    public void createCycleTaskPort(Long mongId, MongSchedulerTypeCode mongSchedulerTypeCode) {

    }

    /**
     * 고정 시간 반복성 몽 스케줄 등록
     * @param mongId 몽 ID
     * @param mongSchedulerTypeCode 몽 스케줄 타입 코드
     * @param time 고정 시간
     */
    @Override
    public void createFixedTimeCycleTaskPort(Long mongId, MongSchedulerTypeCode mongSchedulerTypeCode, LocalTime time) {

    }

    /**
     * 몽 스케줄 삭제
     * @param mongId 몽 ID
     * @param mongSchedulerTypeCode 몽 스케줄 타입 코드
     */
    @Override
    public void deleteTaskPort(Long mongId, MongSchedulerTypeCode mongSchedulerTypeCode) {

    }

    /**
     * 몽 스케줄 전체 삭제
     * @param mongId 몽 ID
     */
    @Override
    public void deleteAllTaskPort(Long mongId) {

    }
}
