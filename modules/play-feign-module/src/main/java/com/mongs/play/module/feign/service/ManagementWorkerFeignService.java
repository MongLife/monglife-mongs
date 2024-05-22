package com.mongs.play.module.feign.service;

import com.mongs.play.module.feign.client.ManagementWorkerClient;
import com.mongs.play.module.feign.dto.req.*;
import com.mongs.play.module.feign.dto.res.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementWorkerFeignService {

    private final ManagementWorkerClient managementWorkerClient;

    public EvolutionScheduleResDto zeroEvolutionSchedule(Long mongId) {
        var res = managementWorkerClient.zeroEvolutionSchedule(EvolutionScheduleReqDto.builder()
                .mongId(mongId)
                .build());

        return res.getBody();
    }

    public EvolutionScheduleResDto firstEvolutionSchedule(Long mongId) {
        var res = managementWorkerClient.firstEvolutionSchedule(EvolutionScheduleReqDto.builder()
                .mongId(mongId)
                .build());

        return res.getBody();
    }

    public EvolutionScheduleResDto evolutionSchedule(Long mongId) {
        var res = managementWorkerClient.evolutionSchedule(EvolutionScheduleReqDto.builder()
                .mongId(mongId)
                .build());

        return res.getBody();
    }

    public EvolutionScheduleResDto lastEvolutionSchedule(Long mongId) {
        var res = managementWorkerClient.lastEvolutionSchedule(EvolutionScheduleReqDto.builder()
                .mongId(mongId)
                .build());

        return res.getBody();
    }

    public SleepingScheduleResDto sleepingSchedule(Long mongId, Boolean isSleeping) {
        var res = managementWorkerClient.sleepingSchedule(SleepingScheduleReqDto.builder()
                .mongId(mongId)
                .isSleeping(isSleeping)
                .build());

        return res.getBody();
    }

    public DeleteScheduleResDto deleteSchedule(Long mongId) {
        var res = managementWorkerClient.deleteSchedule(DeleteScheduleReqDto.builder()
                .mongId(mongId)
                .build());

        return res.getBody();
    }

    public DeadScheduleResDto deadHealthySchedule(Long mongId) {
        var res = managementWorkerClient.deadHealthySchedule(DeadScheduleReqDto.builder()
                .mongId(mongId)
                .build());

        return res.getBody();
    }


    public DeadScheduleResDto deadHealthyScheduleStop(Long mongId) {
        var res = managementWorkerClient.deadHealthyScheduleStop(DeadScheduleReqDto.builder()
                .mongId(mongId)
                .build());

        return res.getBody();
    }


    public DeadScheduleResDto deadSatietySchedule(Long mongId) {
        var res = managementWorkerClient.deadSatietySchedule(DeadScheduleReqDto.builder()
                .mongId(mongId)
                .build());

        return res.getBody();
    }


    public DeadScheduleResDto deadSatietyScheduleStop(Long mongId) {
        var res = managementWorkerClient.deadSatietyScheduleStop(DeadScheduleReqDto.builder()
                .mongId(mongId)
                .build());

        return res.getBody();
    }
}
