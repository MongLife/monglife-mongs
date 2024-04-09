package com.mongs.lifecycle.service.componentService;

import com.mongs.core.enums.management.MongHistoryCode;
import com.mongs.core.enums.management.MongShift;
import com.mongs.core.enums.management.MongState;
import com.mongs.core.enums.lifecycle.TaskCode;
import com.mongs.lifecycle.entity.Mong;
import com.mongs.lifecycle.exception.EventTaskException;
import com.mongs.lifecycle.exception.LifecycleErrorCode;
import com.mongs.lifecycle.repository.MongRepository;
import com.mongs.lifecycle.service.event.vo.StateCheckEvent;
import com.mongs.lifecycle.service.moduleService.MongHistoryService;
import com.mongs.lifecycle.service.moduleService.NotificationService;
import com.mongs.lifecycle.service.moduleService.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.mongs.core.utils.MongUtil.getNextState;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskActiveService {
    private final boolean isDebug = false;
    private static final Integer POOP_MAX = 4;

    private final MongRepository mongRepository;
    private final NotificationService notificationService;
    private final MongHistoryService mongHistoryService;

    @Transactional
    public void eggEvolution(Long mongId) throws EventTaskException {
        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        Mong saveMong = mongRepository.save(mong.toBuilder()
                .shift(MongShift.EVOLUTION_READY)
                .build());

        notificationService.publishEvolutionReady(saveMong.getAccountId(), PublishEvolutionReadyVo.of(saveMong));
        mongHistoryService.saveMongHistory(saveMong.getId(), MongHistoryCode.EVOLUTION);
    }

    @Transactional
    public void decreaseWeight(Long mongId, TaskCode taskCode, LocalDateTime createdAt) throws EventTaskException {
        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        long seconds = Math.min(taskCode.getExpiration(), Duration.between(createdAt, LocalDateTime.now()).getSeconds());
        double subWeight = taskCode.getValue() / taskCode.getExpiration() * seconds;
        double newWeight = Math.max(0D, mong.getWeight() - subWeight);
        MongState nextState = getNextState(
                mong.getState(),
                mong.getGrade(),
                newWeight,
                mong.getStrength(),
                mong.getSatiety(),
                mong.getHealthy(),
                mong.getSleep()
        );

        if (isDebug) {
            log.info("[{}] 몸무게 {} 감소", mongId, mong.getWeight() - newWeight);
        }

        Mong saveMong = mongRepository.save(mong.toBuilder()
                .weight(newWeight)
                .state(nextState)
                .build());

        notificationService.publishWeight(saveMong.getAccountId(), PublishWeightVo.of(saveMong));
    }

    @Transactional
    public void decreaseStrength(Long mongId, TaskCode taskCode, LocalDateTime createdAt) throws EventTaskException {
        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        long seconds = Math.min(taskCode.getExpiration(), Duration.between(createdAt, LocalDateTime.now()).getSeconds());
        double subStrength = taskCode.getValue() / taskCode.getExpiration() * seconds;
        double newStrength = Math.max(0D, mong.getStrength() - subStrength);
        MongState nextState = getNextState(
                mong.getState(),
                mong.getGrade(),
                mong.getWeight(),
                newStrength,
                mong.getSatiety(),
                mong.getHealthy(),
                mong.getSleep()
        );

        if (isDebug) {
            log.info("[{}] 근력 {} 감소", mongId, mong.getStrength() - newStrength);
        }

        Mong saveMong = mongRepository.save(mong.toBuilder()
                .strength(newStrength)
                .state(nextState)
                .build());

        notificationService.publishStrength(saveMong.getAccountId(), PublishStrengthVo.of(saveMong));
    }

    @Transactional
    public double decreaseSatiety(Long mongId, TaskCode taskCode, LocalDateTime createdAt) throws EventTaskException {
        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        long seconds = Math.min(taskCode.getExpiration(), Duration.between(createdAt, LocalDateTime.now()).getSeconds());
        double subSatiety = taskCode.getValue() / taskCode.getExpiration() * seconds;
        double newSatiety = Math.max(0D, mong.getSatiety() - subSatiety);
        MongState nextState = getNextState(
                mong.getState(),
                mong.getGrade(),
                mong.getWeight(),
                mong.getStrength(),
                newSatiety,
                mong.getHealthy(),
                mong.getSleep()
        );

        if (isDebug) {
            log.info("[{}] 포만감 {} 감소", mongId, mong.getSatiety() - newSatiety);
        }

        Mong saveMong = mongRepository.save(mong.toBuilder()
                .satiety(newSatiety)
                .state(nextState)
                .build());

        notificationService.publishSatiety(saveMong.getAccountId(), PublishSatietyVo.of(saveMong));

        return saveMong.getSatiety();
    }

    @Transactional
    public double decreaseHealthy(Long mongId, TaskCode taskCode, LocalDateTime createdAt) throws EventTaskException {
        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        long seconds = Math.min(taskCode.getExpiration(), Duration.between(createdAt, LocalDateTime.now()).getSeconds());
        double subHealthy = taskCode.getValue() / taskCode.getExpiration() * seconds;
        double newHealthy = Math.max(0D, mong.getHealthy() - subHealthy);
        MongState nextState = getNextState(
                mong.getState(),
                mong.getGrade(),
                mong.getWeight(),
                mong.getStrength(),
                mong.getSatiety(),
                newHealthy,
                mong.getSleep()
        );

        if (isDebug) {
            log.info("[{}] 체력 {} 감소", mongId, mong.getHealthy() - newHealthy);
        }

        Mong saveMong = mongRepository.save(mong.toBuilder()
                .healthy(newHealthy)
                .state(nextState)
                .build());

        notificationService.publishHealthy(saveMong.getAccountId(), PublishHealthyVo.of(saveMong));

        return saveMong.getHealthy();
    }

    @Transactional
    public void decreaseSleep(Long mongId, TaskCode taskCode, LocalDateTime createdAt) throws EventTaskException {
        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        long seconds = Math.min(taskCode.getExpiration(), Duration.between(createdAt, LocalDateTime.now()).getSeconds());
        double subSleep = taskCode.getValue() / taskCode.getExpiration() * seconds;
        double newSleep = Math.max(0D, mong.getSleep() - subSleep);
        MongState nextState = getNextState(
                mong.getState(),
                mong.getGrade(),
                mong.getWeight(),
                mong.getStrength(),
                mong.getSatiety(),
                mong.getHealthy(),
                newSleep
        );

        if (isDebug) {
            log.info("[{}] 피로도 {} 감소", mongId, mong.getSleep() - newSleep);
        }

        Mong saveMong = mongRepository.save(mong.toBuilder()
                .sleep(newSleep)
                .state(nextState)
                .build());

        notificationService.publishSleep(saveMong.getAccountId(), PublishSleepVo.of(saveMong));
    }

    @Transactional
    public void increaseSleep(Long mongId, TaskCode taskCode, LocalDateTime createdAt) throws EventTaskException {
        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        long seconds = Math.min(taskCode.getExpiration(), Duration.between(createdAt, LocalDateTime.now()).getSeconds());
        double addSleep = taskCode.getValue() / taskCode.getExpiration() * seconds;
        double newSleep = Math.min(mong.getGrade().getMaxStatus(), mong.getSleep() + addSleep);
        MongState nextState = getNextState(
                mong.getState(),
                mong.getGrade(),
                mong.getWeight(),
                mong.getStrength(),
                mong.getSatiety(),
                mong.getHealthy(),
                newSleep
        );

        if (isDebug) {
            log.info("[{}] 피로도 {} 증가", mongId, newSleep - mong.getSleep());
        }

        Mong saveMong = mongRepository.save(mong.toBuilder()
                .sleep(newSleep)
                .state(nextState)
                .build());

        notificationService.publishSleep(saveMong.getAccountId(), PublishSleepVo.of(saveMong));

    }

    @Transactional
    public void increasePoop(Long mongId, TaskCode taskCode) throws EventTaskException {
        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        int addPoop = taskCode.getValue().intValue();
        int newPoop = Math.min(POOP_MAX, mong.getNumberOfPoop() + addPoop);

        if (newPoop == POOP_MAX) {
            int newPenalty = mong.getPenalty() + 1;
            Mong saveMong = mongRepository.save(mong.toBuilder()
                    .penalty(newPenalty)
                    .build());

            if (isDebug) {
                log.info("[{}] 똥 {} 개 도달 : 패널티 1 증가 ({})", mongId, POOP_MAX, newPenalty);
            }

            mongHistoryService.saveMongHistory(saveMong.getId(), MongHistoryCode.PENALTY);

        } else {
            if (isDebug) {
                log.info("[{}] 똥 {} 개 생성", mongId, newPoop - mong.getNumberOfPoop());
            }

            Mong saveMong = mongRepository.save(mong.toBuilder()
                    .numberOfPoop(newPoop)
                    .build());

            notificationService.publishPoop(saveMong.getAccountId(), PublishPoopVo.of(saveMong));
        }
    }

    @Transactional
    public void dead(Long mongId, TaskCode taskCode) throws EventTaskException {
        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        Mong saveMong = mongRepository.save(mong.toBuilder()
                .numberOfPoop(0)
                .healthy(taskCode.getValue())
                .satiety(taskCode.getValue())
                .sleep(taskCode.getValue())
                .strength(taskCode.getValue())
                .weight(taskCode.getValue())
                .shift(MongShift.DEAD)
                .state(MongState.EMPTY)
                .build());

        notificationService.publishDead(saveMong.getAccountId(), PublishDeadVo.of(saveMong));

        mongHistoryService.saveMongHistory(saveMong.getId(), MongHistoryCode.DEAD);

        log.info("[{}] 몽 사망 ({})", mongId, mong.getName());
    }

    @Transactional
    public void increasePayPoint(Long mongId, TaskCode taskCode) throws EventTaskException {
        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        int addPayPoint = taskCode.getValue().intValue();
        int newPayPoint = mong.getPayPoint() + addPayPoint;

        if (isDebug) {
            log.info("[{}] 페이포인트 {} 증가", mongId, addPayPoint);
        }

        mongRepository.save(mong.toBuilder()
                .payPoint(newPayPoint)
                .build());
    }
}
