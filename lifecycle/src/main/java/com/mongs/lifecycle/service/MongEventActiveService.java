package com.mongs.lifecycle.service;

import com.mongs.core.code.enums.management.MongShift;
import com.mongs.core.code.enums.management.MongState;
import com.mongs.lifecycle.code.MongEventCode;
import com.mongs.lifecycle.entity.Mong;
import com.mongs.lifecycle.entity.MongEvent;
import com.mongs.lifecycle.repository.MongEventRepository;
import com.mongs.lifecycle.repository.MongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongEventActiveService {

    private final MongRepository mongRepository;

    private final Double WEIGHT_MAX = 100D;
    private final Double STRENGTH_MAX = 100D;
    private final Double SATIETY_MAX = 100D;
    private final Double HEALTHY_MAX = 100D;
    private final Double SLEEP_MAX = 100D;
    private final Integer POOP_MAX = 4;

    private Mong findMongById(Long mongId) throws RuntimeException {
        return mongRepository.findById(mongId).orElseThrow(RuntimeException::new);
    }

    @Transactional
    public void weightDownEvent(MongEvent event) throws RuntimeException {
        Mong mong = findMongById(event.getMongId());

        MongEventCode code = event.getEventCode();
        long seconds = Math.min(code.getExpiration(), Duration.between(event.getCreatedAt(), LocalDateTime.now()).getSeconds());
        double subWeight = code.getValue() / code.getExpiration() * seconds;
        double newWeight = Math.max(0D, mong.getWeight() - subWeight);

        log.info("[weightDownEvent] 몸무게 {} 감소, mongEvent: {}", mong.getWeight() - newWeight, event);

        mongRepository.save(mong.toBuilder()
                .weight(newWeight)
                .build());
    }

    @Transactional
    public void strengthDownEvent(MongEvent event) throws RuntimeException {
        Mong mong = findMongById(event.getMongId());

        MongEventCode code = event.getEventCode();
        long seconds = Math.min(code.getExpiration(), Duration.between(event.getCreatedAt(), LocalDateTime.now()).getSeconds());
        double subStrength = code.getValue() / code.getExpiration() * seconds;
        double newStrength = Math.max(0D, mong.getStrength() - subStrength);

        log.info("[strengthDownEvent] 근력 {} 감소, mongEvent: {}", mong.getStrength() - newStrength, event);

        mongRepository.save(mong.toBuilder()
                .strength(newStrength)
                .build());
    }

    @Transactional
    public void satietyDownEvent(MongEvent event) throws RuntimeException {
        Mong mong = findMongById(event.getMongId());

        MongEventCode code = event.getEventCode();
        long seconds = Math.min(code.getExpiration(), Duration.between(event.getCreatedAt(), LocalDateTime.now()).getSeconds());
        double subSatiety = code.getValue() / code.getExpiration() * seconds;
        double newSatiety = Math.max(0D, mong.getSatiety() - subSatiety);

        log.info("[satietyDownEvent] 포만감 {} 감소, mongEvent: {}", mong.getSatiety() - newSatiety, event);

        mongRepository.save(mong.toBuilder()
                .satiety(newSatiety)
                .build());
    }

    @Transactional
    public void healthyDownEvent(MongEvent event) throws RuntimeException {
        Mong mong = findMongById(event.getMongId());

        MongEventCode code = event.getEventCode();
        long seconds = Math.min(code.getExpiration(), Duration.between(event.getCreatedAt(), LocalDateTime.now()).getSeconds());
        double subHealthy = code.getValue() / code.getExpiration() * seconds;
        double newHealthy = Math.max(0D, mong.getHealthy() - subHealthy);

        log.info("[healthyDownEvent] 체력 {} 감소, mongEvent: {}", mong.getHealthy() - newHealthy, event);

        mongRepository.save(mong.toBuilder()
                .healthy(newHealthy)
                .build());
    }

    @Transactional
    public void sleepDownEvent(MongEvent event) throws RuntimeException {
        Mong mong = findMongById(event.getMongId());

        MongEventCode code = event.getEventCode();
        long seconds = Math.min(code.getExpiration(), Duration.between(event.getCreatedAt(), LocalDateTime.now()).getSeconds());
        double subSleep = code.getValue() / code.getExpiration() * seconds;
        double newSleep = Math.max(0D, mong.getSleep() - subSleep);

        log.info("[sleepDownEvent] 피로도 {} 감소, mongEvent: {}", mong.getSleep() - newSleep, event);

        mongRepository.save(mong.toBuilder()
                .sleep(newSleep)
                .build());
    }

    @Transactional
    public void sleepUpEvent(MongEvent event) throws RuntimeException {
        Mong mong = findMongById(event.getMongId());

        MongEventCode code = event.getEventCode();
        long seconds = Math.min(code.getExpiration(), Duration.between(event.getCreatedAt(), LocalDateTime.now()).getSeconds());
        double addSleep = code.getValue() / code.getExpiration() * seconds;
        double newSleep = Math.min(SLEEP_MAX, mong.getSleep() + addSleep);

        log.info("[sleepUpEvent] 피로도 {} 증가, mongEvent: {}", newSleep - mong.getSleep(), event);

        mongRepository.save(mong.toBuilder()
                .sleep(newSleep)
                .build());
    }

    @Transactional
    public void payPointUpEvent(MongEvent event) throws RuntimeException {
        Mong mong = findMongById(event.getMongId());

        int addPayPoint = event.getEventCode().getValue().intValue();
        int newPayPoint = mong.getPayPoint() + addPayPoint;

        log.info("[payPointUpEvent] 페이포인트 {} 증가, mongEvent: {}", addPayPoint, event);

        mongRepository.save(mong.toBuilder()
                .payPoint(newPayPoint)
                .build());
    }

    @Transactional
    public void generatePoopEvent(MongEvent event) throws RuntimeException {
        Mong mong = findMongById(event.getMongId());

        int addPoop = event.getEventCode().getValue().intValue();
        int newPoop = Math.min(POOP_MAX, mong.getNumberOfPoop() + addPoop);

        log.info("[generatePoopEvent] 똥 {}개 생성 mongEvent: {}", newPoop - mong.getNumberOfPoop(), event);

        mongRepository.save(mong.toBuilder()
                .numberOfPoop(newPoop)
                .build());
    }

    @Transactional
    public void deadEvent(MongEvent event) throws RuntimeException {
        Mong mong = findMongById(event.getMongId());

        log.info("[deadEvent] {} : {} 몽 사망 mongEvent: {}", mong.getName(), mong.getId(), event);

        mongRepository.save(mong.toBuilder()
                .isActive(false)
                .shift(MongShift.DIE)
                .state(MongState.EMPTY)
                .build());
    }
}
