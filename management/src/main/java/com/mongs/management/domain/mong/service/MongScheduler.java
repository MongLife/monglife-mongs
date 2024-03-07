//package com.mongs.management.domain.mong.service;
//
//import com.mongs.management.domain.mong.entity.Mong;
//import com.mongs.management.domain.mong.repository.MongRepository;
//import com.mongs.management.domain.mong.service.dto.MongStatus;
//import com.mongs.core.code.enums.management.MongCollapse;
//import com.mongs.management.domain.mongEvent.MongEvent;
//import com.mongs.management.domain.mongEvent.MongEventRepository;
//import com.mongs.management.domain.mongEvent.MongEventService;
//import com.mongs.management.domain.mongEvent.dtos.EventOccurrence;
//import com.mongs.management.exception.ManagementErrorCode;
//import com.mongs.management.exception.ManagementException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class MongScheduler {
//
//    private final MongRepository mongRepository;
//    private final MongEventService mongEventService;
//    private final MongEventRepository mongEventRepository;
//
//    @Value("${schedule.values.poop-check-time}")
//    private int POOP_CHECK_MINUTES;
//
//    @Value("${schedule.values.poop-check-max-time}")
//    private int POOP_CHECK_THRESHOLD;
//
//    @Value("${schedule.values.event-check-time}")
//    private int EVENT_CHECK_WINDOW;
//
//    @Value("${schedule.values.event-check-max-time}")
//    private int EVENT_CHECK_THRESHOLD;
//
//    @Value("${schedule.values.sleep-decay-factor}")
//    private double SLEEP_DECAY_FACTOR;
//
//    @Value("${schedule.values.hunger}")
//    private String CONDITION_HUNGER;
//
//    @Value("${schedule.values.health-zero}")
//    private String CONDITION_HEALTH_ZERO;
//
//    @Value("${schedule.values.value-amount}")
//    private int value;
//
//    // 최종 접속 시간(아무런 변화 없이) 대비 스케쥴 돌리면서 똥 ++ (수면 시간 체크)
//    @Scheduled(cron = "0 10 * * * *")
//    public void lastConnectionTime() {
//        LocalDateTime now = LocalDateTime.now();
//        mongRepository.findAll().stream()
//                .filter(mong -> Duration.between(mong.getUpdatedAt(), now).toMinutes() >= POOP_CHECK_MINUTES
//                        && !mong.getIsSleeping())
//                .filter(mong -> Duration.between(mong.getUpdatedAt(), now).toMinutes() < POOP_CHECK_THRESHOLD)
//                .forEach(Mong::addPoop);
//    }
//
//
//    // 스케쥴 돌면서 15분 마다 포만감, 근력 -- , 30분 마다 수면 -- (수면시 수면 회복, 포만감 - * 0.7) ,(추후 레이지로 변경 필)
//    // 포만감 0, 체력 0 일 때 이벤트 저장
//    @Scheduled(cron = "0 30 * * * *")
//    public void timePasses() {
//        boolean minusSleep = LocalTime.now().getMinute() % 30 == 0;
//        mongRepository.findAll().forEach(mong -> {
//            int adjustedValue = mong.getIsSleeping() ? (int) (value * SLEEP_DECAY_FACTOR) : value;
//            MongStatus mongStatus = mong.mongPassedTime(minusSleep, adjustedValue);
//            mongEventService.save(EventOccurrence.builder()
//                    .mongId(mongStatus.mongId())
//                    .event(mongStatus.collapse() != null ? mongStatus.collapse().getName() : "No Collapse")
//                    .build());
//        });
//    }
//
//
//
//    // event를 계속 돌리면서, 몽 체크, (DB 부담..될 건데 어떻게 할지 고민) 0 초과면 isEnough에 true로 변경
//    @Scheduled(cron = "0 5 * * * *")
//    public void eventCheck() {
//        LocalDateTime now = LocalDateTime.now();
//        mongEventRepository.findAll().stream()
//                .filter(mongEvent -> Duration.between(mongEvent.getCreatedAt(), now).toMinutes() < EVENT_CHECK_THRESHOLD)
//                .forEach(mongEvent -> processEvent(mongEvent, now));
//    }
//
//    private void processEvent(MongEvent mongEvent, LocalDateTime now) {
//        Mong mong = mongRepository.findById(mongEvent.getMongId())
//                .orElseThrow(() -> new ManagementException(ManagementErrorCode.NOT_FOUND));
//        MongCollapse collapse = mongEvent.getCollapse();
//
//        boolean isWindowPassed = Duration.between(mongEvent.getCreatedAt(), now).toMinutes() >= EVENT_CHECK_WINDOW;
//        if (!mongEvent.isEnough() && isWindowPassed) {
//            if (CONDITION_HUNGER.equals(collapse.getName()) && mong.getSatiety() == 0 ||
//                    CONDITION_HEALTH_ZERO.equals(collapse.getName()) && mong.getHealthy() == 0) {
//                mong.shiftToDeath();
//            } else {
//                mongEvent.setEnough(true);
//            }
//        }
//    }
//}
