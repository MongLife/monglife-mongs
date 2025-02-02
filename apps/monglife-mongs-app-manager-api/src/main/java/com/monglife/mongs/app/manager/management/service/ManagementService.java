package com.monglife.mongs.app.manager.management.service;

import com.monglife.mongs.app.manager.global.config.TaskScheduleProperties;
import com.monglife.mongs.client.user.exception.InvalidGetCollectionMongException;
import com.monglife.mongs.client.user.service.CollectionService;
import com.monglife.mongs.client.user.vo.CollectionMongVo;
import com.monglife.mongs.domain.mong.annotation.AllowMongState;
import com.monglife.mongs.domain.mong.annotation.DenyMongState;
import com.monglife.mongs.domain.mong.annotation.VerifyMongAccount;
import com.monglife.mongs.domain.mong.dto.etc.IncreaseMongStatusDto;
import com.monglife.mongs.domain.mong.dto.etc.PatchMongDto;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.service.MongService;
import com.monglife.mongs.domain.mong.vo.FeedItemVo;
import com.monglife.mongs.domain.mong.vo.MongVo;
import com.monglife.mongs.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementService {

    @Value("${application.app-package-name}")
    private String APP_PACKAGE_NAME;

    private final TaskScheduleProperties properties;

    private final MongService mongService;

    private final TaskService taskService;

    private final CollectionService collectionService;

    /**
     * 몽 목록 조회
     * @param accountId 계정 ID
     * @return 몽 정보 목록
     */
    @Transactional(readOnly = true)
    public List<MongVo> getMongs(Long accountId) {
        return mongService.getMongs(accountId);
    }

    @Transactional(readOnly = true)
    public MongVo getMong(Long mongId) {
        return mongService.getMong(mongId);
    }

    /**
     * 몽 조회
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @return 몽 정보
     */
    @Transactional(readOnly = true)
    @VerifyMongAccount
    public MongVo getMong(Long accountId, Long mongId) {
        return mongService.getMong(mongId);
    }

    /**
     * 몽 먹이 목록 조회
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @param foodTypeGroupCode 먹이 그룹 코드
     * @return 구매 가능 여부 포함한 먹이 목록 조회
     */
    @Transactional(readOnly = true)
    @VerifyMongAccount
    public List<FeedItemVo> getFeedItems(Long accountId, Long mongId, String foodTypeGroupCode) {

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

        MongVo mongVo = mongService.createMong(accountId, name, sleepAt, wakeupAt);

        String taskOwnerId = String.valueOf(mongVo.getMongId());
        String mongTypeCode = mongVo.getMongTypeCode();

        taskService.createTask(APP_PACKAGE_NAME, taskOwnerId, properties.eggEvolution.code, properties.eggEvolution.expiration);

        collectionService.createCollectionMong(mongTypeCode);
    }

    /**
     * 몽 삭제
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @Transactional
    @VerifyMongAccount
    public void deleteMong(Long accountId, Long mongId) {

        mongService.deleteMong(mongId);

        String taskOwnerId = String.valueOf(mongId);

        taskService.deleteAllTasks(APP_PACKAGE_NAME, taskOwnerId);
    }

    /**
     * 몽 먹이 주기
     * 불가능 상태 : 수면, 죽음, 알
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @param foodTypeCode 먹이 코드
     */
    @Transactional
    @VerifyMongAccount
    @DenyMongState(stateCodes = { MongStateCode.DEAD }, sleep = true, egg = true)
    public void feedMong(Long accountId, Long mongId, String foodTypeCode) {

        mongService.feedMong(mongId, foodTypeCode);
    }

    /***
     * 몽 쓰다 듬기
     * 불가능 상태 : 졸업 대기, 수면, 죽음, 알
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @Transactional
    @VerifyMongAccount
    @DenyMongState(stateCodes = { MongStateCode.DEAD, MongStateCode.GRADUATE_READY }, sleep = true, egg = true)
    public void strokeMong(Long accountId, Long mongId) {

        mongService.strokeMong(mongId);
    }

    /**
     * 몽 수면/기상
     * 불가능 상태 : 졸업 대기, 죽음, 알
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @Transactional
    @VerifyMongAccount
    @DenyMongState(stateCodes = { MongStateCode.DEAD, MongStateCode.GRADUATE_READY }, egg = true)
    public void sleepMong(Long accountId, Long mongId) {

        String taskOwnerId = String.valueOf(mongId);

        if (mongService.getMong(mongId).getIsSleep()) {
            mongService.wakeupMong(mongId);
            taskService.deleteTask(APP_PACKAGE_NAME, taskOwnerId, properties.increaseStatus.code);
            taskService.createCycleTask(APP_PACKAGE_NAME, taskOwnerId, properties.decreaseStatus.code, properties.decreaseStatus.expiration);
            taskService.createCycleTask(APP_PACKAGE_NAME, taskOwnerId, properties.increasePoop.code, properties.increasePoop.expiration);

        } else {
            mongService.sleepMong(mongId);
            taskService.deleteTask(APP_PACKAGE_NAME, taskOwnerId, properties.decreaseStatus.code);
            taskService.deleteTask(APP_PACKAGE_NAME, taskOwnerId, properties.increasePoop.code);
            taskService.createCycleTask(APP_PACKAGE_NAME, taskOwnerId, properties.increaseStatus.code, properties.increaseStatus.expiration);
        }
    }

    /**
     * 몽 배변 처리
     * 불가능 상태 : 졸업 대기, 수면, 죽음, 알
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @Transactional
    @VerifyMongAccount
    @DenyMongState(stateCodes = { MongStateCode.DEAD, MongStateCode.GRADUATE_READY }, sleep = true, egg = true)
    public void poopCleanMong(Long accountId, Long mongId) {
        mongService.poopCleanMong(mongId);
    }

    /**
     * 몽 진화
     * 불가능 상태 : 졸업 대기, 죽음
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @Transactional
    @VerifyMongAccount
    @DenyMongState(stateCodes = { MongStateCode.DEAD, MongStateCode.GRADUATE_READY })
    @AllowMongState(stateCodes = { MongStateCode.EVOLUTION_READY })
    public void evolutionMong(Long accountId, Long mongId) {

        List<CollectionMongVo> collectionMongVos = Collections.emptyList();

        try {
            collectionMongVos = collectionService.getCollectionMongTypeCodes();
        } catch (InvalidGetCollectionMongException e) {
            log.warn("[{}] {}", e.getResponse().getCode(), e.getResponse().getMessage());
        }

        List<String> collectionMongTypeCodes = collectionMongVos.stream()
                .filter(CollectionMongVo::getIsIncluded)
                .map(CollectionMongVo::getMongTypeCode)
                .toList();

        MongVo mongVo = mongService.evolutionMong(mongId, collectionMongTypeCodes);

        if (mongVo.getLevel().equals(1)) {

            String taskOwnerId = String.valueOf(mongId);

            taskService.createFixTimeCycleTask(APP_PACKAGE_NAME, taskOwnerId, properties.sleep.code, mongVo.getSleepAt());
            taskService.createFixTimeCycleTask(APP_PACKAGE_NAME, taskOwnerId, properties.wakeup.code, mongVo.getWakeupAt());

            taskService.createCycleTask(APP_PACKAGE_NAME, taskOwnerId, properties.decreaseStatus.code, properties.decreaseStatus.expiration);
            taskService.createCycleTask(APP_PACKAGE_NAME, taskOwnerId, properties.increasePoop.code, properties.increasePoop.expiration);
        }

        // 몽 컬렉션 등록
        collectionService.createCollectionMong(mongVo.getMongTypeCode());
    }

    /**
     * 몽 졸업
     * 불가능 상태 : 죽음, 알
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @Transactional
    @VerifyMongAccount
    @AllowMongState(stateCodes = { MongStateCode.GRADUATE_READY })
    @DenyMongState(stateCodes = { MongStateCode.DEAD }, egg = true)
    public void graduateMong(Long accountId, Long mongId) {

        mongService.graduateMong(mongId);

        String taskOwnerId = String.valueOf(mongId);

        taskService.deleteAllTasks(APP_PACKAGE_NAME, taskOwnerId);
    }

    /**
     * 몽 페이 포인트 증가
     * @param mongId 몽 ID
     * @param payPoint 페이 포인트
     */
    @Transactional
    public void chargePayPoint(Long mongId, Integer payPoint) {
        mongService.increasePayPoint(mongId, payPoint);
    }

    /**
     * TODO: 삭제
     * 몽 훈련 이후 갱신
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @param patchMongDto 몽 정보 변경 Dto
     */
    @Transactional
    @VerifyMongAccount
    @DenyMongState(stateCodes = { MongStateCode.DEAD }, egg = true)
    public void trainingMong(Long accountId, Long mongId, PatchMongDto patchMongDto) {
        mongService.trainingMong(mongId, patchMongDto);
    }

    /**
     * TODO: 삭제
     * 배틀 이후 갱신
     * @param mongId
     * @param exp
     * @param payPoint
     */
    @Transactional
    @DenyMongState(stateCodes = { MongStateCode.DEAD }, egg = true)
    public void battleMong(Long mongId, Double exp, Integer payPoint) {
        mongService.battleMong(mongId, exp, payPoint);
    }
}
