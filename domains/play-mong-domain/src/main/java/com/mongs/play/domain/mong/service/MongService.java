package com.mongs.play.domain.mong.service;

import com.mongs.play.core.error.domain.MongErrorCode;
import com.mongs.play.core.exception.common.InvalidException;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.mong.annotation.MongEvolutionValidation;
import com.mongs.play.domain.mong.annotation.MongStateValidation;
import com.mongs.play.domain.mong.annotation.MongStatusValidation;
import com.mongs.play.domain.mong.entity.MongFeedLog;
import com.mongs.play.domain.mong.repository.MongFeedLogRepository;
import com.mongs.play.domain.mong.vo.MongFeedLogVo;
import com.mongs.play.domain.mong.vo.MongStatusPercentVo;
import com.mongs.play.domain.mong.vo.MongStatusVo;
import com.mongs.play.domain.mong.vo.MongVo;
import com.mongs.play.module.code.entity.FoodCode;
import com.mongs.play.module.code.entity.MongCode;
import com.mongs.play.module.code.service.CodeService;
import com.mongs.play.domain.mong.entity.Mong;
import com.mongs.play.domain.mong.entity.MongLog;
import com.mongs.play.domain.mong.enums.*;
import com.mongs.play.domain.mong.repository.MongLogRepository;
import com.mongs.play.domain.mong.repository.MongRepository;
import com.mongs.play.domain.mong.utils.MongUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MongService {

    private final Random random = new Random();

    private final CodeService codeService;
    private final MongRepository mongRepository;
    private final MongLogRepository mongLogRepository;
    private final MongFeedLogRepository mongFeedLogRepository;

    public List<Mong> findMongByAccountId(Long accountId) {
        return mongRepository.findByAccountIdAndIsActiveTrue(accountId);
    }

    public MongVo findMongVoByMongId(Long mongId) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        return MongVo.builder()
                .pastMong(Mong.copy(mong))
                .mong(mong)
                .build();
    }

    @MongStatusValidation
    public Mong addMong(Long accountId, String name, String sleepStart, String sleepEnd) {

        List<MongCode> mongCodeList = codeService.getMongCodeByLevel(MongGrade.ZERO.level);
        int randIdx = random.nextInt(mongCodeList.size());

        String eggMongCode = mongCodeList.get(randIdx).code();

        Mong newMong = mongRepository.save(Mong.builder()
                .accountId(accountId)
                .name(name)
                .sleepTime(sleepStart)
                .wakeUpTime(sleepEnd)
                .mongCode(eggMongCode)
                .grade(MongGrade.ZERO)
                .build());

        MongLogCode mongLogCode = MongLogCode.CREATE;
        mongLogRepository.save(MongLog.builder()
                .mongId(newMong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s", mongLogCode.message, newMong.getMongCode()))
                .build());

        return newMong;
    }

    @MongStatusValidation
    public Mong removeMong(Long mongId) throws NotFoundException {

        Mong mong = findMongVoByMongId(mongId).mong();

        mong = mongRepository.save(mong.toBuilder()
                .isActive(false)
                .grade(MongGrade.EMPTY)
                .shift(MongShift.DELETE)
                .state(MongState.NORMAL)
                .exp(0D)
                .weight(0D)
                .strength(0D)
                .satiety(0D)
                .healthy(0D)
                .sleep(0D)
                .poopCount(0)
                .isSleeping(false)
                .build());

        MongLogCode mongLogCode = MongLogCode.DELETE;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s", mongLogCode.message))
                .build());

        return mong;
    }

    @MongStatusValidation
    @MongEvolutionValidation
    public MongVo strokeMong(Long mongId, Integer strokeCount) throws NotFoundException {

        MongVo mongVo = findMongVoByMongId(mongId);
        Mong mong = mongVo.mong();

        if (MongGrade.EMPTY.equals(mong.getGrade())) {
            throw new InvalidException(MongErrorCode.INVALID_STROKE);
        }
        if (MongGrade.ZERO.equals(mong.getGrade())) {
            throw new InvalidException(MongErrorCode.INVALID_STROKE);
        }
        if (MongGrade.LAST.equals(mong.getGrade())) {
            throw new InvalidException(MongErrorCode.INVALID_STROKE);
        }

        int numberOfStroke = mong.getNumberOfStroke() + strokeCount;
        double exp = mong.getExp() + MongExp.STROKE.exp;

        mong = mongRepository.save(mong.toBuilder()
                .exp(exp)
                .numberOfStroke(numberOfStroke)
                .build());

        MongLogCode mongLogCode = MongLogCode.STROKE;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%d", mongLogCode.message, strokeCount))
                .build());

        return mongVo;
    }

    public MongVo sleepingMong(Long mongId) throws NotFoundException {

        MongVo mongVo = findMongVoByMongId(mongId);
        Mong mong = mongVo.mong();

        if (MongGrade.EMPTY.equals(mong.getGrade())) {
            throw new InvalidException(mong.getIsSleeping() ? MongErrorCode.INVALID_AWAKE : MongErrorCode.INVALID_SLEEPING);
        }
        if (MongGrade.ZERO.equals(mong.getGrade())) {
            throw new InvalidException(mong.getIsSleeping() ? MongErrorCode.INVALID_AWAKE : MongErrorCode.INVALID_SLEEPING);
        }
        if (MongGrade.LAST.equals(mong.getGrade())) {
            throw new InvalidException(mong.getIsSleeping() ? MongErrorCode.INVALID_AWAKE : MongErrorCode.INVALID_SLEEPING);
        }

        boolean isSleeping = !mong.getIsSleeping();

        mong = mongRepository.save(mong.toBuilder()
                .isSleeping(isSleeping)
                .build());

        MongLogCode mongLogCode = mong.getIsSleeping() ? MongLogCode.SLEEP : MongLogCode.AWAKE;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s", mongLogCode.message))
                .build());

        return mongVo;
    }

    @MongStatusValidation
    @MongEvolutionValidation
    public MongVo poopCleanMong(Long mongId) throws NotFoundException {

        MongVo mongVo = findMongVoByMongId(mongId);
        Mong mong = mongVo.mong();

        if (MongGrade.EMPTY.equals(mong.getGrade())) {
            throw new InvalidException(MongErrorCode.INVALID_POOP_CLEAN);
        }
        if (MongGrade.ZERO.equals(mong.getGrade())) {
            throw new InvalidException(MongErrorCode.INVALID_POOP_CLEAN);
        }
        if (MongGrade.LAST.equals(mong.getGrade())) {
            throw new InvalidException(MongErrorCode.INVALID_POOP_CLEAN);
        }

        double exp = mong.getExp() + MongExp.CLEANING_POOP.exp;

        mong = mongRepository.save(mong.toBuilder()
                .exp(exp)
                .poopCount(0)
                .build());

        MongLogCode mongLogCode = MongLogCode.POOP_CLEAN;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s", mongLogCode.message))
                .build());

        return mongVo;
    }

    public Boolean validationTrainingMong(Long mongId, MongTrainingCode mongTrainingCode) throws NotFoundException {

        Mong mong = findMongVoByMongId(mongId).mong();

        if (MongGrade.EMPTY.equals(mong.getGrade())) {
            throw new InvalidException(MongErrorCode.INVALID_TRAINING);
        }
        if (MongGrade.ZERO.equals(mong.getGrade())) {
            throw new InvalidException(MongErrorCode.INVALID_TRAINING);
        }

        return mong.getPayPoint() >= mongTrainingCode.point;
    }

    @MongStatusValidation
    @MongStateValidation
    @MongEvolutionValidation
    public MongVo trainingMong(Long mongId, Integer trainingCount, MongTrainingCode mongTrainingCode) throws NotFoundException {

        MongVo mongVo = findMongVoByMongId(mongId);
        Mong mong = mongVo.mong();

        if (mong.getPayPoint() < mongTrainingCode.point) {
            throw new InvalidException(MongErrorCode.NOT_ENOUGH_PAY_POINT);
        }
        if (MongGrade.EMPTY.equals(mong.getGrade())) {
            throw new InvalidException(MongErrorCode.INVALID_TRAINING);
        }
        if (MongGrade.ZERO.equals(mong.getGrade())) {
            throw new InvalidException(MongErrorCode.INVALID_TRAINING);
        }

        double exp = mong.getExp() + mongTrainingCode.exp;
        double weight = mong.getWeight() + mongTrainingCode.addWeightValue;
        double strength = mong.getStrength() + mongTrainingCode.addStrengthValue;
        double satiety = mong.getSatiety() + mongTrainingCode.addSatietyValue;
        double healthy = mong.getHealthy() + mongTrainingCode.addHealthyValue;
        double sleep = mong.getSleep() + mongTrainingCode.addSleepValue;
        int payPoint = mong.getPayPoint() - mongTrainingCode.point;
        int numberOfTraining = mong.getNumberOfTraining() + trainingCount;

        mong = mongRepository.save(mong.toBuilder()
                .exp(exp)
                .weight(weight)
                .strength(strength)
                .satiety(satiety)
                .healthy(healthy)
                .sleep(sleep)
                .numberOfTraining(numberOfTraining)
                .payPoint(payPoint)
                .build());

        MongLogCode mongLogCode = MongLogCode.TRAINING;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s:%d", mongLogCode.message, mongTrainingCode.code, trainingCount))
                .build());

        return mongVo;
    }

    @MongStatusValidation
    public MongVo graduateMong(Long mongId) throws NotFoundException {

        MongVo mongVo = findMongVoByMongId(mongId);
        Mong mong = mongVo.mong();

        if (!MongGrade.LAST.equals(mong.getGrade())) {
            throw new InvalidException(MongErrorCode.INVALID_GRADUATION);
        }
        if (!MongShift.GRADUATE_READY.equals(mong.getShift())) {
            throw new InvalidException(MongErrorCode.INVALID_GRADUATION);
        }

        mong = mongRepository.save(mong.toBuilder()
                .isActive(false)
                .grade(MongGrade.EMPTY)
                .shift(MongShift.GRADUATE)
                .state(MongState.EMPTY)
                .exp(0D)
                .weight(0D)
                .strength(0D)
                .satiety(0D)
                .healthy(0D)
                .sleep(0D)
                .poopCount(0)
                .isSleeping(false)
                .build());

        MongLogCode mongLogCode = MongLogCode.GRADUATE;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s", mongLogCode.message, mong.getMongCode()))
                .build());

        return mongVo;
    }

    public MongVo evolutionReadyMong(Long mongId) throws NotFoundException {

        MongVo mongVo = findMongVoByMongId(mongId);
        Mong mong = mongVo.mong();

        mong = mongRepository.save(mong.toBuilder()
                .shift(MongShift.EVOLUTION_READY)
                .build());

        MongLogCode mongLogCode = MongLogCode.EVOLUTION_READY;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s",  mongLogCode.message))
                .build());

        return mongVo;
    }

    @MongStatusValidation
    @MongStateValidation
    @MongEvolutionValidation
    public MongVo evolutionMong(Long mongId) throws NotFoundException {

        MongVo mongVo = findMongVoByMongId(mongId);
        Mong mong = mongVo.mong();

        if (MongGrade.LAST.equals(mong.getGrade())) {
            throw new InvalidException(MongErrorCode.INVALID_EVOLUTION);
        }
        if (!MongShift.EVOLUTION_READY.equals(mong.getShift())) {
            throw new InvalidException(MongErrorCode.INVALID_EVOLUTION);
        }
        if (mong.getIsSleeping()) {
            throw new InvalidException(MongErrorCode.INVALID_EVOLUTION);
        }
        if (mong.getExp() < mong.getGrade().evolutionExp) {
            throw new InvalidException(MongErrorCode.NOT_ENOUGH_EXP);
        }

        if (MongGrade.ZERO.equals(mong.getGrade())) {

            List<MongCode> mongCodeList = codeService.getMongCodeByLevel(MongGrade.ZERO.nextGrade.level);
            int randIdx = random.nextInt(mongCodeList.size());

            mong = mong.toBuilder()
                    .shift(MongShift.NORMAL)
                    .state(MongState.NORMAL)
                    .mongCode(mongCodeList.get(randIdx).code())
                    .build();

        } else if (MongGrade.LAST.equals(mong.getGrade().nextGrade)) {

            mong = mong.toBuilder()
                    .shift(MongShift.GRADUATE_READY)
                    .state(MongState.NORMAL)
                    .strength(mong.getGrade().nextGrade.maxStatus)
                    .satiety(mong.getGrade().nextGrade.maxStatus)
                    .healthy(mong.getGrade().nextGrade.maxStatus)
                    .sleep(mong.getGrade().nextGrade.maxStatus)
                    .exp(mong.getGrade().nextGrade.maxStatus)
                    .build();
        } else {
            // TODO("진화 포인트 환산")
            List<MongCode> mongCodeList = codeService.getMongCodeByLevelAndEvolutionPoint(mong.getGrade().nextGrade.level, mong.getEvolutionPoint());
            // TODO("컬렉션 목록을 조회하여 겹치지 않도록 하는 로직 필요")

            MongStatusPercentVo mongStatusPercentVo = MongUtil.statusToPercent(mong.getGrade(), mong);
            MongStatusVo mongStatusVo = MongUtil.percentToStatus(mong.getGrade().nextGrade, mongStatusPercentVo);

            mong = mong.toBuilder()
                    .mongCode(mongCodeList.get(mongCodeList.size() - 1).code())
                    .strength(mongStatusVo.strength())
                    .shift(MongShift.NORMAL)
                    .satiety(mongStatusVo.satiety())
                    .healthy(mongStatusVo.healthy())
                    .sleep(mongStatusVo.sleep())
                    .exp(0D)
                    .build();
        }

        mong = mongRepository.save(mong.toBuilder()
                .grade(mong.getGrade().nextGrade)
                .build());

        MongLogCode mongLogCode = MongLogCode.EVOLUTION;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s",  mongLogCode.message, mong.getMongCode()))
                .build());

        return mongVo;
    }

    @MongStatusValidation
    public MongVo deadMong(Long mongId) throws NotFoundException {

        MongVo mongVo = findMongVoByMongId(mongId);
        Mong mong = mongVo.mong();

        mong = mongRepository.save(mong.toBuilder()
                .isActive(false)
                .grade(MongGrade.EMPTY)
                .shift(MongShift.DEAD)
                .state(MongState.EMPTY)
                .exp(0D)
                .weight(0D)
                .strength(0D)
                .satiety(0D)
                .healthy(0D)
                .sleep(0D)
                .poopCount(0)
                .isSleeping(false)
                .build());

        MongLogCode mongLogCode = MongLogCode.DEAD;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s", mongLogCode.message, mong.getMongCode()))
                .build());

        return mongVo;
    }

    @MongStatusValidation
    @MongStateValidation
    @MongEvolutionValidation
    public MongVo feedMong(Long mongId, String foodCode) throws NotFoundException {

        MongVo mongVo = findMongVoByMongId(mongId);
        Mong mong = mongVo.mong();

        if (MongGrade.EMPTY.equals(mong.getGrade())) {
            throw new InvalidException(MongErrorCode.INVALID_FEED);
        }
        if (MongGrade.ZERO.equals(mong.getGrade())) {
            throw new InvalidException(MongErrorCode.INVALID_FEED);
        }

        FoodCode food = codeService.getFoodCode(foodCode);

        if (mong.getPayPoint() < food.price()) {
            throw new InvalidException(MongErrorCode.NOT_ENOUGH_PAY_POINT);
        }

        double exp = mong.getExp() + MongExp.EAT_THE_FOOD.exp;
        double weight = mong.getWeight() + food.addWeightValue();
        double strength = mong.getStrength() + food.addStrengthValue();
        double satiety = mong.getSatiety() + food.addSatietyValue();
        double healthy = mong.getHealthy() + food.addHealthyValue();
        double sleep = mong.getSleep() + food.addSleepValue();
        int payPoint = mong.getPayPoint() - food.price();

        mong = mongRepository.save(mong.toBuilder()
                .exp(exp)
                .weight(weight)
                .strength(strength)
                .satiety(satiety)
                .healthy(healthy)
                .sleep(sleep)
                .payPoint(payPoint)
                .build());

        MongLogCode mongLogCode = MongLogCode.FEED;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s:$%d", mongLogCode.message, food.code(), food.price()))
                .build());

        MongFeedLog mongFeedLog = mongFeedLogRepository.findByMongIdAndCode(mong.getId(), food.code())
                        .orElseGet(() -> MongFeedLog.builder()
                                .mongId(mongId)
                                .code(food.code())
                                .build());

        mongFeedLogRepository.save(mongFeedLog.toBuilder()
                .price(food.price())
                .build());

        return mongVo;
    }

    public List<MongFeedLogVo> findMongFeedLogByMongId(Long mongId) {

        Map<String, MongFeedLog> mongFeedLogMap = mongFeedLogRepository.findByMongId(mongId).stream()
                .collect(Collectors.toMap(MongFeedLog::getCode, mongFeedLog -> mongFeedLog));

        List<MongFeedLogVo> mongFeedLogVoList = MongFeedLogVo.toList(codeService.getFoodCode());

        return mongFeedLogVoList.stream()
                .map(mongFeedLogVo -> {
                    if (mongFeedLogMap.containsKey(mongFeedLogVo.code())) {
                        MongFeedLog mongFeedLog = mongFeedLogMap.get(mongFeedLogVo.code());
                        return mongFeedLogVo.toBuilder()
                                .lastBuyAt(mongFeedLog.getUpdatedAt())
                                .build();
                    } else {
                        return mongFeedLogVo;
                    }
                })
                .toList();
    }
}
