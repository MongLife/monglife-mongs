package com.mongs.management.domain.mong.service;

import com.mongs.management.domain.mong.entity.Mong;
import com.mongs.management.domain.mong.repository.MongRepository;
import com.mongs.management.domain.mong.service.dto.MongStatus;
import com.mongs.core.code.enums.management.MongCollapse;
import com.mongs.management.domain.mongEvent.MongEventRepository;
import com.mongs.management.domain.mongEvent.MongEventService;
import com.mongs.management.domain.mongEvent.dtos.EventOccurrence;
import com.mongs.management.exception.ManagementErrorCode;
import com.mongs.management.exception.ManagementException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class MongScheduler {

    private final MongRepository mongRepository;
    private final MongEventService mongEventService;
    private final MongEventRepository mongEventRepository;

//    @Value("${passedTime.value}")
    private int value = 100;

    // 경험치 ++은 출석시, 운동, 쓰다듬 ++
    // 졸업도 경험치 올리면서 체크 --> 스케쥴러 필요 없음


    // 최종 접속 시간(아무런 변화 없이) 대비 스케쥴 돌리면서 똥 ++ (수면 시간 체크)
    @Scheduled(cron = "0 10 * * * *")
    public void lastConnectionTime() {
        LocalDateTime now = LocalDateTime.now();
        mongRepository.findAll().stream()
                .takeWhile(mong -> Duration.between(mong.getUpdatedAt(), now).toMinutes() < 70)
                .filter(mong -> Duration.between(mong.getUpdatedAt(), now).toMinutes() >= 60)
                .forEach(Mong::addPoop);
    }

    // 스케쥴 돌면서 15분 마다 포만감, 근력 -- , 30분 마다 수면 -- (수면시 수면 회복, 포만감 - * 0.7) ,(추후 레이지로 변경 필)
    // 포만감 0, 체력 0 일 때 이벤트 저장
    @Scheduled(cron = "0 30 * * * *")
    public void timePasses() {
        boolean minusSleep = LocalTime.now().getMinute() % 30 == 0;

        mongRepository.findAll().stream().forEach(mong -> {
            value = mong.getIsSleeping() ? value - 3 : value;
            MongStatus mongStatus = mong.mongPassedTime(minusSleep, value);
            mongEventService.save(EventOccurrence.builder()
                    .mongId(mongStatus.mongId())
                    .event(mongStatus.collapse() != null ? mongStatus.collapse().getName() : "No Collapse")
                    .build());
        });
    }

    // 이벤트 돌리는 스케쥴러 (5분마다)
    @Scheduled(cron = "0 5 * * * *")
    public void eventCheck() {
        LocalDateTime now = LocalDateTime.now();
        mongEventRepository.findAll().stream()
                .takeWhile(mongEvent -> Duration.between(mongEvent.getCreatedAt(), now).toMinutes() < 190)
                .filter(mongEvent -> Duration.between(mongEvent.getCreatedAt(), now).toMinutes() >= 180)
                .forEach(mongEvent -> {
                    Mong mong = mongRepository.findById(mongEvent.getMongId())
                            .orElseThrow(() -> new ManagementException(ManagementErrorCode.NOT_FOUND));
                    MongCollapse collapse = mongEvent.getCollapse();
                    if ("공복".equals(collapse.getName()) && mong.getSatiety() == 0) {
                        mong.shiftToDeath();
                    } else if ("체력0".equals(collapse.getName()) && mong.getHealthy() == 0) {
                        mong.shiftToDeath();
                    }
                });
    }

}
