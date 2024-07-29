package com.mongs.play.module.feign.service;

import com.mongs.play.module.feign.client.ManagementInternalClient;
import com.mongs.play.module.feign.dto.req.*;
import com.mongs.play.module.feign.dto.res.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementWorkerFeignService {

    private final ManagementInternalClient managementInternalClient;

    public EvolutionScheduleResDto zeroEvolutionSchedule(Long mongId) {
        var res = managementInternalClient.zeroEvolutionSchedule(EvolutionScheduleReqDto.builder()
                .mongId(mongId)
                .build());

        return res.getBody();
    }

    public EvolutionScheduleResDto firstEvolutionSchedule(Long mongId) {
        var res = managementInternalClient.firstEvolutionSchedule(EvolutionScheduleReqDto.builder()
                .mongId(mongId)
                .build());

        return res.getBody();
    }

    public EvolutionScheduleResDto evolutionSchedule(Long mongId) {
        var res = managementInternalClient.evolutionSchedule(EvolutionScheduleReqDto.builder()
                .mongId(mongId)
                .build());

        return res.getBody();
    }

    public EvolutionScheduleResDto lastEvolutionSchedule(Long mongId) {
        var res = managementInternalClient.lastEvolutionSchedule(EvolutionScheduleReqDto.builder()
                .mongId(mongId)
                .build());

        return res.getBody();
    }

    public SleepingScheduleResDto sleepingSchedule(Long mongId, Boolean isSleeping) {
        var res = managementInternalClient.sleepingSchedule(SleepingScheduleReqDto.builder()
                .mongId(mongId)
                .isSleeping(isSleeping)
                .build());

        return res.getBody();
    }

    public DeleteScheduleResDto deleteSchedule(Long mongId) {
        var res = managementInternalClient.deleteSchedule(DeleteScheduleReqDto.builder()
                .mongId(mongId)
                .build());

        return res.getBody();
    }

    public DeadScheduleResDto deadSchedule(Long mongId) {
        var res = managementInternalClient.deadSchedule(DeadScheduleReqDto.builder()
                .mongId(mongId)
                .build());

        return res.getBody();
    }

    public DeadScheduleResDto deadScheduleStop(Long mongId) {
        var res = managementInternalClient.deadScheduleStop(DeadScheduleReqDto.builder()
                .mongId(mongId)
                .build());

        return res.getBody();
    }
}
