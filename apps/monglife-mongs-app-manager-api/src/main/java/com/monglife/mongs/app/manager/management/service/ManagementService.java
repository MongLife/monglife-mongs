package com.monglife.mongs.app.manager.management.service;

import com.monglife.mongs.app.manager.global.config.TaskScheduleProperties;
import com.monglife.mongs.domain.mong.annotation.MongAccountCheck;
import com.monglife.mongs.domain.mong.dto.etc.GetFeedItemDto;
import com.monglife.mongs.domain.mong.dto.etc.GetMongDto;
import com.monglife.mongs.domain.mong.service.MongService;
import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import com.monglife.mongs.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagementService {

    @Value("${application.app-code}")
    private String APP_CODE;

    private final TaskScheduleProperties taskScheduleProperties;

    private final MongService mongService;

    private final TaskService taskService;


    @Transactional(readOnly = true)
    public List<GetMongDto> getMongs(Long accountId) {
        return mongService.getMongs(accountId);
    }

    @MongAccountCheck
    @Transactional(readOnly = true)
    public GetMongDto getMong(Long accountId, Long mongId) {
        return mongService.getMong(mongId);
    }

    @MongAccountCheck
    @Transactional(readOnly = true)
    public List<GetFeedItemDto> getFeedItems(Long accountId, Long mongId, String foodTypeGroupCode) {
        return mongService.getFeedItems(mongId, foodTypeGroupCode);
    }

    @Transactional
    public void createMong(Long accountId, String name, LocalTime sleepAt, LocalTime wakeupAt) {

        Long mongId = mongService.createMong(accountId, name, sleepAt, wakeupAt);

        String taskOwnerId = String.valueOf(mongId);

        taskService.createTask(APP_CODE, taskOwnerId, taskScheduleProperties.eggEvolution.getCode(), taskScheduleProperties.eggEvolution.getExpiration(), TaskStatusCode.PROCESSING);
    }

    @MongAccountCheck
    @Transactional
    public void deleteMong(Long accountId, Long mongId) {

        mongService.deleteMong(mongId);

        String taskOwnerId = String.valueOf(mongId);

        taskService.deleteAllTasks(APP_CODE, taskOwnerId);
    }

    @MongAccountCheck
    @Transactional
    public void feedMong(Long accountId, Long mongId, String foodTypeCode) {
        mongService.feedMong(mongId, foodTypeCode);
    }

    @MongAccountCheck
    @Transactional
    public void strokeMong(Long accountId, Long mongId) {
        mongService.strokeMong(mongId);
    }

    @MongAccountCheck
    @Transactional
    public void sleepMong(Long accountId, Long mongId) {

        String taskOwnerId = String.valueOf(mongId);

        if (mongService.getMong(mongId).getIsSleep()) {

            mongService.wakeupMong(mongId);

            taskService.pauseTask(APP_CODE, taskOwnerId, taskScheduleProperties.statusIncrease.getCode());
            taskService.resumeTask(APP_CODE, taskOwnerId, taskScheduleProperties.statusDecrease.getCode());
            taskService.resumeTask(APP_CODE, taskOwnerId, taskScheduleProperties.poopIncrease.getCode());

        } else {

            mongService.sleepMong(mongId);

            taskService.pauseTask(APP_CODE, taskOwnerId, taskScheduleProperties.statusDecrease.getCode());
            taskService.pauseTask(APP_CODE, taskOwnerId, taskScheduleProperties.poopIncrease.getCode());
            taskService.resumeTask(APP_CODE, taskOwnerId, taskScheduleProperties.statusIncrease.getCode());
        }
    }

    @MongAccountCheck
    @Transactional
    public void poopCleanMong(Long accountId, Long mongId) {
        mongService.poopCleanMong(mongId);
    }

    @MongAccountCheck
    @Transactional
    public void evolutionMong(Long accountId, Long mongId) {

        GetMongDto getMongDto = mongService.getMong(mongId);

        mongService.evolutionMong(mongId);

        if (getMongDto.getIsEgg()) {

            String taskOwnerId = String.valueOf(mongId);

            taskService.deleteTask(APP_CODE, taskOwnerId, taskScheduleProperties.eggEvolution.getCode());

            Long sleepExpirationSeconds = Duration.between(LocalTime.now(), getMongDto.getSleepAt()).getSeconds();
            if (sleepExpirationSeconds < 0) sleepExpirationSeconds = taskScheduleProperties.wakeup.getExpiration() + sleepExpirationSeconds;
            taskService.createTask(APP_CODE, taskOwnerId, taskScheduleProperties.sleep.getCode(), sleepExpirationSeconds, Boolean.TRUE, taskScheduleProperties.sleep.getExpiration(), TaskStatusCode.PROCESSING);

            Long wakeupExpirationSeconds = Duration.between(LocalTime.now(), getMongDto.getWakeupAt()).getSeconds();
            if (wakeupExpirationSeconds < 0) wakeupExpirationSeconds = taskScheduleProperties.wakeup.getExpiration() + wakeupExpirationSeconds;
            taskService.createTask(APP_CODE, taskOwnerId, taskScheduleProperties.wakeup.getCode(), wakeupExpirationSeconds, Boolean.TRUE, taskScheduleProperties.wakeup.getExpiration(), TaskStatusCode.PROCESSING);

            taskService.createTask(APP_CODE, taskOwnerId, taskScheduleProperties.statusIncrease.getCode(), taskScheduleProperties.statusIncrease.getExpiration(), Boolean.TRUE, taskScheduleProperties.statusIncrease.getExpiration(), TaskStatusCode.PAUSE);
            taskService.createTask(APP_CODE, taskOwnerId, taskScheduleProperties.statusDecrease.getCode(), taskScheduleProperties.statusDecrease.getExpiration(), Boolean.TRUE, taskScheduleProperties.statusDecrease.getExpiration(), TaskStatusCode.PROCESSING);
            taskService.createTask(APP_CODE, taskOwnerId, taskScheduleProperties.poopIncrease.getCode(), taskScheduleProperties.poopIncrease.getExpiration(), Boolean.TRUE, taskScheduleProperties.poopIncrease.getExpiration(), TaskStatusCode.PROCESSING);
            taskService.createTask(APP_CODE, taskOwnerId, taskScheduleProperties.dead.getCode(), taskScheduleProperties.dead.getExpiration(), Boolean.TRUE, taskScheduleProperties.dead.getExpiration(), TaskStatusCode.PAUSE);
        }
    }

    @MongAccountCheck
    @Transactional
    public void graduateMong(Long accountId, Long mongId) {

        mongService.graduateMong(mongId);

        String taskOwnerId = String.valueOf(mongId);

        taskService.deleteAllTasks(APP_CODE, taskOwnerId);
    }
}
