package com.monglife.mongs.app.manager.management.service;

import com.monglife.mongs.app.manager.global.config.TaskScheduleProperties;
import com.monglife.mongs.client.user.service.CollectionService;
import com.monglife.mongs.domain.mong.annotation.MongAccountCheck;
import com.monglife.mongs.domain.mong.dto.etc.GetFeedItemDto;
import com.monglife.mongs.domain.mong.dto.etc.GetMongDto;
import com.monglife.mongs.domain.mong.service.MongService;
import com.monglife.mongs.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagementService {

    @Value("${application.app-package-name}")
    private String APP_PACKAGE_NAME;

    private final TaskScheduleProperties taskScheduleProperties;

    private final MongService mongService;

    private final TaskService taskService;

    private final CollectionService collectionService;

    /**
     * 몽 목록 조회
     * @param accountId 계정 ID
     * @return 몽 정보 목록
     */
    @Transactional(readOnly = true)
    public List<GetMongDto> getMongs(Long accountId) {
        return mongService.getMongs(accountId);
    }

    /**
     * 몽 조회
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @return 몽 정보
     */
    @MongAccountCheck
    @Transactional(readOnly = true)
    public GetMongDto getMong(Long accountId, Long mongId) {
        return mongService.getMong(mongId);
    }

    /**
     * 몽 먹이 목록 조회
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @param foodTypeGroupCode 먹이 그룹 코드
     * @return 구매 가능 여부 포함한 먹이 목록 조회
     */
    @MongAccountCheck
    @Transactional(readOnly = true)
    public List<GetFeedItemDto> getFeedItems(Long accountId, Long mongId, String foodTypeGroupCode) {
        return mongService.getFeedItems(mongId, foodTypeGroupCode);
    }

    /**
     * 몽 생성
     * @param accountId 계정 ID
     * @param name 몽 이름
     * @param sleepAt 몽 정기 수면 시간
     * @param wakeupAt 몽 정기 기상 시간
     */
    @Transactional
    public void createMong(Long accountId, String name, LocalTime sleepAt, LocalTime wakeupAt) {

        Long mongId = mongService.createMong(accountId, name, sleepAt, wakeupAt);

        String taskOwnerId = String.valueOf(mongId);

        taskService.createTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.eggEvolution.getCode(), taskScheduleProperties.eggEvolution.getExpiration());
    }

    /**
     * 몽 삭제
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @MongAccountCheck
    @Transactional
    public void deleteMong(Long accountId, Long mongId) {

        mongService.deleteMong(mongId);

        String taskOwnerId = String.valueOf(mongId);

        taskService.deleteAllTasks(APP_PACKAGE_NAME, taskOwnerId);
    }

    /**
     * 몽 먹이 주기
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @param foodTypeCode 먹이 코드
     */
    @MongAccountCheck
    @Transactional
    public void feedMong(Long accountId, Long mongId, String foodTypeCode) {
        mongService.feedMong(mongId, foodTypeCode);
    }

    /***
     * 몽 쓰다 듬기
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @MongAccountCheck
    @Transactional
    public void strokeMong(Long accountId, Long mongId) {
        mongService.strokeMong(mongId);
    }

    /**
     * 몽 수면/기상
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @MongAccountCheck
    @Transactional
    public void sleepMong(Long accountId, Long mongId) {

        String taskOwnerId = String.valueOf(mongId);

        if (mongService.getMong(mongId).getIsSleep()) {
            mongService.wakeupMong(mongId);

            taskService.createCycleTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.statusDecrease.getCode(), taskScheduleProperties.statusDecrease.getExpiration());
            taskService.createCycleTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.poopIncrease.getCode(), taskScheduleProperties.poopIncrease.getExpiration());
            taskService.deleteTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.statusIncrease.getCode());

        } else {
            mongService.sleepMong(mongId);

            taskService.deleteTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.statusDecrease.getCode());
            taskService.deleteTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.poopIncrease.getCode());
            taskService.createCycleTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.statusIncrease.getCode(), taskScheduleProperties.statusIncrease.getExpiration());
        }
    }

    /**
     * 몽 배변 처리
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @MongAccountCheck
    @Transactional
    public void poopCleanMong(Long accountId, Long mongId) {
        mongService.poopCleanMong(mongId);
    }

    /**
     * 몽 진화
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @MongAccountCheck
    @Transactional
    public void evolutionMong(Long accountId, Long mongId) {

        GetMongDto getMongDto = mongService.evolutionMong(mongId);

        // 몽 컬렉션 등록
        collectionService.createCollectionMong(getMongDto.getMongTypeCode());

        if (getMongDto.getLevel().equals(1)) {

            String taskOwnerId = String.valueOf(mongId);

            taskService.createFixTimeCycleTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.sleep.getCode(), getMongDto.getSleepAt());
            taskService.createFixTimeCycleTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.wakeup.getCode(), getMongDto.getWakeupAt());

            taskService.createCycleTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.statusDecrease.getCode(), taskScheduleProperties.statusDecrease.getExpiration());
            taskService.createCycleTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.poopIncrease.getCode(), taskScheduleProperties.poopIncrease.getExpiration());
        }
    }

    /**
     * 몽 졸업
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @MongAccountCheck
    @Transactional
    public void graduateMong(Long accountId, Long mongId) {

        mongService.graduateMong(mongId);

        String taskOwnerId = String.valueOf(mongId);

        taskService.deleteAllTasks(APP_PACKAGE_NAME, taskOwnerId);
    }

    /**
     * 몽 페이 포인트 증가
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @param payPoint 페이 포인트
     */
    @MongAccountCheck
    @Transactional
    public void chargePayPoint(Long accountId, Long mongId, Integer payPoint) {
        mongService.increasePayPoint(mongId, payPoint);
    }
}
