package com.monglife.mongs.adapter.out.mong.persistence.service;

import com.monglife.mongs.application.mong.port.out.vo.CreateInventoryItemVo;
import com.monglife.mongs.application.mong.port.out.vo.CreateMongVo;
import com.monglife.mongs.domain.mong.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MongPersistenceService implements
        com.monglife.mongs.application.mong.port.out.MongPersistencePort,
        com.monglife.mongs.application.battle.port.out.MongPersistencePort {

    /**
     * 몽 쓰다 듬기 대기 잔여 시간 조회
     * @param mongId 몽 ID
     * @return 쓰다 듬기 대기 잔여 시간 (초)
     */
    @Override
    public Long getMongStrokeExpirationSecondsPort(Long mongId) {
        return 0L;
    }

    /**
     * 몽 쓰다 듬기 이력 등록
     * @param mongId 몽 ID
     * @param expirationSeconds 쓰다 듬기 대기 시간 (초)
     * @return 몽 쓰다 듬기 대기 시간
     */
    @Override
    public Optional<Long> createMongStrokeHistoryPort(Long mongId, Long expirationSeconds) {
        return Optional.empty();
    }

    /**
     * 몽 타입 목록 조회
     * @param level 몽 타입 레벨
     * @return 몽 타입 목록
     */
    @Override
    public List<MongType> getMongTypesPort(Integer level) {
        return List.of();
    }

    /**
     * 몽 타입 목록 조회
     * @param evolutionScore 현재 진화 점수
     * @param mongTypeCode 현재 몽 타입 코드
     * @return 몽 타입 목록
     */
    @Override
    public List<MongType> getMongTypesPort(Double evolutionScore, String mongTypeCode) {
        return List.of();
    }

    /**
     * 몽 등록
     * @param createMongVo 몽 등록 Vo
     * @return 몽 도메인 객체
     */
    @Override
    public Optional<Mong> createMongPort(CreateMongVo createMongVo) {
        return Optional.empty();
    }

    /**
     * 몽 조회
     * @param mongId 몽 ID
     * @return 몽 도메인 객체
     */
    @Override
    public Optional<Mong> getMongPort(Long mongId) {
        return Optional.empty();
    }

    /**
     * 몽 목록 조회
     * @param accountId 계정 ID
     * @return 몽 도메인 객체 목록
     */
    @Override
    public List<Mong> getMongsPort(Long accountId) {
        return List.of();
    }

    /**
     * 몽 동기화
     * @param mong 몽 도메인 객체
     * @return 몽 도메인 객체
     */
    @Override
    public Optional<Mong> saveMongPort(Mong mong) {
        return Optional.empty();
    }

    /**
     * 몽 삭제
     * @param mong 몽 도메인 객체
     * @return 몽 도메인 객체
     */
    @Override
    public Optional<Mong> deleteMongPort(Mong mong) {
        return Optional.empty();
    }

    /**
     * 음식 조회
     * @param foodTypeCode 음식 타입 코드
     * @param mongId 몽 ID
     * @return 음식 도메인 객체
     */
    @Override
    public Optional<Food> getFoodPort(String foodTypeCode, Long mongId) {
        return Optional.empty();
    }

    /**
     * 음식 목록 조회
     * @param mongId 몽 ID
     * @return 음식 도메인 객체 목록
     */
    @Override
    public List<Food> getFoodsPort(Long mongId) {
        return List.of();
    }

    /**
     * 간식 조회
     * @param snackTypeCode 간식 타입 코드
     * @param mongId 몽 ID
     * @return 간식 도메인 객체
     */
    @Override
    public Optional<Snack> getSnackPort(String snackTypeCode, Long mongId) {
        return Optional.empty();
    }

    /**
     * 간식 목록 조회
     * @param mongId 몽 ID
     * @return 간식 도메인 객체 목록
     */
    @Override
    public List<Snack> getSnacksPort(Long mongId) {
        return List.of();
    }

    /**
     * 훈련 타입 목록 조회
     * @return 훈련 타입 도메인 객체 목록
     */
    @Override
    public List<TrainingType> getTrainingTypesPort() {
        return List.of();
    }

    /**
     * 훈련 타입 조회
     * @param trainingTypeCode 훈련 타입 코드
     * @return 훈련 타입 도메인 객체
     */
    @Override
    public Optional<TrainingType> getTrainingTypePort(String trainingTypeCode) {
        return Optional.empty();
    }

    /**
     * 인벤 아이템 등록
     * @param createInventoryItemVo 인벤토리 아이템 등록 Vo
     * @return 인벤 아이템 도메인 객체
     */
    @Override
    public Optional<InventoryItem> createInventoryItemPort(CreateInventoryItemVo createInventoryItemVo) {
        return Optional.empty();
    }

    /**
     * 인벤 아이템 삭제
     * @param inventoryItemId 인벤토리 아이템 ID
     * @return 인벤 아이템 도메인 객체
     */
    @Override
    public Optional<InventoryItem> deleteInventoryItemPort(Long inventoryItemId) {
        return Optional.empty();
    }

    /**
     * 인벤 아이템 조회
     * @param inventoryItemId 인벤토리 아이템 ID
     * @return 인벤 아이템 도메인 객체
     */
    @Override
    public Optional<InventoryItem> getInventoryItemPort(Long inventoryItemId) {
        return Optional.empty();
    }

    /**
     * 인벤 아이템 목록 조회
     * @param mongId 몽 ID
     * @return 인벤 아이템 도메인 객체 목록
     */
    @Override
    public List<InventoryItem> getInventoryItemsPort(Long mongId) {
        return List.of();
    }

    /**
     * 랜덤 뽑기 아이템 목록 조회
     * @return 랜덤 뽑기 아이템 도메인 객체 목록
     */
    @Override
    public List<RandomDrawItem> getRandomDrawItemsPort() {
        return List.of();
    }
}

