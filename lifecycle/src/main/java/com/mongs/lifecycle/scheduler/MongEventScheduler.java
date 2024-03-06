package com.mongs.lifecycle.scheduler;

import com.mongs.lifecycle.code.MongEventCode;
import com.mongs.lifecycle.entity.Mong;
import com.mongs.lifecycle.entity.MongEvent;
import com.mongs.lifecycle.repository.MongEventRepository;
import com.mongs.lifecycle.repository.MongRepository;
import com.mongs.lifecycle.service.MongEventActiveService;
import com.mongs.lifecycle.service.MongEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MongEventScheduler {

    private final MongEventService mongEventService;
    private final MongEventActiveService mongEventActiveService;
    private final MongEventRepository mongEventRepository;

    @Autowired
    private MongEventScheduler(
            MongEventService mongEventService,
            MongEventActiveService mongEventActiveService,
            MongEventRepository mongEventRepository,
            MongRepository mongRepository
    ) {
        this.mongEventService = mongEventService;
        this.mongEventActiveService = mongEventActiveService;
        this.mongEventRepository = mongEventRepository;

        mongEventRepository.deleteAll();
        for (long mongId = 1; mongId <= 1000; mongId++) {
            mongRepository.save(Mong.builder()
                    .accountId(1L)
                    .name("(" + mongId + ") 테스트 몽")
                    .build());
        }
    }

    @Scheduled(fixedDelay = 100)
    public void readAndActiveEvent() {
        mongEventRepository.findTopByExpiredAtBeforeOrderByCreatedAt(LocalDateTime.now())
                .ifPresent(mongEvent -> {
                    try {
                        this.updateMong(mongEvent);
                        mongEventRepository.deleteById(mongEvent.getEventId());
                    } catch (Exception e) {
                        log.error("[Active Event Fail] errorMessage: {}, mongEvent: {}", e, mongEvent);
                    }
                });
//        mongEventRepository.findByExpiredAtBeforeOrderByCreatedAt(LocalDateTime.now())
//                .forEach(mongEvent -> {
//                    try {
//                        this.updateMong(mongEvent);
//                        mongEventRepository.deleteById(mongEvent.getEventId());
//                    } catch (Exception e) {
//                        log.error("[Active Event Fail] errorMessage: {}, mongEvent: {}", e, mongEvent);
//                    }
//                });
    }

    private void updateMong(MongEvent event) throws RuntimeException {
        switch (event.getEventCode()) {
            case WEIGHT_DOWN -> {
                mongEventActiveService.weightDownEvent(event);
                mongEventService.registerMongEvent(event.getMongId(), MongEventCode.WEIGHT_DOWN);
            }
            case STRENGTH_DOWN -> {
                mongEventActiveService.strengthDownEvent(event);
                mongEventService.registerMongEvent(event.getMongId(), MongEventCode.STRENGTH_DOWN);
            }
            case SATIETY_DOWN -> {
                mongEventActiveService.satietyDownEvent(event);
                mongEventService.registerMongEvent(event.getMongId(), MongEventCode.SATIETY_DOWN);
            }
            case HEALTHY_DOWN -> {
                mongEventActiveService.healthyDownEvent(event);
                mongEventService.registerMongEvent(event.getMongId(), MongEventCode.HEALTHY_DOWN);
            }
            case SLEEP_DOWN -> {
                mongEventActiveService.sleepDownEvent(event);
                mongEventService.registerMongEvent(event.getMongId(), MongEventCode.SLEEP_DOWN);
            }
            case SLEEP_UP -> {
                mongEventActiveService.sleepUpEvent(event);
                mongEventService.registerMongEvent(event.getMongId(), MongEventCode.SLEEP_UP);
            }
            case PAY_POINT_UP -> {
                mongEventActiveService.payPointUpEvent(event);
                mongEventService.registerMongEvent(event.getMongId(), MongEventCode.PAY_POINT_UP);
            }
            case POOP -> {
                mongEventActiveService.generatePoopEvent(event);
                mongEventService.registerMongEvent(event.getMongId(), MongEventCode.POOP);
            }
            case DEAD, ADMIN_DEAD -> {
                mongEventActiveService.deadEvent(event);
            }
            default -> throw new RuntimeException();
        }
    }
}
