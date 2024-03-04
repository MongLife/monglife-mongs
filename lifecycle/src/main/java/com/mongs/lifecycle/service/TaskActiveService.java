package com.mongs.lifecycle.service;

import com.mongs.core.code.enums.management.MongShift;
import com.mongs.core.code.enums.management.MongState;
import com.mongs.lifecycle.code.TaskCode;
import com.mongs.lifecycle.entity.Mong;
import com.mongs.lifecycle.exception.EventTaskException;
import com.mongs.lifecycle.exception.LifecycleErrorCode;
import com.mongs.lifecycle.repository.MongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskActiveService {

    private final boolean isDebug = true;

    private final MongRepository mongRepository;

    @Value("${application.scheduler.sleep-max}")
    private Double SLEEP_MAX;
    @Value("${application.scheduler.poop-max}")
    private Integer POOP_MAX;

    @Transactional
    public void decreaseWeight(Long mongId, TaskCode taskCode, LocalDateTime createdAt) {
        try {
            Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                    .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

            long seconds = Math.min(taskCode.getExpiration(), Duration.between(createdAt, LocalDateTime.now()).getSeconds());
            double subWeight = taskCode.getValue() / taskCode.getExpiration() * seconds;
            double newWeight = Math.max(0D, mong.getWeight() - subWeight);

            if (isDebug) {
                log.info("[{}] 몸무게 {} 감소", mongId, mong.getWeight() - newWeight);
            }

            mongRepository.save(mong.toBuilder()
                    .weight(newWeight)
                    .build());
        } catch (EventTaskException e) {
            // log.info("[{}] 사망하거나 존재하지 않은 몽", mongId);
        }
    }

    @Transactional
    public void decreaseStrength(Long mongId, TaskCode taskCode, LocalDateTime createdAt) {
        try {
            Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                    .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

            long seconds = Math.min(taskCode.getExpiration(), Duration.between(createdAt, LocalDateTime.now()).getSeconds());
            double subStrength = taskCode.getValue() / taskCode.getExpiration() * seconds;
            double newStrength = Math.max(0D, mong.getStrength() - subStrength);

            if (isDebug) {
                log.info("[{}] 근력 {} 감소", mongId, mong.getStrength() - newStrength);
            }

            mongRepository.save(mong.toBuilder()
                    .strength(newStrength)
                    .build());
        } catch (EventTaskException e) {
            // log.info("[{}] 사망하거나 존재하지 않은 몽", mongId);
        }
    }

    @Transactional
    public double decreaseSatiety(Long mongId, TaskCode taskCode, LocalDateTime createdAt) {
        double newSatiety = -1D;

        try {
            Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                    .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

            long seconds = Math.min(taskCode.getExpiration(), Duration.between(createdAt, LocalDateTime.now()).getSeconds());
            double subSatiety = taskCode.getValue() / taskCode.getExpiration() * seconds;
            newSatiety = Math.max(0D, mong.getSatiety() - subSatiety);

            if (isDebug) {
                log.info("[{}] 포만감 {} 감소", mongId, mong.getSatiety() - newSatiety);
            }

            mongRepository.save(mong.toBuilder()
                    .satiety(newSatiety)
                    .build());
        } catch (EventTaskException e) {
            // log.info("[{}] 사망하거나 존재하지 않은 몽", mongId);
        }

        return newSatiety;
    }

    @Transactional
    public double decreaseHealthy(Long mongId, TaskCode taskCode, LocalDateTime createdAt) {
        double newHealthy = -1D;

        try {
            Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                    .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

            long seconds = Math.min(taskCode.getExpiration(), Duration.between(createdAt, LocalDateTime.now()).getSeconds());
            double subHealthy = taskCode.getValue() / taskCode.getExpiration() * seconds;
            newHealthy = Math.max(0D, mong.getHealthy() - subHealthy);

            if (isDebug) {
                log.info("[{}] 체력 {} 감소", mongId, mong.getHealthy() - newHealthy);
            }

            mongRepository.save(mong.toBuilder()
                    .healthy(newHealthy)
                    .build());
        } catch (EventTaskException e) {
            // log.info("[{}] 사망하거나 존재하지 않은 몽", mongId);
        }

        return newHealthy;
    }

    @Transactional
    public void decreaseSleep(Long mongId, TaskCode taskCode, LocalDateTime createdAt) {
        try {
            Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                    .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

            long seconds = Math.min(taskCode.getExpiration(), Duration.between(createdAt, LocalDateTime.now()).getSeconds());
            double subSleep = taskCode.getValue() / taskCode.getExpiration() * seconds;
            double newSleep = Math.max(0D, mong.getSleep() - subSleep);

            if (isDebug) {
                log.info("[{}] 피로도 {} 감소", mongId, mong.getSleep() - newSleep);
            }

            mongRepository.save(mong.toBuilder()
                    .sleep(newSleep)
                    .build());
        } catch (EventTaskException e) {
            // log.info("[{}] 사망하거나 존재하지 않은 몽", mongId);
        }
    }

    @Transactional
    public void increaseSleep(Long mongId, TaskCode taskCode, LocalDateTime createdAt) {
        try {
            Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                    .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

            long seconds = Math.min(taskCode.getExpiration(), Duration.between(createdAt, LocalDateTime.now()).getSeconds());
            double addSleep = taskCode.getValue() / taskCode.getExpiration() * seconds;
            double newSleep = Math.min(SLEEP_MAX, mong.getSleep() + addSleep);

            if (isDebug) {
                log.info("[{}] 피로도 {} 증가", mongId, newSleep - mong.getSleep());
            }

            mongRepository.save(mong.toBuilder()
                    .sleep(newSleep)
                    .build());
        } catch (EventTaskException e) {
            // log.info("[{}] 사망하거나 존재하지 않은 몽", mongId);
        }
    }

    @Transactional
    public void increasePayPoint(Long mongId, TaskCode taskCode) {
        try {
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
        } catch (EventTaskException e) {
            // log.info("[{}] 사망하거나 존재하지 않은 몽", mongId);
        }
    }

    @Transactional
    public void increasePoop(Long mongId, TaskCode taskCode) {
        try {
            Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                    .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

            int addPoop = taskCode.getValue().intValue();
            int newPoop = Math.min(POOP_MAX, mong.getNumberOfPoop() + addPoop);

            if (newPoop == POOP_MAX) {
                int newPenalty = mong.getPenalty() + 1;
                mongRepository.save(mong.toBuilder()
                        .penalty(newPenalty)
                        .build());

                if (isDebug) {
                    log.info("[{}] 똥 {} 개 도달 : 패널티 1 증가 ({})", mongId, POOP_MAX, newPenalty);
                }

            } else {
                if (isDebug) {
                    log.info("[{}] 똥 {} 개 생성", mongId, newPoop - mong.getNumberOfPoop());
                }

                mongRepository.save(mong.toBuilder()
                        .numberOfPoop(newPoop)
                        .build());
            }
        } catch (EventTaskException e) {
            // log.info("[{}] 사망하거나 존재하지 않은 몽", mongId);
        }
    }

    @Transactional
    public void dead(Long mongId, TaskCode taskCode) {
        try {
            Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                    .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

            mongRepository.save(mong.toBuilder()
                    .isActive(false)
                    .numberOfPoop(0)
                    .healthy(taskCode.getValue())
                    .satiety(taskCode.getValue())
                    .sleep(taskCode.getValue())
                    .strength(taskCode.getValue())
                    .weight(taskCode.getValue())
                    .shift(MongShift.DIE)
                    .state(MongState.EMPTY)
                    .build());

            log.info("[{}] 몽 사망 ({})", mongId, mong.getName());
        } catch (EventTaskException e) {
            // log.info("[{}] 사망하거나 존재하지 않은 몽", mongId);
        }
    }
}