package com.mongs.play.module.feign.client;

import com.mongs.play.module.feign.dto.req.*;
import com.mongs.play.module.feign.dto.res.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "MANAGEMENT-WORKER")
public interface ManagementWorkerClient {
    @PostMapping("/worker/management/zeroEvolution")
    ResponseEntity<EvolutionScheduleResDto> zeroEvolutionSchedule(@RequestBody EvolutionScheduleReqDto evolutionScheduleReqDto);
    @PostMapping("/worker/management/firstEvolution")
    ResponseEntity<EvolutionScheduleResDto> firstEvolutionSchedule(@RequestBody EvolutionScheduleReqDto evolutionScheduleReqDto);
    @PostMapping("/worker/management/evolution")
    ResponseEntity<EvolutionScheduleResDto> evolutionSchedule(@RequestBody EvolutionScheduleReqDto evolutionScheduleReqDto);
    @PostMapping("/worker/management/lastEvolution")
    ResponseEntity<EvolutionScheduleResDto> lastEvolutionSchedule(@RequestBody EvolutionScheduleReqDto evolutionScheduleReqDto);
    @PostMapping("/worker/management/sleeping")
    ResponseEntity<SleepingScheduleResDto> sleepingSchedule(SleepingScheduleReqDto sleepingScheduleReqDto);
    @PostMapping("/worker/management/delete")
    ResponseEntity<DeleteScheduleResDto> deleteSchedule(DeleteScheduleReqDto deleteScheduleReqDto);
    @PostMapping("/worker/management/dead/healthy")
    ResponseEntity<DeadScheduleResDto> deadHealthySchedule(DeadScheduleReqDto deadScheduleReqDto);
    @DeleteMapping("/worker/management/dead/healthy")
    ResponseEntity<DeadScheduleResDto> deadHealthyScheduleStop(DeadScheduleReqDto deadScheduleReqDto);
    @PostMapping("/worker/management/dead/satiety")
    ResponseEntity<DeadScheduleResDto> deadSatietySchedule(DeadScheduleReqDto deadScheduleReqDto);
    @DeleteMapping("/worker/management/dead/satiety")
    ResponseEntity<DeadScheduleResDto> deadSatietyScheduleStop(DeadScheduleReqDto deadScheduleReqDto);
}
