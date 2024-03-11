package com.mongs.lifecycle.scheduler;

import com.mongs.lifecycle.entity.MongEvent;
import com.mongs.lifecycle.repository.MongEventRepository;
import com.mongs.lifecycle.service.MongEventActiveService;
import com.mongs.lifecycle.service.MongEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class MongEventScheduler {

    private final MongEventService mongEventService;
    private final MongEventActiveService mongEventActiveService;
    private final MongEventRepository mongEventRepository;

    @Scheduled(fixedDelay = 1000)
    public void readAndActiveEvent() {
        mongEventRepository.findTopByExpiredAtBeforeOrderByCreatedAt(LocalDateTime.now())
                .ifPresent(mongEvent -> {
                    try {
                        this.updateMong(mongEvent);
                        mongEventRepository.deleteById(mongEvent.getEventId());
                    } catch (Exception e) {
                        log.error("[Active Event Fail] mongEvent: {}", mongEvent);
                    }
                });
    }

    private void updateMong(MongEvent event) throws Exception {
        switch (event.getEventCode()) {
            case WEIGHT_DOWN -> {
                mongEventActiveService.weightDownEvent(event);
                mongEventService.registerWeightDownEvent(event.getMongId());
            }
            case STRENGTH_DOWN -> {
                mongEventActiveService.strengthDownEvent(event);
                mongEventService.registerStrengthDownEvent(event.getMongId());
            }
            case SATIETY_DOWN -> {
                mongEventActiveService.satietyDownEvent(event);
                mongEventService.registerSatietyDownEvent(event.getMongId());
            }
            case HEALTHY_DOWN -> {
                mongEventActiveService.healthyDownEvent(event);
                mongEventService.registerHealthyDownEvent(event.getMongId());
            }
            case SLEEP_DOWN -> {
                mongEventActiveService.sleepDownEvent(event);
                mongEventService.registerSleepDownEvent(event.getMongId());
            }
            case SLEEP_UP -> {
                mongEventActiveService.sleepUpEvent(event);
                mongEventService.registerSleepUpEvent(event.getMongId());
            }
            case PAY_POINT_UP -> {
                mongEventActiveService.payPointUpEvent(event);
                mongEventService.registerPayPointUpEvent(event.getMongId());
            }
            case POOP -> {
                mongEventActiveService.generatePoopEvent(event);
                mongEventService.registerGeneratePoopEvent(event.getMongId());
            }
            default -> throw new Exception();
        }
    }
}
