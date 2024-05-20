package com.mongs.play.app.management.external.service;

import com.mongs.play.app.management.external.dto.res.EvolutionReadyResDto;
import com.mongs.play.app.management.external.vo.*;
import com.mongs.play.client.publisher.mong.code.PublishCode;
import com.mongs.play.client.publisher.mong.service.MqttService;
import com.mongs.play.core.error.app.ManagementExternalErrorCode;
import com.mongs.play.core.error.domain.MongErrorCode;
import com.mongs.play.core.exception.app.ManagementExternalException;
import com.mongs.play.core.exception.common.InvalidException;
import com.mongs.play.domain.code.entity.FoodCode;
import com.mongs.play.domain.code.entity.MongCode;
import com.mongs.play.domain.code.service.CodeService;
import com.mongs.play.domain.mong.enums.MongGrade;
import com.mongs.play.domain.mong.enums.MongShift;
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
import com.mongs.play.module.kafka.event.managementInternal.EvolutionReadyMongEvent;
import com.mongs.play.module.kafka.event.managementInternal.FeedMongEvent;
import com.mongs.play.module.kafka.event.managementInternal.TrainingMongEvent;
import com.mongs.play.module.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementExternalService {
    private final Random random = new Random();

    private final CodeService codeService;
    private final MongService mongService;
    private final KafkaService kafkaService;
    private final MqttService mqttService;

    private MongVo evolutionReady(MongVo mongVo) {

        if (MongUtil.isEvolutionReady(mongVo.grade(), mongVo.shift(), mongVo.exp())) {
            mongVo = mongService.toggleEvolutionReady(mongVo.mongId());

            kafkaService.sendCommit(KafkaService.CommitTopic.EVOLUTION_READY, EvolutionReadyMongEvent.builder()
                    .mongId(mongVo.mongId())
                    .build());

            mqttService.send(PublishCode.EVOLUTION_READY, mongVo.accountId(), EvolutionReadyResDto.builder()
                    .accountId(mongVo.accountId())
                    .mongId(mongVo.mongId())
                    .shiftCode(mongVo.shift().code)
                    .build());
        }

        return mongVo;
    }

    @Transactional(readOnly = true)
    public List<FindMongVo> findMong(Long accountId) {

        List<MongVo> mongList = mongService.findMongByAccountId(accountId);

        return mongList.stream()
                .map(mong -> {
                    MongStatusPercentVo mongStatusPercentVo =
                            MongUtil.statusToPercent(mong.grade(), mong);

                    return FindMongVo.builder()
                            .accountId(mong.accountId())
                            .mongId(mong.mongId())
                            .name(mong.name())
                            .mongCode(mong.mongCode())
                            .weight(mong.weight())
                            .strength(mongStatusPercentVo.strength())
                            .satiety(mongStatusPercentVo.satiety())
                            .healthy(mongStatusPercentVo.healthy())
                            .sleep(mongStatusPercentVo.sleep())
                            .poopCount(mong.poopCount())
                            .isSleeping(mong.isSleeping())
                            .exp(mongStatusPercentVo.exp())
                            .state(mong.state())
                            .shift(mong.shift())
                            .payPoint(mong.payPoint())
                            .born(mong.born())
                            .build();
                })
                .toList();
    }

    @Transactional
    public RegisterMongVo registerMong(Long accountId, String name, String sleepStart, String sleepEnd) {

        List<MongCode> mongCodeList = codeService.getMongCodeByLevel(MongGrade.ZERO.level);
        int randIdx = random.nextInt(mongCodeList.size());

        String eggMongCode = mongCodeList.get(randIdx).code();

        MongVo newMongVo = mongService.addMong(accountId, eggMongCode, name, sleepStart, sleepEnd);
        MongStatusPercentVo mongStatusPercentVo = MongUtil.statusToPercent(newMongVo.grade(), newMongVo);

        kafkaService.sendCommit(KafkaService.CommitTopic.REGISTER_MONG, RegisterMongEvent.builder()
                .mongId(newMongVo.mongId())
                .accountId(newMongVo.accountId())
                .mongCode(newMongVo.mongCode())
                .build());

        return RegisterMongVo.builder()
                .accountId(newMongVo.accountId())
                .mongId(newMongVo.mongId())
                .name(newMongVo.name())
                .mongCode(newMongVo.mongCode())
                .weight(newMongVo.weight())
                .strength(mongStatusPercentVo.strength())
                .satiety(mongStatusPercentVo.satiety())
                .healthy(mongStatusPercentVo.healthy())
                .sleep(mongStatusPercentVo.sleep())
                .exp(mongStatusPercentVo.exp())
                .poopCount(newMongVo.poopCount())
                .isSleeping(newMongVo.isSleeping())
                .state(newMongVo.state())
                .shift(newMongVo.shift())
                .payPoint(newMongVo.payPoint())
                .born(newMongVo.born())
                .build();
    }

    @Transactional
    public DeleteMongVo deleteMong(Long accountId, Long mongId) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        if (!accountId.equals(mongVo.accountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }

        MongVo newMongVo = mongService.removeMong(mongVo.mongId());

        kafkaService.sendCommit(KafkaService.CommitTopic.DELETE_MONG, DeleteMongEvent.builder()
                .mongId(newMongVo.mongId())
                .build());

        return DeleteMongVo.builder()
                .accountId(newMongVo.accountId())
                .mongId(newMongVo.mongId())
                .shift(newMongVo.shift())
                .build();
    }

    @Transactional
    public StrokeMongVo strokeMong(Long accountId, Long mongId) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        if (!accountId.equals(mongVo.accountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }
        if (MongGrade.EMPTY.equals(mongVo.grade())) {
            throw new ManagementExternalException(MongErrorCode.INVALID_STROKE);
        }
        if (MongGrade.ZERO.equals(mongVo.grade())) {
            throw new ManagementExternalException(MongErrorCode.INVALID_STROKE);
        }
        if (MongGrade.LAST.equals(mongVo.grade())) {
            throw new ManagementExternalException(MongErrorCode.INVALID_STROKE);
        }

        MongVo newMongVo = mongService.increaseNumberOfStroke(mongVo.mongId(), 1);
        newMongVo = this.evolutionReady(newMongVo);

        double exp = MongUtil.statusToPercent(newMongVo.grade().evolutionExp, newMongVo.exp());

        return StrokeMongVo.builder()
                .accountId(newMongVo.accountId())
                .mongId(newMongVo.mongId())
                .exp(exp)
                .build();
    }

    @Transactional
    public SleepingMongVo sleepingMong(Long accountId, Long mongId) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        if (!accountId.equals(mongVo.accountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }
        if (MongGrade.EMPTY.equals(mongVo.grade())) {
            throw new ManagementExternalException(mongVo.isSleeping() ? MongErrorCode.INVALID_AWAKE : MongErrorCode.INVALID_SLEEPING);
        }
        if (MongGrade.ZERO.equals(mongVo.grade())) {
            throw new ManagementExternalException(mongVo.isSleeping() ? MongErrorCode.INVALID_AWAKE : MongErrorCode.INVALID_SLEEPING);
        }
        if (MongGrade.LAST.equals(mongVo.grade())) {
            throw new ManagementExternalException(mongVo.isSleeping() ? MongErrorCode.INVALID_AWAKE : MongErrorCode.INVALID_SLEEPING);
        }

        MongVo newMongVo = mongService.toggleIsSleeping(mongVo.mongId());

        if (newMongVo.isSleeping()) {
            kafkaService.sendCommit(KafkaService.CommitTopic.SLEEP_SLEEPING_MONG, SleepingMongEvent.builder()
                    .mongId(newMongVo.mongId())
                    .build());
        } else {
            kafkaService.sendCommit(KafkaService.CommitTopic.AWAKE_SLEEPING_MONG, SleepingMongEvent.builder()
                    .mongId(newMongVo.mongId())
                    .build());
        }

        return SleepingMongVo.builder()
                .accountId(newMongVo.accountId())
                .mongId(newMongVo.mongId())
                .isSleeping(newMongVo.isSleeping())
                .build();
    }

    @Transactional
    public PoopCleanMongVo poopClean(Long accountId, Long mongId) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        if (!accountId.equals(mongVo.accountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }
        if (MongGrade.EMPTY.equals(mongVo.grade())) {
            throw new ManagementExternalException(MongErrorCode.INVALID_POOP_CLEAN);
        }
        if (MongGrade.ZERO.equals(mongVo.grade())) {
            throw new ManagementExternalException(MongErrorCode.INVALID_POOP_CLEAN);
        }
        if (MongGrade.LAST.equals(mongVo.grade())) {
            throw new ManagementExternalException(MongErrorCode.INVALID_POOP_CLEAN);
        }

        MongVo newMongVo = mongService.clearPoopCount(mongVo.mongId());
        newMongVo = this.evolutionReady(newMongVo);

        double exp = MongUtil.statusToPercent(newMongVo.grade().evolutionExp, newMongVo.exp());

        return PoopCleanMongVo.builder()
                .accountId(newMongVo.accountId())
                .mongId(newMongVo.mongId())
                .poopCount(newMongVo.poopCount())
                .exp(exp)
                .build();
    }

    @Transactional
    public ValidationTrainingMongVo validationTrainingMong(Long mongId, String trainingCode) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        if (MongGrade.EMPTY.equals(mongVo.grade())) {
            throw new ManagementExternalException(MongErrorCode.INVALID_TRAINING);
        }
        if (MongGrade.ZERO.equals(mongVo.grade())) {
            throw new ManagementExternalException(MongErrorCode.INVALID_TRAINING);
        }

        MongTrainingCode mongTrainingCode = MongTrainingCode.findMongTrainingCode(trainingCode);

        boolean isPossible = mongVo.payPoint() >= mongTrainingCode.point;

        return ValidationTrainingMongVo.builder()
                .accountId(mongVo.accountId())
                .mongId(mongVo.mongId())
                .isPossible(isPossible)
                .build();
    }

    @Transactional
    public TrainingMongVo trainingMong(Long accountId, Long mongId, String trainingCode) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        MongTrainingCode mongTrainingCode = MongTrainingCode.findMongTrainingCode(trainingCode);

        if (!accountId.equals(mongVo.accountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }
        if (mongVo.payPoint() < mongTrainingCode.point) {
            throw new ManagementExternalException(MongErrorCode.NOT_ENOUGH_PAY_POINT);
        }
        if (MongGrade.EMPTY.equals(mongVo.grade())) {
            throw new ManagementExternalException(MongErrorCode.INVALID_TRAINING);
        }
        if (MongGrade.ZERO.equals(mongVo.grade())) {
            throw new ManagementExternalException(MongErrorCode.INVALID_TRAINING);
        }

        MongVo newMongVo = mongService.increaseStatusTraining(mongVo.mongId(), 1, mongTrainingCode);
        newMongVo = this.evolutionReady(newMongVo);

        kafkaService.sendCommit(KafkaService.CommitTopic.TRAINING_MONG, TrainingMongEvent.builder()
                .mongId(newMongVo.mongId())
                .weight(newMongVo.weight())
                .strength(newMongVo.strength())
                .satiety(newMongVo.satiety())
                .healthy(newMongVo.healthy())
                .sleep(newMongVo.sleep())
                .build());

        MongStatusPercentVo mongStatusPercentVo = MongUtil.statusToPercent(mongVo.grade(), newMongVo);

        return TrainingMongVo.builder()
                .accountId(newMongVo.accountId())
                .mongId(newMongVo.mongId())
                .weight(newMongVo.weight())
                .strength(mongStatusPercentVo.strength())
                .satiety(mongStatusPercentVo.satiety())
                .healthy(mongStatusPercentVo.healthy())
                .sleep(mongStatusPercentVo.sleep())
                .exp(mongStatusPercentVo.exp())
                .payPoint(newMongVo.payPoint())
                .build();
    }

    @Transactional
    public GraduateMongVo graduateMong(Long accountId, Long mongId) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        if (!accountId.equals(mongVo.accountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }
        if (!MongGrade.LAST.equals(mongVo.grade())) {
            throw new ManagementExternalException(MongErrorCode.INVALID_GRADUATION);
        }
        if (!MongShift.GRADUATE_READY.equals(mongVo.shift())) {
            throw new ManagementExternalException(MongErrorCode.INVALID_GRADUATION);
        }

        MongVo newMongVo = mongService.toggleGraduate(mongVo.mongId());

        return GraduateMongVo.builder()
                .accountId(newMongVo.accountId())
                .mongId(newMongVo.mongId())
                .shift(newMongVo.shift())
                .build();
    }

    @Transactional
    public EvolutionMongVo evolutionMong(Long accountId, Long mongId) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        if (!accountId.equals(mongVo.accountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }
        if (MongGrade.LAST.equals(mongVo.grade())) {
            throw new ManagementExternalException(MongErrorCode.INVALID_EVOLUTION);
        }
        if (!MongShift.EVOLUTION_READY.equals(mongVo.shift())) {
            throw new ManagementExternalException(MongErrorCode.INVALID_EVOLUTION);
        }
        if (mongVo.isSleeping()) {
            throw new ManagementExternalException(MongErrorCode.INVALID_EVOLUTION);
        }
        if (mongVo.exp() < mongVo.grade().evolutionExp) {
            throw new ManagementExternalException(MongErrorCode.NOT_ENOUGH_EXP);
        }

        MongVo newMongVo;
        if (MongGrade.ZERO.equals(mongVo.grade())) {

            List<MongCode> mongCodeList = codeService.getMongCodeByLevel(MongGrade.ZERO.nextGrade.level);
            int randIdx = random.nextInt(mongCodeList.size());
            String mongCode = mongCodeList.get(randIdx).code();

            newMongVo = mongService.toggleFirstEvolution(mongVo.mongId(), mongCode);

            kafkaService.sendCommit(KafkaService.CommitTopic.FIRST_EVOLUTION_MONG, EvolutionMongEvent.builder()
                    .accountId(newMongVo.accountId())
                    .mongId(newMongVo.mongId())
                    .pastMongCode(mongVo.mongCode())
                    .mongCode(newMongVo.mongCode())
                    .build());

        } else if (MongGrade.LAST.equals(mongVo.grade().nextGrade)) {

            newMongVo = mongService.toggleLastEvolution(mongVo.mongId());

            kafkaService.sendCommit(KafkaService.CommitTopic.LAST_EVOLUTION_MONG, EvolutionMongEvent.builder()
                    .accountId(newMongVo.accountId())
                    .mongId(newMongVo.mongId())
                    .pastMongCode(mongVo.mongCode())
                    .mongCode(newMongVo.mongCode())
                    .build());

        } else {
            // TODO("진화 포인트 환산")
            int evolutionPoint = 0;

            List<MongCode> mongCodeList = codeService.getMongCodeByLevelAndEvolutionPoint(mongVo.grade().nextGrade.level, evolutionPoint);

            // TODO("컬렉션 목록을 조회하여 겹치지 않도록 하는 로직 필요")

            String mongCode = mongCodeList.get(mongCodeList.size() - 1).code();

            newMongVo = mongService.toggleEvolution(mongVo.mongId(), mongCode);

            kafkaService.sendCommit(KafkaService.CommitTopic.EVOLUTION_MONG, EvolutionMongEvent.builder()
                    .accountId(newMongVo.accountId())
                    .mongId(newMongVo.mongId())
                    .pastMongCode(mongVo.mongCode())
                    .mongCode(newMongVo.mongCode())
                    .build());
        }

        MongStatusPercentVo mongStatusPercentVo = MongUtil.statusToPercent(newMongVo.grade(), newMongVo);

        return EvolutionMongVo.builder()
                .accountId(newMongVo.accountId())
                .mongId(newMongVo.mongId())
                .mongCode(newMongVo.mongCode())
                .weight(newMongVo.weight())
                .strength(mongStatusPercentVo.strength())
                .satiety(mongStatusPercentVo.satiety())
                .healthy(mongStatusPercentVo.healthy())
                .sleep(mongStatusPercentVo.sleep())
                .exp(mongStatusPercentVo.exp())
                .shift(newMongVo.shift())
                .state(newMongVo.state())
                .build();
    }

    @Transactional
    public FeedMongVo feedMong(Long accountId, Long mongId, String foodCode) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        FoodCode food = codeService.getFoodCode(foodCode);

        if (!accountId.equals(mongVo.accountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }
        if (mongVo.payPoint() < food.price()) {
            throw new InvalidException(MongErrorCode.NOT_ENOUGH_PAY_POINT);
        }
        if (MongGrade.EMPTY.equals(mongVo.grade())) {
            throw new ManagementExternalException(MongErrorCode.INVALID_FEED);
        }
        if (MongGrade.ZERO.equals(mongVo.grade())) {
            throw new ManagementExternalException(MongErrorCode.INVALID_FEED);
        }

        MongVo newMongVo = mongService.feedMong(mongVo.mongId(), food.code(), food.addWeightValue(), food.addStrengthValue(), food.addSatietyValue(), food.addHealthyValue(), food.addSleepValue(), food.price());
        newMongVo = this.evolutionReady(newMongVo);

        kafkaService.sendCommit(KafkaService.CommitTopic.FEED_MONG, FeedMongEvent.builder()
                .mongId(newMongVo.mongId())
                .weight(newMongVo.weight())
                .strength(newMongVo.strength())
                .satiety(newMongVo.satiety())
                .healthy(newMongVo.healthy())
                .sleep(newMongVo.sleep())
                .build());

        MongStatusPercentVo mongStatusPercentVo = MongUtil.statusToPercent(newMongVo.grade(), newMongVo);

        return FeedMongVo.builder()
                .accountId(newMongVo.accountId())
                .mongId(newMongVo.mongId())
                .weight(newMongVo.weight())
                .strength(mongStatusPercentVo.strength())
                .satiety(mongStatusPercentVo.satiety())
                .healthy(mongStatusPercentVo.healthy())
                .sleep(mongStatusPercentVo.sleep())
                .exp(mongStatusPercentVo.exp())
                .payPoint(newMongVo.payPoint())
                .build();
    }

    @Transactional
    public List<FindFeedLogVo> findFeedLog(Long accountId, Long mongId) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        if (!accountId.equals(mongVo.accountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_CHANGE_MONG);
        }

        List<MongFeedLogVo> mongFeedLogVoList = mongService.findMongFeedLogByMongId(mongVo.mongId());

        Map<String, MongFeedLogVo> mongFeedLogVoMap = mongFeedLogVoList.stream()
                .collect(Collectors.toMap(MongFeedLogVo::code, mongFeedLogVo -> mongFeedLogVo));

        List<FoodCode> foodCodeList = codeService.getFoodCode();

        return foodCodeList.stream()
                .map(foodCode -> {
                    String code = foodCode.code();
                    long id = mongVo.mongId();
                    boolean isCanBuy = true;

                    // 음식 섭취 기록이 있으면
                    if (mongFeedLogVoMap.containsKey(foodCode.code())) {
                        MongFeedLogVo mongFeedLogVo = mongFeedLogVoMap.get(foodCode.code());
                        isCanBuy = mongFeedLogVo.lastBuyAt()
                                .plusSeconds(foodCode.delaySeconds())
                                .isBefore(LocalDateTime.now());
                    }

                    return FindFeedLogVo.builder()
                            .accountId(mongVo.accountId())
                            .code(code)
                            .mongId(id)
                            .isCanBuy(isCanBuy)
                            .build();
                })
                .toList();
    }
}
