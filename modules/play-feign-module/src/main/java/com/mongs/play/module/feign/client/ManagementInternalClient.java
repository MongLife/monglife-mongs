package com.mongs.play.module.feign.client;

import com.mongs.play.config.FeignClientConfig;
import com.mongs.play.config.FeignErrorDecoder;
import com.mongs.play.module.feign.dto.req.*;
import com.mongs.play.module.feign.dto.res.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "MANAGEMENT-INTERNAL", configuration = FeignClientConfig.class)
public interface ManagementInternalClient {
    @PutMapping("/internal/management/evolutionReady")
    ResponseEntity<EvolutionReadyResDto> evolutionReady(@RequestBody EvolutionReadyReqDto evolutionReadyReqDto);
    @PutMapping("/internal/management/decreaseStatus")
    ResponseEntity<DecreaseStatusResDto> decreaseStatus(@RequestBody DecreaseStatusReqDto decreaseStatusReqDto);
    @PutMapping("/internal/management/increaseStatus")
    ResponseEntity<IncreaseStatusResDto> increaseStatus(@RequestBody IncreaseStatusReqDto increaseStatusReqDto);
    @PutMapping("/internal/management/increasePoopCount")
    ResponseEntity<IncreasePoopCountResDto> increasePoopCount(@RequestBody IncreasePoopCountReqDto increasePoopCountReqDto);
    @PutMapping("/internal/management/dead")
    ResponseEntity<DeadResDto> dead(@RequestBody DeadReqDto deadReqDto);
    @PutMapping("/internal/management/increasePayPoint")
    ResponseEntity<IncreasePayPointResDto> increasePayPoint(@RequestBody IncreasePayPointReqDto increasePayPointReqDto);

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
