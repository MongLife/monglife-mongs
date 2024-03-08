package com.mongs.lifecycle.service;

import com.mongs.core.code.enums.management.MongShift;
import com.mongs.core.code.enums.management.MongState;
import com.mongs.lifecycle.code.MongEventCode;
import com.mongs.lifecycle.entity.Mong;
import com.mongs.lifecycle.entity.MongEvent;
import com.mongs.lifecycle.exception.EventTaskException;
import com.mongs.lifecycle.exception.LifecycleErrorCode;
import com.mongs.lifecycle.repository.MongRepository;
import com.mongs.lifecycle.vo.StartTaskVo;
import com.mongs.lifecycle.vo.StopAllTaskVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskActiveServiceImpl implements TaskActiveService {

    private final MongRepository mongRepository;
    private final ApplicationEventPublisher taskServicePublisher;


    private final Double SLEEP_MAX = 100D;
    private final Integer POOP_MAX = 4;

    @Transactional
    public void decreaseWeight(MongEvent event) throws RuntimeException {
        Mong mong = mongRepository.findById(event.getMongId())
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        MongEventCode code = event.getEventCode();
        long seconds = Math.min(code.getExpiration(), Duration.between(event.getCreatedAt(), LocalDateTime.now()).getSeconds());
        double subWeight = code.getValue() / code.getExpiration() * seconds;
        double newWeight = Math.max(0D, mong.getWeight() - subWeight);

        // log.info("[{}] 몸무게 {} 감소", event.getMongId(), mong.getWeight() - newWeight);

        mongRepository.save(mong.toBuilder()
                .weight(newWeight)
                .build());
    }

    @Transactional
    public void decreaseStrength(MongEvent event) throws RuntimeException {
        Mong mong = mongRepository.findById(event.getMongId())
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        MongEventCode code = event.getEventCode();
        long seconds = Math.min(code.getExpiration(), Duration.between(event.getCreatedAt(), LocalDateTime.now()).getSeconds());
        double subStrength = code.getValue() / code.getExpiration() * seconds;
        double newStrength = Math.max(0D, mong.getStrength() - subStrength);

        // log.info("[{}] 근력 {} 감소", event.getMongId(), mong.getStrength() - newStrength);

        mongRepository.save(mong.toBuilder()
                .strength(newStrength)
                .build());
    }

    @Transactional
    public double decreaseSatiety(MongEvent event) throws RuntimeException {
        Mong mong = mongRepository.findById(event.getMongId())
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        MongEventCode code = event.getEventCode();
        long seconds = Math.min(code.getExpiration(), Duration.between(event.getCreatedAt(), LocalDateTime.now()).getSeconds());
        double subSatiety = code.getValue() / code.getExpiration() * seconds;
        double newSatiety = Math.max(0D, mong.getSatiety() - subSatiety);

        // log.info("[{}] 포만감 {} 감소", event.getMongId(), mong.getSatiety() - newSatiety);

        mongRepository.save(mong.toBuilder()
                .satiety(newSatiety)
                .build());

        return newSatiety;
    }

    @Transactional
    public double decreaseHealthy(MongEvent event) throws RuntimeException {
        Mong mong = mongRepository.findById(event.getMongId())
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        MongEventCode code = event.getEventCode();
        long seconds = Math.min(code.getExpiration(), Duration.between(event.getCreatedAt(), LocalDateTime.now()).getSeconds());
        double subHealthy = code.getValue() / code.getExpiration() * seconds;
        double newHealthy = Math.max(0D, mong.getHealthy() - subHealthy);

        // log.info("[{}] 체력 {} 감소", event.getMongId(), mong.getHealthy() - newHealthy);

        mongRepository.save(mong.toBuilder()
                .healthy(newHealthy)
                .build());

        return newHealthy;
    }

    @Transactional
    public void decreaseSleep(MongEvent event) throws RuntimeException {
        Mong mong = mongRepository.findById(event.getMongId())
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        MongEventCode code = event.getEventCode();
        long seconds = Math.min(code.getExpiration(), Duration.between(event.getCreatedAt(), LocalDateTime.now()).getSeconds());
        double subSleep = code.getValue() / code.getExpiration() * seconds;
        double newSleep = Math.max(0D, mong.getSleep() - subSleep);

        // log.info("[{}] 피로도 {} 감소", event.getMongId(), mong.getSleep() - newSleep);

        mongRepository.save(mong.toBuilder()
                .sleep(newSleep)
                .build());
    }

    @Transactional
    public void increaseSleep(MongEvent event) throws RuntimeException {
        Mong mong = mongRepository.findById(event.getMongId())
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        MongEventCode code = event.getEventCode();
        long seconds = Math.min(code.getExpiration(), Duration.between(event.getCreatedAt(), LocalDateTime.now()).getSeconds());
        double addSleep = code.getValue() / code.getExpiration() * seconds;
        double newSleep = Math.min(SLEEP_MAX, mong.getSleep() + addSleep);

        // log.info("[{}] 피로도 {} 증가", event.getMongId(), newSleep - mong.getSleep());

        mongRepository.save(mong.toBuilder()
                .sleep(newSleep)
                .build());
    }

    @Transactional
    public void increasePayPoint(MongEvent event) throws RuntimeException {
        Mong mong = mongRepository.findById(event.getMongId())
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        int addPayPoint = event.getEventCode().getValue().intValue();
        int newPayPoint = mong.getPayPoint() + addPayPoint;

        // log.info("[{}] 페이포인트 {} 증가", event.getMongId(), addPayPoint);

        mongRepository.save(mong.toBuilder()
                .payPoint(newPayPoint)
                .build());
    }

    @Transactional
    public void increasePoop(MongEvent event) throws RuntimeException {
        Mong mong = mongRepository.findById(event.getMongId())
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        int addPoop = event.getEventCode().getValue().intValue();
        int newPoop = Math.min(POOP_MAX, mong.getNumberOfPoop() + addPoop);

        if (newPoop == POOP_MAX) {
            int newPenalty = mong.getPenalty() + 1;
            mongRepository.save(mong.toBuilder()
                    .penalty(newPenalty)
                    .build());

            // log.info("[{}] 똥 {} 개 도달 : 패널티 1 증가 ({})", event.getMongId(), POOP_MAX, newPenalty);
        } else {
            mongRepository.save(mong.toBuilder()
                    .numberOfPoop(newPoop)
                    .build());

            // log.info("[{}] 똥 {} 개 생성", event.getMongId(), newPoop - mong.getNumberOfPoop());
        }
    }

    @Transactional
    public void dead(MongEvent event) throws RuntimeException {
        Mong mong = mongRepository.findById(event.getMongId())
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        if (!mong.getIsActive()) {
            log.info("[{}] 이미 사망한 몽 ({})", event.getEventId(), mong.getName());
            return;
        }

        mongRepository.save(mong.toBuilder()
                .isActive(false)
                .numberOfPoop(0)
                .healthy(event.getEventCode().getValue())
                .satiety(event.getEventCode().getValue())
                .sleep(event.getEventCode().getValue())
                .strength(event.getEventCode().getValue())
                .weight(event.getEventCode().getValue())
                .shift(MongShift.DIE)
                .state(MongState.EMPTY)
                .build());

        log.info("[{}] 몽 사망 ({})", event.getEventId(), mong.getName());
    }
}
