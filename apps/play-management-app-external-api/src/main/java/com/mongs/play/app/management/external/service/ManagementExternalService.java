package com.mongs.play.app.management.external.service;

import com.mongs.play.app.management.external.vo.*;
import com.mongs.play.core.error.app.ManagementExternalErrorCode;
import com.mongs.play.core.exception.app.ManagementExternalException;
import com.mongs.play.domain.mong.entity.Mong;
import com.mongs.play.domain.mong.enums.MongTrainingCode;
import com.mongs.play.domain.mong.service.MongService;
import com.mongs.play.domain.mong.utils.MongUtil;
import com.mongs.play.domain.mong.vo.MongFeedLogVo;
import com.mongs.play.domain.mong.vo.MongStatusPercentVo;
import com.mongs.play.module.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementExternalService {

    private final MongService mongService;
    private final KafkaService kafkaService;

    @Transactional(readOnly = true)
    public List<FindMongVo> findMong(Long accountId) {

        List<Mong> mongList = mongService.findMongByAccountId(accountId);

        return mongList.stream()
                .map(mong -> {
                    MongStatusPercentVo mongStatusPercentVo =
                            MongUtil.statusToPercent(mong.getGrade(), mong);

                    return FindMongVo.builder()
                            .accountId(mong.getAccountId())
                            .mongId(mong.getId())
                            .name(mong.getName())
                            .mongCode(mong.getMongCode())
                            .weight(mongStatusPercentVo.weight())
                            .strength(mongStatusPercentVo.strength())
                            .satiety(mongStatusPercentVo.satiety())
                            .healthy(mongStatusPercentVo.healthy())
                            .sleep(mongStatusPercentVo.sleep())
                            .poopCount(mong.getPoopCount())
                            .isSleeping(mong.getIsSleeping())
                            .exp(mongStatusPercentVo.exp())
                            .state(mong.getState())
                            .shift(mong.getShift())
                            .payPoint(mong.getPayPoint())
                            .born(mong.getCreatedAt())
                            .build();
                })
                .toList();
    }

    @Transactional
    public RegisterMongVo registerMong(Long accountId, String name, String sleepStart, String sleepEnd) {

        Mong mong = mongService.addMong(accountId, name, sleepStart, sleepEnd);

        MongStatusPercentVo mongStatusPercentVo =
                MongUtil.statusToPercent(mong.getGrade(), mong);

        return RegisterMongVo.builder()
                .mongId(mong.getId())
                .name(mong.getName())
                .mongCode(mong.getMongCode())
                .weight(mongStatusPercentVo.weight())
                .strength(mongStatusPercentVo.strength())
                .satiety(mongStatusPercentVo.satiety())
                .healthy(mongStatusPercentVo.healthy())
                .sleep(mongStatusPercentVo.sleep())
                .exp(mongStatusPercentVo.exp())
                .poopCount(mong.getPoopCount())
                .isSleeping(mong.getIsSleeping())
                .state(mong.getState())
                .shift(mong.getShift())
                .payPoint(mong.getPayPoint())
                .born(mong.getCreatedAt())
                .build();
    }

    @Transactional
    public DeleteMongVo deleteMong(Long accountId, Long mongId) {

        Mong mong = mongService.removeMong(mongId);

        if (!accountId.equals(mong.getAccountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }

        return DeleteMongVo.builder()
                .mongId(mong.getId())
                .shift(mong.getShift())
                .build();
    }

    @Transactional
    public StrokeMongVo strokeMong(Long accountId, Long mongId) {

        Mong mong = mongService.strokeMong(mongId, 1);

        if (!accountId.equals(mong.getAccountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }

        double exp = MongUtil.statusToPercent(mong.getGrade().evolutionExp, mong.getExp());

        return StrokeMongVo.builder()
                .mongId(mong.getId())
                .exp(exp)
                .build();
    }

    @Transactional
    public SleepingMongVo sleepingMong(Long accountId, Long mongId) {

        Mong mong = mongService.sleepingMong(mongId);

        if (!accountId.equals(mong.getAccountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }

        return SleepingMongVo.builder()
                .mongId(mong.getId())
                .isSleeping(mong.getIsSleeping())
                .build();
    }

    @Transactional
    public PoopCleanMongVo poopClean(Long accountId, Long mongId) {

        Mong mong = mongService.poopCleanMong(mongId);

        if (!accountId.equals(mong.getAccountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }

        double exp = MongUtil.statusToPercent(mong.getGrade().evolutionExp, mong.getExp());

        return PoopCleanMongVo.builder()
                .mongId(mong.getId())
                .poopCount(mong.getPoopCount())
                .exp(exp)
                .build();
    }

    @Transactional(readOnly = true)
    public ValidationTrainingMongVo validationTrainingMong(Long mongId, String trainingCode) {

        MongTrainingCode mongTrainingCode = MongTrainingCode.findMongTrainingCode(trainingCode);

        boolean isPossible = mongService.validationTrainingMong(mongId, mongTrainingCode);

        return ValidationTrainingMongVo.builder()
                .mongId(mongId)
                .isPossible(isPossible)
                .trainingId(UUID.randomUUID().toString().replace("-", ""))
                .build();
    }

    @Transactional
    public TrainingMongVo trainingMong(Long accountId, Long mongId, String trainingId, String trainingCode) {

        MongTrainingCode mongTrainingCode = MongTrainingCode.findMongTrainingCode(trainingCode);

        // TODO(트레이닝 아이디 유효성 체크: validationTrainingMong() 에서 생성한 trainingId 가 맞는지 확인)
//        if () {
//            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_TRAINING_ID);
//        }

        Mong mong = mongService.trainingMong(mongId, 1, mongTrainingCode);

        if (!accountId.equals(mong.getAccountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }

        double strength = MongUtil.statusToPercent(mong.getGrade().maxStatus, mong.getStrength());
        double exp = MongUtil.statusToPercent(mong.getGrade().evolutionExp, mong.getExp());

        return TrainingMongVo.builder()
                .mongId(mong.getId())
                .strength(strength)
                .exp(exp)
                .payPoint(mong.getPayPoint())
                .build();
    }

    @Transactional
    public GraduateMongVo graduateMong(Long accountId, Long mongId) {

        Mong mong = mongService.graduateMong(mongId);

        if (!accountId.equals(mong.getAccountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }

        return GraduateMongVo.builder()
                .mongId(mong.getId())
                .shift(mong.getShift())
                .build();
    }

    @Transactional
    public EvolutionMongVo evolutionMong(Long accountId, Long mongId) {

        Mong pastMong = mongService.findMongByMongId(mongId);

        Mong mong = mongService.evolutionMong(mongId);

        if (!accountId.equals(mong.getAccountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }

        MongStatusPercentVo mongStatusPercentVo =
                MongUtil.statusToPercent(mong.getGrade(), mong);

//        kafkaService.sendCommit(KafkaTopic.EVOLUTION_MONG, EvolutionMongEvent.builder()
//                        .accountId(mong.getAccountId())
//                        .mongCode(mong.getMongCode())
//                        .build());

        return EvolutionMongVo.builder()
                .mongId(mong.getId())
                .mongCode(mong.getMongCode())
                .weight(mongStatusPercentVo.weight())
                .strength(mongStatusPercentVo.strength())
                .satiety(mongStatusPercentVo.satiety())
                .healthy(mongStatusPercentVo.healthy())
                .sleep(mongStatusPercentVo.sleep())
                .exp(mongStatusPercentVo.exp())
                .shift(mong.getShift())
                .state(mong.getState())
                .build();
    }

    @Transactional(readOnly = true)
    public List<FindFeedLogVo> findFeedLog(Long accountId, Long mongId) {

        Mong mong = mongService.findMongByMongId(mongId);

        if (!accountId.equals(mong.getAccountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }

        List<MongFeedLogVo> mongFeedLogVoList = mongService.findMongFeedLog(mongId);

        return mongFeedLogVoList.stream()
                .map(mongFeedLogVo -> FindFeedLogVo.builder()
                        .mongId(mongId)
                        .code(mongFeedLogVo.code())
                        .isCanBuy(mongFeedLogVo.lastBuyAt()
                                .plusSeconds(mongFeedLogVo.delaySeconds())
                                .isBefore(LocalDateTime.now()))
                        .build())
                .toList();
    }

    @Transactional
    public FeedMongVo feedMong(Long accountId, Long mongId, String foodCode) {

        Mong mong = mongService.feedMong(mongId, foodCode);

        if (!accountId.equals(mong.getAccountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }

        MongStatusPercentVo mongStatusPercentVo =
                MongUtil.statusToPercent(mong.getGrade(), mong);

        return FeedMongVo.builder()
                .mongId(mong.getId())
                .weight(mongStatusPercentVo.weight())
                .strength(mongStatusPercentVo.strength())
                .satiety(mongStatusPercentVo.satiety())
                .healthy(mongStatusPercentVo.healthy())
                .sleep(mongStatusPercentVo.sleep())
                .exp(mongStatusPercentVo.exp())
                .payPoint(mong.getPayPoint())
                .build();
    }
}
