package com.monglife.mongs.domain.taskSchedule.listener;

import com.monglife.mongs.domain.taskSchedule.dto.etc.StartTaskScheduleDto;
import com.monglife.mongs.domain.taskSchedule.dto.event.StartTaskScheduleEvent;
import com.monglife.mongs.domain.taskSchedule.dto.event.StopTaskScheduleEvent;
import com.monglife.mongs.domain.taskSchedule.service.TaskScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskScheduleEventListener {

    private final TaskScheduleService taskScheduleService;

    /**
     * 커밋 이후 실행할 테스크 스케줄러 실행 이벤트
     * postPersist 는 커밋 이후가 아닌 영속화 한 직후 실행 된다.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void startTaskScheduleEventListener(StartTaskScheduleEvent startTaskScheduleEvent) {

        StartTaskScheduleDto startTaskScheduleDto = StartTaskScheduleDto.builder()
                .taskId(startTaskScheduleEvent.getTaskId())
                .appPackageName(startTaskScheduleEvent.getAppPackageName())
                .taskOwnerId(startTaskScheduleEvent.getTaskOwnerId())
                .taskCode(startTaskScheduleEvent.getTaskCode())
                .expiredAt(startTaskScheduleEvent.getExpiredAt())
                .isCycle(startTaskScheduleEvent.getIsCycle())
                .build();

        taskScheduleService.startTaskSchedule(startTaskScheduleDto);
    }

    /**
     * 커밋 이후 실행할 테스크 스케줄러 중지 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void stopTaskScheduleEventListener(StopTaskScheduleEvent stopTaskScheduleEvent) {
        taskScheduleService.stopTaskSchedule(stopTaskScheduleEvent.getTaskId());
    }
}
