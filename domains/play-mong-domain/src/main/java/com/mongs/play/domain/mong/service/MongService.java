package com.mongs.play.domain.mong.service;

import com.mongs.play.core.error.domain.MongErrorCode;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.mong.entity.MongFeedLog;
import com.mongs.play.domain.mong.repository.MongFeedLogRepository;
import com.mongs.play.domain.mong.vo.MongFeedLogVo;
import com.mongs.play.domain.mong.vo.MongStatusPercentVo;
import com.mongs.play.domain.mong.vo.MongStatusVo;
import com.mongs.play.domain.mong.vo.MongVo;
import com.mongs.play.domain.mong.entity.Mong;
import com.mongs.play.domain.mong.entity.MongLog;
import com.mongs.play.domain.mong.enums.*;
import com.mongs.play.domain.mong.repository.MongLogRepository;
import com.mongs.play.domain.mong.repository.MongRepository;
import com.mongs.play.domain.mong.utils.MongUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongService {
    private final MongRepository mongRepository;
    private final MongLogRepository mongLogRepository;
    private final MongFeedLogRepository mongFeedLogRepository;

    @Transactional(transactionManager = "mongTransactionManager", readOnly = true)
    public List<MongVo> findAllMong() {
        return MongVo.toList(mongRepository.findAllActive());
    }

    @Transactional(transactionManager = "mongTransactionManager", readOnly = true)
    public List<MongVo> findMongByAccountId(Long accountId) {
        return MongVo.toList(mongRepository.findByAccountIdAndIsActiveTrue(accountId));
    }

    @Transactional(transactionManager = "mongTransactionManager", readOnly = true)
    public MongVo findActiveMongById(Long mongId) throws NotFoundException {
        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_ACTIVE_MONG));

        return MongVo.of(mong);
    }

    @Transactional(transactionManager = "mongTransactionManager")
    public MongVo addMong(Long accountId, String mongCode, String name, String sleepStart, String sleepEnd) {

        Mong mong = mongRepository.save(Mong.builder()
                .accountId(accountId)
                .name(name)
                .sleepTime(sleepStart)
                .wakeUpTime(sleepEnd)
                .mongCode(mongCode)
                .shift(MongShift.NORMAL)
                .state(MongState.NORMAL)
                .grade(MongGrade.ZERO)
                .build().validation());

        MongLogCode mongLogCode = MongLogCode.CREATE;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s", mongLogCode.message, mong.getMongCode()))
                .build());

        log.info("[addMong] mongId: {}, mongCode: {}", mong.getId(), mong.getMongCode());

        return MongVo.of(mong);
    }

    @Transactional(transactionManager = "mongTransactionManager")
    public MongVo removeMong(Long mongId) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrueWithLock(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_ACTIVE_MONG));

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
                .build().validation());

        MongLogCode mongLogCode = MongLogCode.DELETE;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s", mongLogCode.message))
                .build());

        log.info("[removeMong] mongId: {}", mong.getId());

        return MongVo.of(mong);
    }

    @Transactional(transactionManager = "mongTransactionManager")
    public MongVo increaseNumberOfStroke(Long mongId, Integer strokeCount) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrueWithLock(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_ACTIVE_MONG));

        int numberOfStroke = mong.getNumberOfStroke() + strokeCount;
        double exp = mong.getExp() + MongExp.STROKE.exp;

        mong = mongRepository.save(mong.toBuilder()
                .exp(exp)
                .numberOfStroke(numberOfStroke)
                .build().validation());

        MongLogCode mongLogCode = MongLogCode.STROKE;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%d", mongLogCode.message, strokeCount))
                .build());

        log.info("[strokeMong] mongId: {}", mong.getId());

        return MongVo.of(mong);
    }

    @Transactional(transactionManager = "mongTransactionManager")
    public MongVo toggleIsSleeping(Long mongId) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrueWithLock(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_ACTIVE_MONG));

        boolean isSleeping = !mong.getIsSleeping();

        mong = mongRepository.save(mong.toBuilder()
                .isSleeping(isSleeping)
                .build().validation());

        MongLogCode mongLogCode = mong.getIsSleeping() ? MongLogCode.SLEEP : MongLogCode.AWAKE;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s", mongLogCode.message))
                .build());

        log.info("[sleepingMong] mongId: {}", mong.getId());

        return MongVo.of(mong);
    }

    @Transactional(transactionManager = "mongTransactionManager")
    public MongVo clearPoopCount(Long mongId) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrueWithLock(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_ACTIVE_MONG));

        int pastPoopCount = mong.getPoopCount();
        double exp = mong.getExp() + MongExp.CLEANING_POOP.exp * pastPoopCount;

        mong = mongRepository.save(mong.toBuilder()
                .exp(exp)
                .poopCount(0)
                .build().validation());

        MongLogCode mongLogCode = MongLogCode.DECREASE_POOP_COUNT;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s", mongLogCode.message, pastPoopCount))
                .build());

        log.info("[poopCleanMong] mongId: {}", mong.getId());

        return MongVo.of(mong);
    }

    @Transactional(transactionManager = "mongTransactionManager")
    public MongVo increaseStatusTraining(Long mongId, Integer trainingCount, MongTrainingCode mongTrainingCode) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrueWithLock(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_ACTIVE_MONG));

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
                .build().validation());

        MongLogCode mongLogCode = MongLogCode.TRAINING;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s:%d", mongLogCode.message, mongTrainingCode.code, trainingCount))
                .build());

        log.info("[trainingMong] mongId: {}", mong.getId());

        return MongVo.of(mong);
    }

    @Transactional(transactionManager = "mongTransactionManager")
    public MongVo toggleGraduate(Long mongId) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrueWithLock(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_ACTIVE_MONG));

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

        log.info("[graduateMong] mongId: {}", mong.getId());

        return MongVo.of(mong);
    }

    @Transactional(transactionManager = "mongTransactionManager")
    public MongVo toggleEvolutionReady(Long mongId) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrueWithLock(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_ACTIVE_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .shift(MongShift.EVOLUTION_READY)
                .build().validation());

        MongLogCode mongLogCode = MongLogCode.EVOLUTION_READY;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s",  mongLogCode.message))
                .build());

        log.info("[evolutionReadyMong] mongId: {}", mong.getId());

        return MongVo.of(mong);
    }

    @Transactional(transactionManager = "mongTransactionManager")
    public MongVo toggleFirstEvolution(Long mongId, String mongCode) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrueWithLock(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_ACTIVE_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .shift(MongShift.NORMAL)
                .state(MongState.NORMAL)
                .grade(mong.getGrade().nextGrade)
                .mongCode(mongCode)
                .build());

        MongLogCode mongLogCode = MongLogCode.EVOLUTION;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s",  mongLogCode.message, mong.getMongCode()))
                .build());

        log.info("[toggleFirstEvolution] mongId: {}", mong.getId());

        return MongVo.of(mong);
    }

    @Transactional(transactionManager = "mongTransactionManager")
    public MongVo toggleEvolution(Long mongId, String mongCode) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrueWithLock(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_ACTIVE_MONG));

        MongStatusPercentVo mongStatusPercentVo = MongUtil.statusToPercent(mong.getGrade(), mong);
        MongStatusVo mongStatusVo = MongUtil.percentToStatus(mong.getGrade().nextGrade, mongStatusPercentVo);

        mong = mongRepository.save(mong.toBuilder()
                .shift(MongShift.NORMAL)
                .grade(mong.getGrade().nextGrade)
                .mongCode(mongCode)
                .strength(mongStatusVo.strength())
                .satiety(mongStatusVo.satiety())
                .healthy(mongStatusVo.healthy())
                .sleep(mongStatusVo.sleep())
                .exp(0D)
                .build());

        MongLogCode mongLogCode = MongLogCode.EVOLUTION;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s",  mongLogCode.message, mong.getMongCode()))
                .build());

        log.info("[toggleEvolution] mongId: {}", mong.getId());

        return MongVo.of(mong);
    }

    @Transactional(transactionManager = "mongTransactionManager")
    public MongVo toggleLastEvolution(Long mongId) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrueWithLock(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_ACTIVE_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .shift(MongShift.GRADUATE_READY)
                .grade(mong.getGrade().nextGrade)
                .state(MongState.NORMAL)
                .strength(mong.getGrade().nextGrade.maxStatus)
                .satiety(mong.getGrade().nextGrade.maxStatus)
                .healthy(mong.getGrade().nextGrade.maxStatus)
                .sleep(mong.getGrade().nextGrade.maxStatus)
                .exp(mong.getGrade().nextGrade.maxStatus)
                .build());

        MongLogCode mongLogCode = MongLogCode.EVOLUTION;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s",  mongLogCode.message, mong.getMongCode()))
                .build());

        log.info("[toggleLastEvolution] mongId: {}", mong.getId());

        return MongVo.of(mong);
    }

    @Transactional(transactionManager = "mongTransactionManager")
    public MongVo deadMong(Long mongId) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrueWithLock(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_ACTIVE_MONG));

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
                .build().validation());

        MongLogCode mongLogCode = MongLogCode.DEAD;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s", mongLogCode.message, mong.getMongCode()))
                .build());

        log.info("[deadMong] mongId: {}", mong.getId());

        return MongVo.of(mong);
    }

    @Transactional(transactionManager = "mongTransactionManager")
    public MongVo feedMong(Long mongId, String foodCode, Double addWeight, Double addStrength, Double addSatiety, Double addHealthy, Double addSleep, Integer price) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrueWithLock(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_ACTIVE_MONG));

        double exp = mong.getExp() + MongExp.EAT_THE_FOOD.exp;
        double weight = mong.getWeight() + addWeight;
        double strength = mong.getStrength() + addStrength;
        double satiety = mong.getSatiety() + addSatiety;
        double healthy = mong.getHealthy() + addHealthy;
        double sleep = mong.getSleep() + addSleep;
        int payPoint = mong.getPayPoint() - price;

        mong = mongRepository.save(mong.toBuilder()
                .exp(exp)
                .weight(weight)
                .strength(strength)
                .satiety(satiety)
                .healthy(healthy)
                .sleep(sleep)
                .payPoint(payPoint)
                .build().validation());

        MongLogCode mongLogCode = MongLogCode.FEED;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s:$%d", mongLogCode.message, foodCode, price))
                .build());

        MongFeedLog mongFeedLog = mongFeedLogRepository.findByMongIdAndCodeWithLock(mong.getId(), foodCode)
                        .orElseGet(() -> MongFeedLog.builder()
                                .mongId(mongId)
                                .code(foodCode)
                                .build());

        mongFeedLogRepository.save(mongFeedLog.toBuilder()
                .price(price)
                .build());

        log.info("[feedMong] mongId: {}", mong.getId());

        return MongVo.of(mong);
    }

    @Transactional(transactionManager = "mongTransactionManager", readOnly = true)
    public List<MongFeedLogVo> findMongFeedLogByMongId(Long mongId) {
        return MongFeedLogVo.toList(mongFeedLogRepository.findByMongId(mongId));
    }

    @Transactional(transactionManager = "mongTransactionManager")
    public MongVo toggleIsDeadSchedule(Long mongId, Boolean isDeadSchedule) {

        Mong mong = mongRepository.findByIdAndIsActiveTrueWithLock(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_ACTIVE_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .isDeadSchedule(isDeadSchedule)
                .build().validation());

        return MongVo.of(mong);
    }
}
