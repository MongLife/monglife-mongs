package com.monglife.mongs.app.manager.management.service;

import com.monglife.mongs.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementService {

    @Value("${application.app-code}")
    private String APP_CODE;

    @Value("${application.scheduler.task.sleep.code}")
    private String TASK_SLEEP_CODE;
    private static final Long TASK_SLEEP_EXPIRATION = 60 * 60 * 24L;

    @Value("${application.scheduler.task.wakeup.code}")
    private String TASK_WAKEUP_CODE;
    private static final Long TASK_WAKEUP_EXPIRATION = 60 * 60 * 24L;

    @Value("${application.scheduler.task.egg-evolution.code}")
    private String TASK_EGG_EVOLUTION_CODE;
    @Value("${application.scheduler.task.egg-evolution.expiration}")
    private Long TASK_EGG_EVOLUTION_EXPIRATION;

    @Value("${application.scheduler.task.status-increase.code}")
    private String TASK_STATUS_INCREASE_CODE;
    @Value("${application.scheduler.task.status-increase.expiration}")
    private Long TASK_STATUS_INCREASE_EXPIRATION;

    @Value("${application.scheduler.task.status-decrease.code}")
    private String TASK_STATUS_DECREASE_CODE;
    @Value("${application.scheduler.task.status-decrease.expiration}")
    private Long TASK_STATUS_DECREASE_EXPIRATION;

    @Value("${application.scheduler.task.poop-increase.code}")
    private String TASK_POOP_INCREASE_CODE;
    @Value("${application.scheduler.task.poop-increase.expiration}")
    private Long TASK_POOP_INCREASE_EXPIRATION;


    private final TaskService taskService;


    @Transactional
    public void eggEvolutionScheduler(Long mongId) {

        String taskOwnerId = String.valueOf(mongId);

        taskService.createTask(APP_CODE, taskOwnerId, TASK_EGG_EVOLUTION_CODE, TASK_EGG_EVOLUTION_EXPIRATION);
    }

    @Transactional
    public void cycleSleepScheduler(Long mongId, LocalTime sleepAt, LocalTime wakeupAt) {

        String taskOwnerId = String.valueOf(mongId);

        Long sleepExpirationSeconds = Duration.between(LocalTime.now(), sleepAt).getSeconds();
        taskService.createTask(APP_CODE, taskOwnerId, TASK_SLEEP_CODE, sleepExpirationSeconds, Boolean.TRUE, TASK_SLEEP_EXPIRATION);

        Long wakeupExpirationSeconds = Duration.between(LocalTime.now(), wakeupAt).getSeconds();
        taskService.createTask(APP_CODE, taskOwnerId, TASK_WAKEUP_CODE, wakeupExpirationSeconds, Boolean.TRUE, TASK_WAKEUP_EXPIRATION);
    }

    @Transactional
    public void cycleIncreaseStatusScheduler(Long mongId) {

        String taskOwnerId = String.valueOf(mongId);

        taskService.deleteTask(APP_CODE, taskOwnerId, TASK_STATUS_DECREASE_CODE);
        taskService.deleteTask(APP_CODE, taskOwnerId, TASK_POOP_INCREASE_CODE);
        taskService.createTask(APP_CODE, taskOwnerId, TASK_STATUS_INCREASE_CODE, Boolean.TRUE, TASK_STATUS_INCREASE_EXPIRATION);
    }

    @Transactional
    public void cycleDecreaseStatusScheduler(Long mongId) {

        String taskOwnerId = String.valueOf(mongId);

        taskService.deleteTask(APP_CODE, taskOwnerId, TASK_STATUS_INCREASE_CODE);
        taskService.createTask(APP_CODE, taskOwnerId, TASK_STATUS_DECREASE_CODE, Boolean.TRUE, TASK_STATUS_DECREASE_EXPIRATION);
        taskService.createTask(APP_CODE, taskOwnerId, TASK_POOP_INCREASE_CODE, Boolean.TRUE, TASK_POOP_INCREASE_EXPIRATION);
    }

    @Transactional
    public void stopAllScheduler(Long mongId) {

        String taskOwnerId = String.valueOf(mongId);

        taskService.deleteAllTasks(APP_CODE, taskOwnerId);
    }
}
