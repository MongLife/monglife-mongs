package com.monglife.mongs.application.mong.port.out;

import com.monglife.mongs.application.mong.port.out.vo.CreateInventoryVo;
import com.monglife.mongs.application.mong.port.out.vo.CreateMongVo;
import com.monglife.mongs.domain.mong.model.*;

import java.util.Optional;

public interface MongPersistencePort {

    /**
     * 몽 쓰다 듬기 이력 등록
     * @param mongId 몽 ID
     * @param expirationSeconds 쓰다 듬기 대기 시간 (초)
     * @return 몽 쓰다 듬기 대기 시간
     */
    Optional<Long> createMongStrokeHistoryPort(Long mongId, Long expirationSeconds);

    /**
     * 몽 음식 섭취 이력 등록
     * @param mongId 몽 ID
     * @param foodCode 음식 코드
     * @return 음식 코드
     */
    Optional<String> createMongFeedFoodHistoryPort(Long mongId, String foodCode);

    /**
     * 몽 간식 섭취 이력 등록
     * @param mongId 몽 ID
     * @param snackCode 간식 코드
     * @return 간식 코드
     */
    Optional<String> createMongFeedSnackHistoryPort(Long mongId, String snackCode);

    /**
     * 몽 등록
     * @param createMongVo 몽 등록 Vo
     * @return 생성한 몽 도메인 객체
     */
    Optional<Mong> createMongPort(CreateMongVo createMongVo);

    /**
     * 몽 조회
     * @param mongId 몽 ID
     * @return 몽 도메인 객체
     */
    Optional<Mong> getMongPort(Long mongId);

    /**
     * 몽 정보 수정
     * @param mong 몽 도메인 객체
     * @return 수정한 몽 도메인 객체
     */
    Optional<Mong> saveMongPort(Mong mong);

    /**
     * 몽 삭제
     * @param mong 몽 도메인 객체
     */
    Optional<Mong> deleteMongPort(Mong mong);

    /**
     * 인벤토리 아이템 등록
     * @param createInventoryVo 인벤토리 아이템 등록 Vo
     * @return 등록한 인벤토리 아이템 도메인 객체
     */
    Optional<Inventory> createInventoryPort(CreateInventoryVo createInventoryVo);

    /**
     * 인벤토리 아이템 삭제
     * @param inventoryId 인벤토리 아이템 ID
     */
    Optional<Inventory> deleteInventoryPort(Long inventoryId);

    /**
     * 인벤토리 아이템 조회
     * @param inventoryId 인벤토리 아이템 ID
     * @return 인벤토리 아이템 도메인 객체
     */
    Optional<Inventory> getInventoryPort(Long inventoryId);
}
