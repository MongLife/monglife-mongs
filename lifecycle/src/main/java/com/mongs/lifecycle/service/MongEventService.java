package com.mongs.lifecycle.service;

import com.mongs.lifecycle.code.MongEventCode;
import com.mongs.lifecycle.entity.MongEvent;
import com.mongs.lifecycle.repository.MongEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongEventService {

    private final MongEventActiveService mongEventActiveService;
    private final MongEventRepository mongEventRepository;

    private final Integer MIN_POOP_EXPIRATION = 5;//60 * 60 * 4;

    private String generateEventId() {
        return UUID.randomUUID().toString();
    }

    private Long getExpiration(MongEventCode mongEventCode) {
        long expiration = mongEventCode.getExpiration();

        if (MongEventCode.POOP.equals(mongEventCode)) {
            expiration = (int) (Math.random() * (expiration - MIN_POOP_EXPIRATION + 1) + MIN_POOP_EXPIRATION);
        }

        return expiration;
    }

    @Transactional
    public void registerMongEvent(Long mongId, MongEventCode mongEventCode) throws RuntimeException {
        long expiration = getExpiration(mongEventCode);

        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiredAt = createdAt.plusSeconds(expiration);

        mongEventRepository.save(MongEvent.builder()
                .eventId(generateEventId())
                .mongId(mongId)
                .eventCode(mongEventCode)
                .expiredAt(expiredAt)
                .createdAt(createdAt)
                .build());
    }

    @Transactional
    public void removeMongEvent(Long mongId, MongEventCode mongEventCode) throws RuntimeException {
        mongEventRepository.findByMongIdAndEventCode(mongId, mongEventCode)
                .ifPresent(event -> {
                    this.updateMong(event);
                    mongEventRepository.deleteByMongIdAndEventCode(mongId, mongEventCode);
                });
    }

    @Transactional
    public void removeAllMongEvent(Long mongId) throws RuntimeException {
        mongEventRepository.findByMongId(mongId)
                .forEach(event -> {
                    this.updateMong(event);
                    mongEventRepository.deleteByMongIdAndEventCode(mongId, event.getEventCode());
                });
    }

    private void updateMong(MongEvent event) throws RuntimeException {
        switch (event.getEventCode()) {
            case WEIGHT_DOWN -> mongEventActiveService.weightDownEvent(event);
            case STRENGTH_DOWN -> mongEventActiveService.strengthDownEvent(event);
            case SATIETY_DOWN -> mongEventActiveService.satietyDownEvent(event);
            case HEALTHY_DOWN -> mongEventActiveService.healthyDownEvent(event);
            case SLEEP_DOWN -> mongEventActiveService.sleepDownEvent(event);
            case SLEEP_UP -> mongEventActiveService.sleepUpEvent(event);
            case PAY_POINT_UP -> mongEventActiveService.payPointUpEvent(event);
            case POOP -> mongEventActiveService.generatePoopEvent(event);
            case DEAD, ADMIN_DEAD -> mongEventActiveService.deadEvent(event);
            default -> throw new RuntimeException();
        }
    }
}
