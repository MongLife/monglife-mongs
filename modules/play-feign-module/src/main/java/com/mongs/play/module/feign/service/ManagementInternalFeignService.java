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
public class ManagementInternalFeignService {

    private final ManagementInternalClient managementInternalClient;

    public EvolutionReadyResDto evolutionReady(Long mongId) {
        try {
            var res = managementInternalClient.evolutionReady(EvolutionReadyReqDto.builder()
                    .mongId(mongId)
                    .build());

            return res.getBody();

        } catch (Exception e) {
            return null;
        }
    }

    public DecreaseStatusResDto decreaseStatus(Long mongId, Double subWeight, Double subStrength, Double subSatiety, Double subHealthy, Double subSleep) {
        try {
            var res = managementInternalClient.decreaseStatus(DecreaseStatusReqDto.builder()
                    .mongId(mongId)
                    .subWeight(subWeight)
                    .subStrength(subStrength)
                    .subSatiety(subSatiety)
                    .subHealthy(subHealthy)
                    .subSleep(subSleep)
                    .build());

            return res.getBody();

        } catch (Exception e) {
            return null;
        }
    }

    public IncreaseStatusResDto increaseStatus(Long mongId, Double addWeight, Double addStrength, Double addSatiety, Double addHealthy, Double addSleep) {
        try {
            var res = managementInternalClient.increaseStatus(IncreaseStatusReqDto.builder()
                    .mongId(mongId)
                    .addWeight(addWeight)
                    .addStrength(addStrength)
                    .addSatiety(addSatiety)
                    .addHealthy(addHealthy)
                    .addSleep(addSleep)
                    .build());

            return res.getBody();

        } catch (Exception e) {
            return null;
        }
    }

    public IncreasePoopCountResDto increasePoopCount(Long mongId, Integer addPoopCount) {
        try {
            var res = managementInternalClient.increasePoopCount(IncreasePoopCountReqDto.builder()
                    .mongId(mongId)
                    .addPoopCount(addPoopCount)
                    .build());

            return res.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public DeadResDto dead(Long mongId) {
        try {
            var res = managementInternalClient.dead(DeadReqDto.builder()
                    .mongId(mongId)
                    .build());

            return res.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public IncreasePayPointResDto increasePayPoint(Long mongId, Integer addPayPoint) {
        try {
            var res = managementInternalClient.increasePayPoint(IncreasePayPointReqDto.builder()
                    .mongId(mongId)
                    .addPayPoint(addPayPoint)
                    .build());

            return res.getBody();
        } catch (Exception e) {
            return null;
        }
    }
}
