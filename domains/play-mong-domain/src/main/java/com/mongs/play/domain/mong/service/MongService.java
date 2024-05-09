package com.mongs.play.domain.mong.service;

import com.mongs.play.core.error.domain.MongErrorCode;
import com.mongs.play.core.exception.common.InvalidException;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.mong.vo.MongStatusPercentVo;
import com.mongs.play.domain.mong.vo.MongStatusVo;
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
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MongService {

    private final Random random = new Random();

    private final CodeService codeService;
    private final MongRepository mongRepository;
    private final MongLogRepository mongLogRepository;

    public List<Mong> findMongByAccountId(Long accountId) {
        return mongRepository.findByAccountIdAndIsActiveTrue(accountId);
    }

    public Mong findMongByMongId(Long mongId) {
        return mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));
    }

    public Mong addMong(Long accountId, String name, String sleepStart, String sleepEnd) {

        List<MongCode> mongCodeList = codeService.getMongCodeByLevel(0);
        int randIdx = random.nextInt(mongCodeList.size());

        String eggMongCode = mongCodeList.get(randIdx).code();

        Mong mong = mongRepository.save(Mong.builder()
                .accountId(accountId)
                .name(name)
                .sleepTime(sleepStart)
                .wakeUpTime(sleepEnd)
                .mongCode(eggMongCode)
                .grade(MongGrade.ZERO)
                .build());

        MongLogCode mongLogCode = MongLogCode.CREATE;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s %s", mong.getMongCode(), mongLogCode.message))
                .build());

        return mong;
    }

    public Mong removeMong(Long mongId) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

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

    public Mong strokeMong(Long mongId, Integer strokeCount) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

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
                .message(String.format("%s", mongLogCode.message))
                .build());

        return mong;
    }

    public Mong sleepingMong(Long mongId) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

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

        return mong;
    }

    public Mong poopCleanMong(Long mongId) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

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

        return mong;
    }

    public Boolean validationTrainingMong(Long mongId, MongTrainingCode mongTrainingCode) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        return mong.getPayPoint() >= mongTrainingCode.point;
    }

    public Mong trainingMong(Long mongId, Integer trainingCount, MongTrainingCode mongTrainingCode) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        if (mong.getPayPoint() < mongTrainingCode.point) {
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
                .message(String.format("%s", mongLogCode.message))
                .build());

        return mong;
    }

    public Mong graduateMong(Long mongId) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

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
                .message(String.format("%s", mongLogCode.message))
                .build());

        return mong;
    }

    public Mong evolutionMong(Long mongId) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        if (MongGrade.LAST.equals(mong.getGrade())) {
            throw new InvalidException(MongErrorCode.INVALID_EVOLUTION);
        }
        if (!MongShift.EVOLUTION_READY.equals(mong.getShift())) {
            throw new InvalidException(MongErrorCode.INVALID_EVOLUTION);
        }
        if (mong.getIsSleeping()) {
            throw new InvalidException(MongErrorCode.INVALID_EVOLUTION);
        }
        if (mong.getExp() < mong.getGrade().nextGrade.evolutionExp) {
            throw new InvalidException(MongErrorCode.NOT_ENOUGH_EXP);
        }

        // TODO("진화 포인트 환산")

        List<MongCode> mongCodeList = codeService.getMongCodeByEvolutionPoint(mong.getEvolutionPoint());

        // TODO("컬렉션 목록을 조회하여 겹치지 않도록 하는 로직 필요")

        String mongCode = mongCodeList.get(mongCodeList.size() - 1).code();

        MongStatusPercentVo mongStatusPercentVo = MongUtil.statusToPercent(mong.getGrade(), mong);

        MongShift mongShift = MongGrade.LAST.equals(mong.getGrade().nextGrade) ? MongShift.GRADUATE_READY : mong.getShift();
        MongState mongState = MongGrade.LAST.equals(mong.getGrade().nextGrade) ? MongState.NORMAL : mong.getState();
        MongGrade mongGrade = mong.getGrade().nextGrade;

        MongStatusVo mongStatusVo = MongUtil.percentToStatus(mongGrade, mongStatusPercentVo);

        mong = mongRepository.save(mong.toBuilder()
                .mongCode(mongCode)
                .grade(mongGrade)
                .shift(mongShift)
                .state(mongState)
                .weight(mongStatusVo.weight())
                .strength(mongStatusVo.strength())
                .satiety(mongStatusVo.satiety())
                .healthy(mongStatusVo.healthy())
                .sleep(mongStatusVo.sleep())
                .exp(mongStatusVo.exp())
                .build());

        MongLogCode mongLogCode = MongLogCode.EVOLUTION;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s %s", mong.getMongCode(), mongLogCode.message))
                .build());

        return mong;
    }

    public Mong deadMong(Long mongId) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

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
                .message(String.format("%s", mongLogCode.message))
                .build());

        return mong;
    }

    public Mong feedMong(Long mongId, String foodCode) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        if (MongGrade.EMPTY.equals(mong.getGrade())) {
            throw new InvalidException(MongErrorCode.INVALID_FEED);
        }
        if (MongGrade.ZERO.equals(mong.getGrade())) {
            throw new InvalidException(MongErrorCode.INVALID_FEED);
        }

        FoodCode food = codeService.getFoodCode(foodCode);

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
                .message(String.format("%s %s", food.code(), mongLogCode.message))
                .build());

        return mong;
    }
}
