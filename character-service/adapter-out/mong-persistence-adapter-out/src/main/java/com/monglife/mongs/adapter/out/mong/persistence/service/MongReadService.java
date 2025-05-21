package com.monglife.mongs.adapter.out.mong.persistence.service;

import com.monglife.mongs.adapter.out.mong.persistence.entity.*;
import com.monglife.mongs.adapter.out.mong.persistence.repository.*;
import com.monglife.mongs.application.mong.port.out.MongReadPort;
import com.monglife.mongs.domain.mong.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MongReadService implements MongReadPort {

    private final MongStrokeHistoryRepository mongStrokeHistoryRepository;

    private final MongFeedHistoryRepository mongFeedHistoryRepository;

    private final FoodTypeRepository foodTypeRepository;

    private final SnackTypeRepository snackTypeRepository;

    private final MongTypeRepository mongTypeRepository;

    private final TrainingTypeRepository trainingTypeRepository;

    private final MongRepository mongRepository;

    private final RandomDrawItemRepository randomDrawItemRepository;

    private final InventoryItemRepository inventoryItemRepository;

    /**
     * 몽 쓰다 듬기 대기 잔여 시간 조회
     * @param mongId 몽 ID
     * @return 쓰다 듬기 대기 잔여 시간 (초)
     */
    @Override
    @Transactional
    public Long getMongStrokeExpirationSecondsPort(Long mongId) {
        return mongStrokeHistoryRepository.findByMongId(mongId)
                .map(mongStrokeHistoryEntity -> mongStrokeHistoryEntity.getExpiration() - Duration.between(mongStrokeHistoryEntity.getStrokeAt(), LocalDateTime.now()).getSeconds())
                .orElse(0L);
    }

    /**
     * 몽 타입 목록 조회
     * @param level 몽 타입 레벨
     * @return 몽 타입 목록
     */
    @Override
    @Transactional
    public List<MongType> getMongTypesPort(Integer level) {
        return mongTypeRepository.findByLevel(level).stream()
                .map(MongTypeEntity::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 다음 레벨 몽 타입 목록 조회
     * @param evolutionScore 현재 진화 점수
     * @param mongTypeCode 현재 몽 타입 코드
     * @return 몽 타입 목록
     */
    @Override
    @Transactional
    public List<MongType> getNextLevelMongTypesPort(Double evolutionScore, String mongTypeCode) {
        return mongTypeRepository.findByEvolutionScoreAndMongTypeCode(evolutionScore, mongTypeCode).stream()
                .map(MongTypeEntity::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 몽 조회
     * @param mongId 몽 ID
     * @return 몽 도메인 객체
     */
    @Override
    @Transactional
    public Optional<Mong> getMongPort(Long mongId) {
        return mongRepository.findByMongId(mongId)
                .map(MongEntity::toDomain)
                .or(Optional::empty);
    }

    /**
     * 몽 목록 조회
     * @param accountId 계정 ID
     * @return 몽 도메인 객체 목록
     */
    @Override
    @Transactional
    public List<Mong> getMongsPort(Long accountId) {
        return mongRepository.findAllByAccountId(accountId).stream()
                .map(MongEntity::toDomain)
                .toList();
    }

    /**
     * 음식 조회
     * @param foodTypeCode 음식 타입 코드
     * @param mongId 몽 ID
     * @return 음식 도메인 객체
     */
    @Override
    @Transactional
    public Optional<Food> getFoodPort(String foodTypeCode, Long mongId) {

        Optional<FoodTypeEntity> foodTypeEntityOptional = foodTypeRepository.findByComnCode(foodTypeCode);

        if (foodTypeEntityOptional.isPresent()) {
            FoodTypeEntity foodTypeEntity = foodTypeEntityOptional.get();
            boolean isCanBuy = mongFeedHistoryRepository.findByMongIdAndTypeCode(mongId, foodTypeCode).isEmpty();

            Food food = Food.builder()
                    .foodTypeCode(foodTypeEntity.getComn().getCode())
                    .foodTypeName(foodTypeEntity.getComn().getName())
                    .price(foodTypeEntity.getPrice())
                    .isCanBuy(isCanBuy)
                    .weight(foodTypeEntity.getWeight())
                    .strength(foodTypeEntity.getStrength())
                    .satiety(foodTypeEntity.getSatiety())
                    .healthy(foodTypeEntity.getHealthy())
                    .fatigue(foodTypeEntity.getFatigue())
                    .build();

            return Optional.of(food);
        }

        return Optional.empty();
    }

    /**
     * 음식 목록 조회
     * @param mongId 몽 ID
     * @return 음식 도메인 객체 목록
     */
    @Override
    @Transactional
    public List<Food> getFoodsPort(Long mongId) {

        List<String> invalidBuyFoodTypeCodes = mongFeedHistoryRepository.findByMongId(mongId).stream()
                .map(MongFeedHistoryEntity::getTypeCode)
                .toList();

        return foodTypeRepository.findAll().stream()
                .map(foodTypeEntity -> Food.builder()
                            .foodTypeCode(foodTypeEntity.getComn().getCode())
                            .foodTypeName(foodTypeEntity.getComn().getName())
                            .price(foodTypeEntity.getPrice())
                            .isCanBuy(!invalidBuyFoodTypeCodes.contains(foodTypeEntity.getComn().getCode()))
                            .weight(foodTypeEntity.getWeight())
                            .strength(foodTypeEntity.getStrength())
                            .satiety(foodTypeEntity.getSatiety())
                            .healthy(foodTypeEntity.getHealthy())
                            .fatigue(foodTypeEntity.getFatigue())
                            .build())
                .toList();
    }

    /**
     * 간식 조회
     * @param snackTypeCode 간식 타입 코드
     * @param mongId 몽 ID
     * @return 간식 도메인 객체
     */
    @Override
    @Transactional
    public Optional<Snack> getSnackPort(String snackTypeCode, Long mongId) {

        Optional<SnackTypeEntity> snackTypeEntityOptional = snackTypeRepository.findByComnCode(snackTypeCode);

        if (snackTypeEntityOptional.isPresent()) {
            SnackTypeEntity snackTypeEntity = snackTypeEntityOptional.get();
            boolean isCanBuy = mongFeedHistoryRepository.findByMongIdAndTypeCode(mongId, snackTypeCode).isEmpty();

            Snack snack = Snack.builder()
                    .snackTypeCode(snackTypeEntity.getComn().getCode())
                    .snackTypeName(snackTypeEntity.getComn().getName())
                    .price(snackTypeEntity.getPrice())
                    .isCanBuy(isCanBuy)
                    .weight(snackTypeEntity.getWeight())
                    .strength(snackTypeEntity.getStrength())
                    .satiety(snackTypeEntity.getSatiety())
                    .healthy(snackTypeEntity.getHealthy())
                    .fatigue(snackTypeEntity.getFatigue())
                    .build();

            return Optional.of(snack);
        }

        return Optional.empty();
    }

    /**
     * 간식 목록 조회
     * @param mongId 몽 ID
     * @return 간식 도메인 객체 목록
     */
    @Override
    @Transactional
    public List<Snack> getSnacksPort(Long mongId) {

        List<String> invalidBuySnackTypeCodes = mongFeedHistoryRepository.findByMongId(mongId).stream()
                .map(MongFeedHistoryEntity::getTypeCode)
                .toList();

        return snackTypeRepository.findAll().stream()
                .map(snackTypeEntity -> Snack.builder()
                        .snackTypeCode(snackTypeEntity.getComn().getCode())
                        .snackTypeName(snackTypeEntity.getComn().getName())
                        .price(snackTypeEntity.getPrice())
                        .isCanBuy(!invalidBuySnackTypeCodes.contains(snackTypeEntity.getComn().getCode()))
                        .weight(snackTypeEntity.getWeight())
                        .strength(snackTypeEntity.getStrength())
                        .satiety(snackTypeEntity.getSatiety())
                        .healthy(snackTypeEntity.getHealthy())
                        .fatigue(snackTypeEntity.getFatigue())
                        .build())
                .toList();
    }

    /**
     * 훈련 타입 목록 조회
     * @return 훈련 타입 도메인 객체 목록
     */
    @Override
    @Transactional
    public List<TrainingType> getTrainingTypesPort() {
        return trainingTypeRepository.findAll().stream()
                .map(TrainingTypeEntity::toDomain)
                .toList();
    }

    /**
     * 훈련 타입 조회
     * @param trainingTypeCode 훈련 타입 코드
     * @return 훈련 타입 도메인 객체
     */
    @Override
    @Transactional
    public Optional<TrainingType> getTrainingTypePort(String trainingTypeCode) {
        return trainingTypeRepository.findByTrainingTypeCode(trainingTypeCode)
                .map(TrainingTypeEntity::toDomain)
                .or(Optional::empty);
    }

    /**
     * 랜덤 뽑기 아이템 목록 조회
     * @return 랜덤 뽑기 아이템 도메인 객체 목록
     */
    @Override
    @Transactional
    public List<RandomDrawItem> getRandomDrawItemsPort() {
        return randomDrawItemRepository.findAll().stream()
                .map(RandomDrawItemEntity::toDomain)
                .toList();
    }

    /**
     * 인벤 아이템 목록 조회
     * @param mongId 몽 ID
     * @return 인벤 아이템 도메인 객체 목록
     */
    @Override
    @Transactional
    public List<InventoryItem> getInventoryItemsPort(Long mongId) {
        return inventoryItemRepository.findByMongId(mongId).stream()
                .map(InventoryItemEntity::toDomain)
                .toList();
    }
}
