package com.mongs.play.app.management.external.service;

import com.mongs.play.app.management.external.vo.*;
import com.mongs.play.core.error.app.ManagementExternalErrorCode;
import com.mongs.play.core.exception.app.ManagementExternalException;
import com.mongs.play.domain.mong.entity.Mong;
import com.mongs.play.domain.mong.enums.MongGrade;
import com.mongs.play.domain.mong.enums.MongTrainingCode;
import com.mongs.play.domain.mong.service.MongService;
import com.mongs.play.domain.mong.utils.MongUtil;
import com.mongs.play.domain.mong.vo.MongFeedLogVo;
import com.mongs.play.domain.mong.vo.MongStatusPercentVo;
import com.mongs.play.domain.mong.vo.MongVo;
import com.mongs.play.module.kafka.event.managementExternal.DeleteMongEvent;
import com.mongs.play.module.kafka.event.managementExternal.EvolutionMongEvent;
import com.mongs.play.module.kafka.event.managementExternal.RegisterMongEvent;
import com.mongs.play.module.kafka.event.managementExternal.SleepingMongEvent;
import com.mongs.play.module.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
                            .weight(mong.getWeight())
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

        kafkaService.sendCommit(KafkaService.KafkaTopic.REGISTER_MONG, RegisterMongEvent.builder()
                .mongId(mong.getId())
                .build());

        return RegisterMongVo.builder()
                .mongId(mong.getId())
                .name(mong.getName())
                .mongCode(mong.getMongCode())
                .weight(mong.getWeight())
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

        kafkaService.sendCommit(KafkaService.KafkaTopic.DELETE_MONG, DeleteMongEvent.builder()
                .mongId(mong.getId())
                .build());

        return DeleteMongVo.builder()
                .mongId(mong.getId())
                .shift(mong.getShift())
                .build();
    }

    @Transactional
    public StrokeMongVo strokeMong(Long accountId, Long mongId) {

        Mong mong = mongService.strokeMong(mongId, 1).mong();

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

        Mong mong = mongService.sleepingMong(mongId).mong();

        if (!accountId.equals(mong.getAccountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }

        if (mong.getIsSleeping()) {
            kafkaService.sendCommit(KafkaService.KafkaTopic.SLEEP_SLEEPING_MONG, SleepingMongEvent.builder()
                    .mongId(mong.getId())
                    .build());
        } else {
            kafkaService.sendCommit(KafkaService.KafkaTopic.AWAKE_SLEEPING_MONG, SleepingMongEvent.builder()
                    .mongId(mong.getId())
                    .build());
        }

        return SleepingMongVo.builder()
                .mongId(mong.getId())
                .isSleeping(mong.getIsSleeping())
                .build();
    }

    @Transactional
    public PoopCleanMongVo poopClean(Long accountId, Long mongId) {

        Mong mong = mongService.poopCleanMong(mongId).mong();

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
                .build();
    }

    @Transactional
    public TrainingMongVo trainingMong(Long accountId, Long mongId, String trainingCode) {

        MongTrainingCode mongTrainingCode = MongTrainingCode.findMongTrainingCode(trainingCode);

        Mong mong = mongService.trainingMong(mongId, 1, mongTrainingCode).mong();

        if (!accountId.equals(mong.getAccountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }

        MongStatusPercentVo mongStatusPercentVo = MongUtil.statusToPercent(mong.getGrade(), mong);

        return TrainingMongVo.builder()
                .mongId(mong.getId())
                .weight(mong.getWeight())
                .strength(mongStatusPercentVo.strength())
                .satiety(mongStatusPercentVo.satiety())
                .healthy(mongStatusPercentVo.healthy())
                .sleep(mongStatusPercentVo.sleep())
                .exp(mongStatusPercentVo.exp())
                .payPoint(mong.getPayPoint())
                .build();
    }

    @Transactional
    public GraduateMongVo graduateMong(Long accountId, Long mongId) {

        Mong mong = mongService.graduateMong(mongId).mong();

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

        MongVo mongVo = mongService.evolutionMong(mongId);
        Mong pastMong = mongVo.pastMong();
        Mong mong = mongVo.mong();

        if (!accountId.equals(mong.getAccountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }

        MongStatusPercentVo mongStatusPercentVo = MongUtil.statusToPercent(mong.getGrade(), mong);

        EvolutionMongEvent evolutionMongEvent = EvolutionMongEvent.builder()
                .mongId(mong.getId())
                .pastMongCode(pastMong.getMongCode())
                .build();

        if (MongGrade.ZERO.equals(mong.getGrade())) {
            kafkaService.sendCommit(KafkaService.KafkaTopic.FIRST_EVOLUTION_MONG, evolutionMongEvent);
        } else if (MongGrade.LAST.equals(mong.getGrade())) {
            kafkaService.sendCommit(KafkaService.KafkaTopic.LAST_EVOLUTION_MONG, evolutionMongEvent);
        } else {
            kafkaService.sendCommit(KafkaService.KafkaTopic.EVOLUTION_MONG, evolutionMongEvent);
        }

        return EvolutionMongVo.builder()
                .mongId(mong.getId())
                .mongCode(mong.getMongCode())
                .weight(mong.getWeight())
                .strength(mongStatusPercentVo.strength())
                .satiety(mongStatusPercentVo.satiety())
                .healthy(mongStatusPercentVo.healthy())
                .sleep(mongStatusPercentVo.sleep())
                .exp(mongStatusPercentVo.exp())
                .shift(mong.getShift())
                .state(mong.getState())
                .build();
    }

    @Transactional
    public FeedMongVo feedMong(Long accountId, Long mongId, String foodCode) {

        Mong mong =  mongService.feedMong(mongId, foodCode).mong();

        if (!accountId.equals(mong.getAccountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }

        MongStatusPercentVo mongStatusPercentVo =
                MongUtil.statusToPercent(mong.getGrade(), mong);

        return FeedMongVo.builder()
                .mongId(mong.getId())
                .weight(mong.getWeight())
                .strength(mongStatusPercentVo.strength())
                .satiety(mongStatusPercentVo.satiety())
                .healthy(mongStatusPercentVo.healthy())
                .sleep(mongStatusPercentVo.sleep())
                .exp(mongStatusPercentVo.exp())
                .payPoint(mong.getPayPoint())
                .build();
    }

    @Transactional(readOnly = true)
    public List<FindFeedLogVo> findFeedLog(Long accountId, Long mongId) {

        Mong mong = mongService.findMongVoByMongId(mongId).mong();

        if (!accountId.equals(mong.getAccountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }

        List<MongFeedLogVo> mongFeedLogVoList = mongService.findMongFeedLogByMongId(mongId);

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
}
