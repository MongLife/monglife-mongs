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
        try {
            var res = managementWorkerClient.zeroEvolutionSchedule(EvolutionScheduleReqDto.builder()
                    .mongId(mongId)
                    .build());

            return res.getBody();

        } catch (Exception e) {
            return null;
        }
    }

    public EvolutionScheduleResDto firstEvolutionSchedule(Long mongId) {
        try {
            var res = managementWorkerClient.firstEvolutionSchedule(EvolutionScheduleReqDto.builder()
                    .mongId(mongId)
                    .build());

            return res.getBody();

        } catch (Exception e) {
            return null;
        }
    }

    public EvolutionScheduleResDto evolutionSchedule(Long mongId) {
        try {
            var res = managementWorkerClient.evolutionSchedule(EvolutionScheduleReqDto.builder()
                    .mongId(mongId)
                    .build());

            return res.getBody();

        } catch (Exception e) {
            return null;
        }
    }

    public EvolutionScheduleResDto lastEvolutionSchedule(Long mongId) {
        try {
            var res = managementWorkerClient.lastEvolutionSchedule(EvolutionScheduleReqDto.builder()
                    .mongId(mongId)
                    .build());

            return res.getBody();

        } catch (Exception e) {
            return null;
        }
    }

    public SleepingScheduleResDto sleepingSchedule(Long mongId, Boolean isSleeping) {
        try {
            var res = managementWorkerClient.sleepingSchedule(SleepingScheduleReqDto.builder()
                    .mongId(mongId)
                    .isSleeping(isSleeping)
                    .build());

            return res.getBody();

        } catch (Exception e) {
            return null;
        }
    }

    public DeleteScheduleResDto deleteSchedule(Long mongId) {
        try {
            var res = managementWorkerClient.deleteSchedule(DeleteScheduleReqDto.builder()
                    .mongId(mongId)
                    .build());

            return res.getBody();

        } catch (Exception e) {
            return null;
        }
    }

    public DeadScheduleResDto deadHealthySchedule(Long mongId) {
        try {
            var res = managementWorkerClient.deadHealthySchedule(DeadScheduleReqDto.builder()
                    .mongId(mongId)
                    .build());

            return res.getBody();

        } catch (Exception e) {
            return null;
        }
    }


    public DeadScheduleResDto deadHealthyScheduleStop(Long mongId) {
        try {
            var res = managementWorkerClient.deadHealthyScheduleStop(DeadScheduleReqDto.builder()
                    .mongId(mongId)
                    .build());

            return res.getBody();

        } catch (Exception e) {
            return null;
        }
    }


    public DeadScheduleResDto deadSatietySchedule(Long mongId) {
        try {
            var res = managementWorkerClient.deadSatietySchedule(DeadScheduleReqDto.builder()
                    .mongId(mongId)
                    .build());

            return res.getBody();

        } catch (Exception e) {
            return null;
        }
    }


    public DeadScheduleResDto deadSatietyScheduleStop(Long mongId) {
        try {
            var res = managementWorkerClient.deadSatietyScheduleStop(DeadScheduleReqDto.builder()
                    .mongId(mongId)
                    .build());

            return res.getBody();

        } catch (Exception e) {
            return null;
        }
    }
}
