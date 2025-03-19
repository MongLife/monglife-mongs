package com.monglife.mongs.app.manager.management.listener;

import com.monglife.mongs.app.manager.global.config.TaskScheduleProperties;
import com.monglife.mongs.domain.mong.exception.NotExistsMongException;
import com.monglife.mongs.domain.mong.service.MongService;
import com.monglife.mongs.domain.task.dto.event.DeleteCycleTaskEvent;
import com.monglife.mongs.domain.task.dto.event.DeleteTaskEvent;
import com.monglife.mongs.domain.task.dto.event.ExecuteCycleTaskEvent;
import com.monglife.mongs.domain.task.dto.event.ExecuteTaskEvent;
import com.monglife.mongs.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEventListener {

    @Value("${application.app-package-name}")
    private String APP_PACKAGE_NAME;

    private final TaskScheduleProperties properties;

    private final MongService mongService;

    private final TaskService taskService;

    /**
     * 일회성 Task 완료 이벤트 리스너
     * 알 진화, 몽 사망
     * @param event 일회성 Task 완료 이벤트
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void executeTaskEventListener(ExecuteTaskEvent event) {

        // 앱 코드 확인
        if (!APP_PACKAGE_NAME.equals(event.getAppPackageName())) return;

        String taskCode = event.getTaskCode();
        Long mongId = Long.parseLong(event.getTaskOwnerId());

        try {
            if (taskCode.equals(properties.eggEvolution.code)) {
                // 알 진화 준비
                mongService.evolutionReadyMong(mongId);
            } else if (taskCode.equals(properties.dead.code)) {
                // 사망
                mongService.deadMong(mongId);
            }
        } catch (NotExistsMongException e) {
            // 몽이 존재하지 않는 경우 모든 Task 삭제
            taskService.deleteAllTasks(APP_PACKAGE_NAME, event.getTaskOwnerId());
        }
    }


    /**
     * 반복 Task 완료 이벤트 리스너
     * 지수 증가, 지수 감소, 배변 증가, 정기 수면, 정기 기상
     * @param event 반복 Task 완료 이벤트
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void executeCycleTaskEventListener(ExecuteCycleTaskEvent event) {

        // 앱 코드 확인
        if (!APP_PACKAGE_NAME.equals(event.getAppPackageName())) return;

        String taskCode = event.getTaskCode();
        Long mongId = Long.parseLong(event.getTaskOwnerId());

        try {
            // 반복 테스크
            if (taskCode.equals(properties.increaseStatus.code)) {
                // 지수 증가
                mongService.increaseMongStatus(mongId, properties.increaseStatus.toIncreaseMongStatusDto());
            } else if (taskCode.equals(properties.decreaseStatus.code)) {
                // 지수 감소
                mongService.decreaseMongStatus(mongId, properties.decreaseStatus.toDecreaseMongStatusDto());
            } else if (taskCode.equals(properties.increasePoop.code)) {
                // 배변 증가
                mongService.increasePoop(mongId, properties.increasePoop.poopCount);
            } else if (taskCode.equals(properties.sleep.code)) {
                // 정기 수면
                taskService.deleteTask(APP_PACKAGE_NAME, event.getTaskOwnerId(), properties.decreaseStatus.code);
                taskService.deleteTask(APP_PACKAGE_NAME, event.getTaskOwnerId(), properties.increasePoop.code);
                taskService.createCycleTask(APP_PACKAGE_NAME, event.getTaskOwnerId(), properties.increaseStatus.code, properties.increaseStatus.expiration);
                mongService.sleepMong(mongId);
            } else if (taskCode.equals(properties.wakeup.code)) {
                // 정기 기상
                taskService.createCycleTask(APP_PACKAGE_NAME, event.getTaskOwnerId(), properties.decreaseStatus.code, properties.decreaseStatus.expiration);
                taskService.createCycleTask(APP_PACKAGE_NAME, event.getTaskOwnerId(), properties.increasePoop.code, properties.increasePoop.expiration);
                taskService.deleteTask(APP_PACKAGE_NAME, event.getTaskOwnerId(), properties.increaseStatus.code);
                mongService.wakeupMong(mongId);
            }
        } catch (NotExistsMongException e) {
            // 몽이 존재하지 않는 경우 모든 Task 삭제
            taskService.deleteAllTasks(APP_PACKAGE_NAME, event.getTaskOwnerId());
        }
    }

    /**
     * 일회성 Task 삭제 이벤트 리스너
     * 일회성 Task 는 삭제 시, 발생할 이벤트 없음.
     * @param ignored 일회성 Task 삭제 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteTaskEventListener(DeleteTaskEvent ignored) {}

    /**
     * 반복 Task 삭제 이벤트 리스너
     * 지수 일부 증가, 지수 일부 감소, 배변 일부 증가
     * @param event 반복 Task 삭제 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteCycleTaskEventListener(DeleteCycleTaskEvent event) {

        // 앱 코드 확인
        if (!APP_PACKAGE_NAME.equals(event.getAppPackageName())) return;

        String taskCode = event.getTaskCode();
        Long mongId = Long.parseLong(event.getTaskOwnerId());

        long restExpirationSeconds = Math.max(0, Duration.between(LocalDateTime.now(), event.getExpiredAt()).toSeconds());
        long expirationSeconds = Math.max(1, event.getExpirationSeconds());

        double percentage = restExpirationSeconds == 0 ? 1 : (double) (expirationSeconds - restExpirationSeconds) / (double) expirationSeconds;

        try {
            if (taskCode.equals(properties.increaseStatus.code)) {
                // 지수 증가
                mongService.increaseMongStatus(mongId, properties.increaseStatus.toIncreaseMongStatusDto(percentage));
            } else if (taskCode.equals(properties.decreaseStatus.code)) {
                // 지수 감소
                mongService.decreaseMongStatus(mongId, properties.decreaseStatus.toDecreaseMongStatusDto(percentage));
            } else if (taskCode.equals(properties.increasePoop.code) && percentage >= 0.5) {
                // 배변 증가
                mongService.increasePoop(mongId, properties.increasePoop.poopCount);
            }
        } catch (NotExistsMongException e) {
            // 몽이 존재하지 않는 경우 모든 Task 삭제
            taskService.deleteAllTasks(APP_PACKAGE_NAME, event.getTaskOwnerId());
        }
    }
}
