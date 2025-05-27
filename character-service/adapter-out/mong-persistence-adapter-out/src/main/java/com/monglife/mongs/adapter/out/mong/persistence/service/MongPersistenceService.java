package com.monglife.mongs.adapter.out.mong.persistence.service;

import com.monglife.core.utils.CommonUtil;
import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import com.monglife.mongs.adapter.out.mong.persistence.entity.*;
import com.monglife.mongs.adapter.out.mong.persistence.repository.*;
import com.monglife.mongs.application.mong.port.out.vo.CreateInventoryVo;
import com.monglife.mongs.application.mong.port.out.vo.CreateMongVo;
import com.monglife.mongs.domain.mong.model.Inventory;
import com.monglife.mongs.domain.mong.model.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MongPersistenceService implements
        com.monglife.mongs.application.mong.port.out.MongPersistencePort,
        com.monglife.mongs.application.battle.port.out.MongPersistencePort {

    private final ComnCodeRepository comnCodeRepository;

    private final MongStrokeHistoryRepository mongStrokeHistoryRepository;

    private final MongFeedHistoryRepository mongFeedHistoryRepository;

    private final FoodTypeRepository foodTypeRepository;

    private final SnackTypeRepository snackTypeRepository;

    private final MongTypeRepository mongTypeRepository;

    private final MongRepository mongRepository;

    private final InventoryItemRepository inventoryItemRepository;

    /**
     * 몽 쓰다 듬기 이력 등록
     * @param mongId 몽 ID
     * @param expirationSeconds 쓰다 듬기 대기 시간 (초)
     * @return 몽 쓰다 듬기 대기 시간
     */
    @Override
    @Transactional
    public Optional<Long> createMongStrokeHistoryPort(Long mongId, Long expirationSeconds) {

        MongStrokeHistoryEntity mongStrokeHistoryEntity = MongStrokeHistoryEntity.builder()
                .mongStrokeHistoryId(CommonUtil.randomId())
                .mongId(mongId)
                .strokedAt(LocalDateTime.now())
                .expiration(expirationSeconds)
                .build();

        mongStrokeHistoryEntity = mongStrokeHistoryRepository.save(mongStrokeHistoryEntity);

        return Optional.of(mongStrokeHistoryEntity.getExpiration());
    }

    /**
     * 몽 음식 섭취 이력 등록
     * @param mongId 몽 ID
     * @param foodTypeCode 음식 코드
     * @return 음식 코드
     */
    @Override
    @Transactional
    public Optional<String> createMongFeedFoodHistoryPort(Long mongId, String foodTypeCode) {

        Optional<FoodEntity> foodTypeEntityOptional = foodTypeRepository.findByComnCode(foodTypeCode);

        if (foodTypeEntityOptional.isPresent()) {
            FoodEntity foodEntity = foodTypeEntityOptional.get();

            MongFeedHistoryEntity mongFeedHistoryEntity = MongFeedHistoryEntity.builder()
                    .mongFeedHistoryId(CommonUtil.randomId())
                    .mongId(mongId)
                    .code(foodEntity.getComn().getCode())
                    .boughtAt(LocalDateTime.now())
                    .expiration((long) foodEntity.getDelaySeconds())
                    .build();

            return Optional.of(mongFeedHistoryRepository.save(mongFeedHistoryEntity).getCode());
        }

        return Optional.empty();
    }

    /**
     * 몽 간식 섭취 이력 등록
     * @param mongId 몽 ID
     * @param snackTypeCode 간식 코드
     * @return 간식 코드
     */
    @Override
    @Transactional
    public Optional<String> createMongFeedSnackHistoryPort(Long mongId, String snackTypeCode) {

        Optional<SnackEntity> snackTypeEntityOptional = snackTypeRepository.findByComnCode(snackTypeCode);

        if (snackTypeEntityOptional.isPresent()) {
            SnackEntity snackEntity = snackTypeEntityOptional.get();

            MongFeedHistoryEntity mongFeedHistoryEntity = MongFeedHistoryEntity.builder()
                    .mongFeedHistoryId(CommonUtil.randomId())
                    .mongId(mongId)
                    .code(snackEntity.getComn().getCode())
                    .boughtAt(LocalDateTime.now())
                    .expiration((long) snackEntity.getDelaySeconds())
                    .build();

            return Optional.of(mongFeedHistoryRepository.save(mongFeedHistoryEntity).getCode());
        }

        return Optional.empty();
    }

    /**
     * 몽 등록
     * @param createMongVo 몽 등록 Vo
     * @return 몽 도메인 객체
     */
    @Override
    @Transactional
    public Optional<Mong> createMongPort(CreateMongVo createMongVo) {

        Optional<MongTypeEntity> mongTypeEntityOptional = mongTypeRepository.findByComnCode(createMongVo.getMongType().getMongTypeCode());

        if (mongTypeEntityOptional.isPresent()) {
            MongEntity mongEntity = mongRepository.save(MongEntity.builder()
                    .accountId(createMongVo.getAccountId())
                    .mongName(createMongVo.getMongName())
                    .sleepAt(createMongVo.getSleepAt())
                    .wakeupAt(createMongVo.getWakeupAt())
                    .payPoint(createMongVo.getPayPoint())
                    .mongType(mongTypeEntityOptional.get())
                    .stateCode(createMongVo.getStateCode())
                    .isSleep(createMongVo.getIsSleep())
                    .maxStatus(createMongVo.getMongType().getMaxStatus())
                    .statusCode(createMongVo.getStatusCode())
                    .weight(createMongVo.getWeight())
                    .poopCount(createMongVo.getPoopCount())
                    .exp(createMongVo.getExp())
                    .strength(createMongVo.getStrength())
                    .satiety(createMongVo.getSatiety())
                    .healthy(createMongVo.getHealthy())
                    .fatigue(createMongVo.getFatigue())
                    .trainingCount(createMongVo.getTrainingCount())
                    .strokeCount(createMongVo.getStrokeCount())
                    .evolutionReward(createMongVo.getEvolutionReward())
                    .evolutionPenalty(createMongVo.getEvolutionPenalty())
                    .randomDrawTicketCount(createMongVo.getRandomDrawTicketCount())
                    .build());

            return Optional.of(mongEntity.toDomain());
        }

        return Optional.empty();
    }

    /**
     * 몽 조회
     * @param mongId 몽 ID
     * @return 몽 도메인 객체
     */
    @Override
    @Transactional
    public Optional<Mong> getMongPort(Long mongId) {
        return mongRepository.findByMongIdWithLock(mongId).map(MongEntity::toDomain).or(Optional::empty);
    }

    /**
     * 몽 동기화
     * @param mong 몽 도메인 객체
     * @return 몽 도메인 객체
     */
    @Override
    @Transactional
    public Optional<Mong> saveMongPort(Mong mong) {

        Optional<MongEntity> mongEntityOptional = mongRepository.findByMongIdWithLock(mong.getMongId());

        if (mongEntityOptional.isPresent()) {
            MongEntity mongEntity = mongEntityOptional.get();

            if (mong.getMongTypeCode().equals(mongEntity.getMongType().getComn().getCode())) {
                Optional<MongTypeEntity> mongTypeEntityOptional = mongTypeRepository.findByComnCode(mong.getMongTypeCode());

                if (mongTypeEntityOptional.isEmpty()) {
                    return Optional.empty();
                }

                mongEntity.update(mong, mongTypeEntityOptional.get());
            } else {
                mongEntity.update(mong);
            }

            return Optional.of(mongRepository.save(mongEntity).toDomain());
        }

        return Optional.empty();
    }

    /**
     * 몽 삭제
     * @param mong 몽 도메인 객체
     * @return 몽 도메인 객체
     */
    @Override
    @Transactional
    public Optional<Mong> deleteMongPort(Mong mong) {

        Optional<MongEntity> mongEntityOptional = mongRepository.findByMongIdWithLock(mong.getMongId());

        if (mongEntityOptional.isPresent()) {

            mongRepository.deleteById(mong.getMongId());

            return Optional.of(mongEntityOptional.get().toDomain());
        }

        return Optional.empty();
    }

    /**
     * 인벤 아이템 등록
     * @param createInventoryVo 인벤토리 아이템 등록 Vo
     * @return 인벤 아이템 도메인 객체
     */
    @Override
    @Transactional
    public Optional<Inventory> createInventoryItemPort(CreateInventoryVo createInventoryVo) {

        Optional<ComnCodeEntity> comnCodeEntityOptional = comnCodeRepository.findById(createInventoryVo.getTypeCode());

        if (comnCodeEntityOptional.isPresent()) {
            ComnCodeEntity comnCodeEntity = comnCodeEntityOptional.get();

            InventoryEntity inventoryEntity = InventoryEntity.builder()
                    .mongId(createInventoryVo.getMongId())
                    .comn(comnCodeEntity)
                    .inventoryTypeCode(createInventoryVo.getInventoryTypeCode())
                    .build();

            return Optional.of(inventoryItemRepository.save(inventoryEntity).toDomain());
        }

        return Optional.empty();
    }

    /**
     * 인벤 아이템 삭제
     * @param inventoryItemId 인벤토리 아이템 ID
     * @return 인벤 아이템 도메인 객체
     */
    @Override
    @Transactional
    public Optional<Inventory> deleteInventoryItemPort(Long inventoryItemId) {

        Optional<InventoryEntity> inventoryItemEntityOptional = inventoryItemRepository.findByIdWithLock(inventoryItemId);

        if (inventoryItemEntityOptional.isPresent()) {

            inventoryItemRepository.deleteById(inventoryItemId);

            return Optional.of(inventoryItemEntityOptional.get().toDomain());
        }

        return Optional.empty();
    }

    /**
     * 인벤 아이템 조회
     * @param inventoryItemId 인벤토리 아이템 ID
     * @return 인벤 아이템 도메인 객체
     */
    @Override
    @Transactional
    public Optional<Inventory> getInventoryItemPort(Long inventoryItemId) {
        return inventoryItemRepository.findByIdWithLock(inventoryItemId)
                .map(InventoryEntity::toDomain)
                .or(Optional::empty);
    }
}

