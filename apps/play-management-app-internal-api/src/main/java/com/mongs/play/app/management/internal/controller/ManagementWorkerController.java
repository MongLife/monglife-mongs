package com.mongs.play.app.management.internal.controller;

import com.mongs.play.app.management.internal.dto.req.*;
import com.mongs.play.app.management.internal.dto.res.*;
import com.mongs.play.app.management.internal.service.ManagementWorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/worker/management")
@RequiredArgsConstructor
@RestController
public class ManagementWorkerController {

    private final ManagementWorkerService managementWorkerService;

    @PostMapping("/zeroEvolution")
    public ResponseEntity<EvolutionScheduleResDto> zeroEvolutionSchedule(@RequestBody EvolutionScheduleReqDto evolutionScheduleReqDto) {
        Long mongId = evolutionScheduleReqDto.mongId();

        log.info("[zeroEvolution] mongId: {}", mongId);

        managementWorkerService.zeroEvolution(mongId);

        return ResponseEntity.ok().body(EvolutionScheduleResDto.builder()
                .mongId(mongId)
                .build());
    }

    @PostMapping("/firstEvolution")
    public ResponseEntity<EvolutionScheduleResDto> firstEvolutionSchedule(@RequestBody EvolutionScheduleReqDto evolutionScheduleReqDto) {
        Long mongId = evolutionScheduleReqDto.mongId();

        log.info("[firstEvolution] mongId: {}", mongId);

        managementWorkerService.firstEvolution(mongId);

        return ResponseEntity.ok().body(EvolutionScheduleResDto.builder()
                .mongId(mongId)
                .build());
    }

    @PostMapping("/evolution")
    public ResponseEntity<EvolutionScheduleResDto> evolutionSchedule(@RequestBody EvolutionScheduleReqDto evolutionScheduleReqDto) {
        Long mongId = evolutionScheduleReqDto.mongId();

        log.info("[evolution] mongId: {}", mongId);

        managementWorkerService.evolution(mongId);

        return ResponseEntity.ok().body(EvolutionScheduleResDto.builder()
                .mongId(mongId)
                .build());
    }

    @PostMapping("/lastEvolution")
    public ResponseEntity<EvolutionScheduleResDto> lastEvolutionSchedule(@RequestBody EvolutionScheduleReqDto evolutionScheduleReqDto) {
        Long mongId = evolutionScheduleReqDto.mongId();

        log.info("[lastEvolution] mongId: {}", mongId);

        managementWorkerService.lastEvolution(mongId);

        return ResponseEntity.ok().body(EvolutionScheduleResDto.builder()
                .mongId(mongId)
                .build());
    }

    @PostMapping("/sleeping")
    public ResponseEntity<SleepingScheduleResDto> sleepingSchedule(@RequestBody SleepingScheduleReqDto sleepingScheduleReqDto) {
        Long mongId = sleepingScheduleReqDto.mongId();
        Boolean isSleeping = sleepingScheduleReqDto.isSleeping();

        log.info("[sleepSleeping] mongId: {}, isSleeping: {}", mongId, isSleeping);

        if (isSleeping) {
            managementWorkerService.sleepSleeping(mongId);
        } else {
            managementWorkerService.awakeSleeping(mongId);
        }

        return ResponseEntity.ok().body(SleepingScheduleResDto.builder()
                .mongId(mongId)
                .build());
    }

    @PostMapping("/delete")
    public ResponseEntity<DeleteScheduleResDto> deleteSchedule(@RequestBody DeleteScheduleReqDto deleteScheduleReqDto) {
        Long mongId = deleteScheduleReqDto.mongId();

        log.info("[delete] mongId: {}", mongId);

        managementWorkerService.delete(mongId);

        return ResponseEntity.ok().body(DeleteScheduleResDto.builder()
                .mongId(mongId)
                .build());
    }

    @PostMapping("/dead")
    public ResponseEntity<DeadScheduleResDto> deadSchedule(@RequestBody DeadScheduleReqDto deadScheduleReqDto) {
        Long mongId = deadScheduleReqDto.mongId();

        log.info("[dead] mongId: {}", mongId);

        managementWorkerService.dead(mongId);

        return ResponseEntity.ok().body(DeadScheduleResDto.builder()
                .mongId(mongId)
                .build());
    }

    @DeleteMapping("/dead")
    public ResponseEntity<DeadScheduleResDto> deadScheduleStop(@RequestBody DeadScheduleReqDto deadScheduleReqDto) {
        Long mongId = deadScheduleReqDto.mongId();

        log.info("[deadStop] mongId: {}", mongId);

        managementWorkerService.live(mongId);

        return ResponseEntity.ok().body(DeadScheduleResDto.builder()
                .mongId(mongId)
                .build());
    }
}
