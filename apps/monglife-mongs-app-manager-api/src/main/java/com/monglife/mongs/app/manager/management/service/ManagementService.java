package com.monglife.mongs.app.manager.management.service;

import com.monglife.mongs.app.manager.global.config.TaskProperties;
import com.monglife.mongs.domain.mong.dto.etc.GetFeedItemDto;
import com.monglife.mongs.domain.mong.dto.etc.GetMongDto;
import com.monglife.mongs.domain.mong.service.MongService;
import com.monglife.mongs.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementService {

    @Value("${application.app-code}")
    private String APP_CODE;

    private final TaskProperties taskProperties;


    private final MongService mongService;

    private final TaskService taskService;


    @Transactional(readOnly = true)
    public List<GetMongDto> getMongs(Long accountId) {
        return mongService.getMongs(accountId);
    }

    @Transactional(readOnly = true)
    public GetMongDto getMong(Long accountId, Long mongId) {
        return mongService.getMong(accountId, mongId);
    }

    @Transactional(readOnly = true)
    public List<GetFeedItemDto> getFeedItems(Long accountId, Long mongId, String foodTypeGroupCode) {
        return mongService.getFeedItems(accountId, mongId, foodTypeGroupCode);
    }

    @Transactional
    public void createMong(Long accountId, String name, LocalTime sleepAt, LocalTime wakeupAt) {

        Long mongId = mongService.createMong(accountId, name, sleepAt, wakeupAt);

        String taskOwnerId = String.valueOf(mongId);

        taskService.createTask(APP_CODE, taskOwnerId, taskProperties.eggEvolution.getCode(), taskProperties.eggEvolution.getExpiration());
    }

    @Transactional
    public void deleteMong(Long accountId, Long mongId) {

        mongService.deleteMong(accountId, mongId);

        String taskOwnerId = String.valueOf(mongId);

        taskService.deleteAllTasks(APP_CODE, taskOwnerId);
    }

    @Transactional
    public void feedMong(Long accountId, Long mongId, String foodTypeCode) {
        mongService.feedMong(accountId, mongId, foodTypeCode);
    }

    @Transactional
    public void strokeMong(Long accountId, Long mongId) {
        mongService.strokeMong(accountId, mongId);
    }

    @Transactional
    public void sleepMong(Long accountId, Long mongId) {

        String taskOwnerId = String.valueOf(mongId);

        if (mongService.getMong(accountId, mongId).getIsSleep()) {

            mongService.wakeupMong(accountId, mongId);

            taskService.deleteTask(APP_CODE, taskOwnerId, taskProperties.statusIncrease.getCode());
            taskService.createTask(APP_CODE, taskOwnerId, taskProperties.statusDecrease.getCode(), Boolean.TRUE, taskProperties.statusDecrease.getExpiration());
            taskService.createTask(APP_CODE, taskOwnerId, taskProperties.poopIncrease.getCode(), Boolean.TRUE, taskProperties.poopIncrease.getExpiration());

        } else {

            mongService.sleepMong(accountId, mongId);

            taskService.deleteTask(APP_CODE, taskOwnerId, taskProperties.statusDecrease.getCode());
            taskService.deleteTask(APP_CODE, taskOwnerId, taskProperties.poopIncrease.getCode());
            taskService.createTask(APP_CODE, taskOwnerId, taskProperties.statusIncrease.getCode(), Boolean.TRUE, taskProperties.statusIncrease.getExpiration());
        }
    }

    @Transactional
    public void poopCleanMong(Long accountId, Long mongId) {
        mongService.poopCleanMong(accountId, mongId);
    }

    @Transactional
    public void evolutionMong(Long accountId, Long mongId) {

        mongService.evolutionMong(accountId, mongId);

        GetMongDto getMongDto = mongService.getMong(accountId, mongId);

        if (getMongDto.getLevel() == 1) {

            String taskOwnerId = String.valueOf(mongId);

            Long sleepExpirationSeconds = Duration.between(LocalTime.now(), getMongDto.getSleepAt()).getSeconds();
            taskService.createTask(APP_CODE, taskOwnerId, taskProperties.sleep.getCode(), sleepExpirationSeconds, Boolean.TRUE, taskProperties.sleep.getExpiration());

            Long wakeupExpirationSeconds = Duration.between(LocalTime.now(), getMongDto.getWakeupAt()).getSeconds();
            taskService.createTask(APP_CODE, taskOwnerId, taskProperties.wakeup.getCode(), wakeupExpirationSeconds, Boolean.TRUE, taskProperties.wakeup.getExpiration());
        }
    }

    @Transactional
    public void graduateMong(Long accountId, Long mongId) {

        mongService.graduateMong(accountId, mongId);

        String taskOwnerId = String.valueOf(mongId);

        taskService.deleteAllTasks(APP_CODE, taskOwnerId);
    }
}
