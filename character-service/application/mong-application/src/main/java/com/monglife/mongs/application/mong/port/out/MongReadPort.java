package com.monglife.mongs.application.mong.port.out;

import com.monglife.mongs.domain.mong.model.*;

import java.util.List;
import java.util.Optional;

public interface MongReadPort {

    /**
     * 몽 쓰다 듬기 대기 시간 조회
     * @param mongId 몽 ID
     * @return 다음 쓰다 듬기 가능까지 남은 시간
     */
    Long getMongStrokeExpirationSecondsPort(Long mongId);

    /**
     * 몽 타입 목록 조회
     * @param level 몽 타입 레벨
     * @return 레벨 기준 몽 타입 목록 조회
     */
    List<MongType> getMongTypesPort(Integer level);

    /**
     * 다음 레벨 몽 타입 목록 조회
     * @param evolutionScore 현재 진화 점수
     * @param mongTypeCode 현재 몽 타입 코드
     * @return 현재 진화 점수 기준 진화 가능한 몽 타입 목록
     */
    List<MongType> getNextLevelMongTypesPort(Double evolutionScore, String mongTypeCode);

    /**
     * 몽 조회
     * @param mongId 몽 ID
     * @return 몽 도메인 객체
     */
    Optional<Mong> getMongPort(Long mongId);

    /**
     * 몽 목록 조회
     * @param accountId 계정 ID
     * @return 몽 도메인 객체 목록
     */
    List<Mong> getMongsPort(Long accountId);

    /**
     * 랜덤 뽑기 아이템 목록 조회
     * @return 랜덤 뽑기 아이템 도메인 객체 목록
     */
    List<RandomDrawItem> getRandomDrawItemsPort();

    /**
     * 음식 조회
     * @param foodTypeCode 음식 타입 코드
     * @param mongId 몽 ID
     * @return 음식 도메인 객체
     */
    Optional<Food> getFoodPort(String foodTypeCode, Long mongId);

    /**
     * 음식 목록 조회
     * @param mongId 몽 ID
     * @return 음식 도메인 객체 목록
     */
    List<Food> getFoodsPort(Long mongId);

    /**
     * 간식 조회
     * @param snackTypeCode 간식 타입 코드
     * @param mongId 몽 ID
     * @return 간식 도메인 객체
     */
    Optional<Snack> getSnackPort(String snackTypeCode, Long mongId);

    /**
     * 간식 목록 조회
     * @param mongId 몽 ID
     * @return 간식 도메인 객체 목록
     */
    List<Snack> getSnacksPort(Long mongId);

    /**
     * 훈련 타입 목록 조회
     * @return 훈련 타입 도메인 객체 목록
     */
    List<TrainingType> getTrainingTypesPort();

    /**
     * 훈련 타입 조회
     * @param trainingTypeCode 훈련 타입 코드
     * @return 훈련 타입 도메인 객체
     */
    Optional<TrainingType> getTrainingTypePort(String trainingTypeCode);

    /**
     * 인벤토리 아이템 목록 조회
     * @param mongId 몽 ID
     * @return 인벤토리 아이템 도메인 객체 목록
     */
    List<InventoryItem> getInventoryItemsPort(Long mongId);
}
