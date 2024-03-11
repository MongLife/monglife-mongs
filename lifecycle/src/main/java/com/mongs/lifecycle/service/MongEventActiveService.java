package com.mongs.lifecycle.service;

import com.mongs.lifecycle.entity.Mong;
import com.mongs.lifecycle.entity.MongEvent;
import com.mongs.lifecycle.repository.MongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongEventActiveService {

    private final MongRepository mongRepository;

    private Mong findMongById(Long mongId) throws Exception {
        return mongRepository.findById(mongId).orElseThrow(Exception::new);
    }

    @Transactional
    public void weightDownEvent(MongEvent event) throws Exception {
        log.info("[weightDownEvent] mongEvent: {}", event);
        Mong mong = findMongById(event.getMongId());
        double newWeight = mong.getWeight() - event.getEventCode().getValue();        ;
        mongRepository.save(mong.toBuilder()
                .weight(newWeight)
                .build());
    }

    @Transactional
    public void strengthDownEvent(MongEvent event) throws Exception {
        log.info("[strengthDownEvent] mongEvent: {}", event);
        Mong mong = findMongById(event.getMongId());
        double newStrength = mong.getStrength() - event.getEventCode().getValue();
        mongRepository.save(mong.toBuilder()
                .strength(newStrength)
                .build());
    }

    @Transactional
    public void satietyDownEvent(MongEvent event) throws Exception {
        log.info("[satietyDownEvent] mongEvent: {}", event);
        Mong mong = findMongById(event.getMongId());
        double newSatiety = mong.getSatiety() - event.getEventCode().getValue();
        mongRepository.save(mong.toBuilder()
                .satiety(newSatiety)
                .build());
    }

    @Transactional
    public void healthyDownEvent(MongEvent event) throws Exception {
        log.info("[healthyDownEvent] mongEvent: {}", event);
        Mong mong = findMongById(event.getMongId());
        double newHealthy = mong.getHealthy() - event.getEventCode().getValue();
        mongRepository.save(mong.toBuilder()
                .healthy(newHealthy)
                .build());
    }

    @Transactional
    public void sleepDownEvent(MongEvent event) throws Exception {
        log.info("[sleepDownEvent] mongEvent: {}", event);
        Mong mong = findMongById(event.getMongId());
        double newSleep = mong.getSleep() - event.getEventCode().getValue();
        mongRepository.save(mong.toBuilder()
                .sleep(newSleep)
                .build());
    }

    @Transactional
    public void sleepUpEvent(MongEvent event) throws Exception {
        log.info("[sleepUpEvent] mongEvent: {}", event);
        Mong mong = findMongById(event.getMongId());
        double newSleep = mong.getSleep() + event.getEventCode().getValue();
        mongRepository.save(mong.toBuilder()
                .sleep(newSleep)
                .build());
    }

    @Transactional
    public void payPointUpEvent(MongEvent event) throws Exception {
        log.info("[payPointUpEvent] mongEvent: {}", event);
        Mong mong = findMongById(event.getMongId());
        int newPayPoint = mong.getPayPoint() + event.getEventCode().getValue();
        mongRepository.save(mong.toBuilder()
                .payPoint(newPayPoint)
                .build());
    }

    @Transactional
    public void generatePoopEvent(MongEvent event) throws Exception {
        log.info("[generatePoopEvent] mongEvent: {}", event);
        Mong mong = findMongById(event.getMongId());
        int newPoop = mong.getNumberOfPoop() + event.getEventCode().getValue();
        mongRepository.save(mong.toBuilder()
                .numberOfPoop(newPoop)
                .build());
    }
}
