package com.monglife.mongs.app.manager.management.listener;

import com.monglife.mongs.app.manager.global.config.TaskScheduleProperties;
import com.monglife.mongs.app.manager.management.dto.etc.MongBasicDto;
import com.monglife.mongs.app.manager.management.dto.etc.MongStateDto;
import com.monglife.mongs.app.manager.management.dto.etc.MongStatusDto;
import com.monglife.mongs.app.manager.management.publisher.ManagementPublisher;
import com.monglife.mongs.domain.mong.dto.event.*;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStateHistoryType;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import com.monglife.mongs.domain.mong.enums.MongStatusHistoryType;
import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import com.monglife.mongs.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MongObserveEventListener {

    @Value("${application.app-package-name}")
    private String APP_PACKAGE_NAME;

    private final TaskScheduleProperties properties;

    private final TaskService taskService;

    private final ManagementPublisher managementPublisher;

    /**
     * 몽 기본 정보 변경 이벤트 리스너
     * @param event 몽 기본 정보 변경 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void mongBasicObserveEventListener(MongBasicObserveEvent event) {

        managementPublisher.mongBasicObservePublish(event.getMongId(), MongBasicDto.builder()
                .mongId(event.getMongId())
                .mongName(event.getMongName())
                .mongTypeCode(event.getMongTypeCode())
                .payPoint(event.getPayPoint())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build());
    }

    /**
     * 몽 상태 정보 변경 이벤트 리스너
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void mongStateObserveEventListener(MongStateObserveEvent event) {

        String taskOwnerId = String.valueOf(event.getMongId());

        if (MongStateCode.DEAD.equals(event.getStateCode()) || MongStateCode.GRADUATE_READY.equals(event.getStateCode())) {
            taskService.deleteAllTasks(APP_PACKAGE_NAME, taskOwnerId);
        }

        managementPublisher.mongStateObservePublish(event.getMongId(), MongStateDto.builder()
                .mongId(event.getMongId())
                .stateCode(event.getStateCode())
                .isSleep(event.getIsSleep())
                .updatedAt(event.getUpdatedAt())
                .build());
    }

    /**
     * 몽 상태 기록 등록 이벤트 리스너
     * @param event 몽 상태 등록 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void mongStateHistoryEventListener(MongStateHistoryEvent event) {

        if (MongStateHistoryType.SET_CODE.equals(event.getType())) {

            Long accountId = event.getAccountId();
            String mongName = event.getMongName();
            MongStateCode stateCode = event.getCode();

            managementPublisher.mongStateHistoryPublish(accountId, mongName, stateCode);
        }
    }

    /**
     * 몽 지수 정보 변경 이벤트 리스너
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void mongStatusObserveEventListener(MongStatusObserveEvent event) {

        String taskOwnerId = String.valueOf(event.getMongId());

        boolean isDeadStatus = event.getSatietyRatio() <= properties.dead.satietyRatio || event.getHealthyRatio() <= properties.dead.healthyRatio;

        if (isDeadStatus) {
            // 죽음 스케줄러 존재하는 경우
            if (taskService.isExistsTask(APP_PACKAGE_NAME, taskOwnerId, properties.dead.code, TaskStatusCode.PAUSE)) {
                // 기존 죽음 스케줄러 재가동
                taskService.resumeTask(APP_PACKAGE_NAME, taskOwnerId, properties.dead.code);
            }
            // 죽음 스케줄러 없는 경우
            else if (!taskService.isExistsTask(APP_PACKAGE_NAME, taskOwnerId, properties.dead.code, TaskStatusCode.PROCESSING) && !MongStateCode.DEAD.equals(event.getStateCode())) {
                // 새로운 죽음 스케줄러 생성
                taskService.createTask(APP_PACKAGE_NAME, taskOwnerId, properties.dead.code, properties.dead.expiration);
            }
        } else {
            // 죽음 스케줄러 존재하는 경우
            if (taskService.isExistsTask(APP_PACKAGE_NAME, taskOwnerId, properties.dead.code, TaskStatusCode.PROCESSING)) {
                // 일시 중지
                taskService.pauseTask(APP_PACKAGE_NAME, taskOwnerId, properties.dead.code);
            }
        }

        managementPublisher.mongStatusObservePublish(event.getMongId(), MongStatusDto.builder()
                .mongId(event.getMongId())
                .statusCode(event.getStatusCode())
                .expRatio(event.getExpRatio())
                .weight(event.getWeight())
                .strengthRatio(event.getStrengthRatio())
                .satietyRatio(event.getSatietyRatio())
                .healthyRatio(event.getHealthyRatio())
                .fatigueRatio(event.getFatigueRatio())
                .poopCount(event.getPoopCount())
                .updatedAt(event.getUpdatedAt())
                .build());
    }

    /**
     * 몽 지수 기록 등록 이벤트 리스너
     * @param event 몽 지수 등록 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void mongStatusHistoryEventListener(MongStatusHistoryEvent event) {

        if (MongStatusHistoryType.SET_CODE.equals(event.getType())) {

            Long accountId = event.getAccountId();
            String mongName = event.getMongName();
            MongStatusCode statusCode = event.getCode();

            managementPublisher.mongStatusHistoryPublish(accountId, mongName, statusCode);
        }
    }
}
