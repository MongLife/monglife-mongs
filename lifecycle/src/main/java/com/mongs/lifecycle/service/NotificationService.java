package com.mongs.lifecycle.service;

import com.mongs.core.vo.mqtt.*;
import com.mongs.lifecycle.client.NotificationClient;
import com.mongs.lifecycle.entity.Mong;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationClient notificationClient;

    public void publishPoop(Mong mong) {
        try {
            notificationClient.publishPoop(MqttReqDto.builder()
                    .accountId(mong.getAccountId())
                    .data(PublishPoopVo.builder()
                            .mongId(mong.getId())
                            .poopCount(mong.getNumberOfPoop())
                            .exp(mong.getExp())
                            .build())
                    .build());
        } catch (FeignException ignored) {}
    }

    public void publishDelete(Mong mong) {
        try{
            notificationClient.publishDelete(MqttReqDto.builder()
                    .accountId(mong.getAccountId())
                    .data(PublishDeleteVo.builder()
                            .mongId(mong.getId())
                            .build())
                    .build());
        } catch (FeignException ignored) {}
    }

    public void publishEvolutionReady(Mong mong) {
        try {
            notificationClient.publishEvolutionReady(MqttReqDto.builder()
                    .accountId(mong.getAccountId())
                    .data(PublishEvolutionReadyVo.builder()
                            .mongId(mong.getId())
                            .shiftCode(mong.getShift().getCode())
                            .build())
                    .build());
        } catch (FeignException ignored) {}
    }

    public void publishSleep(Mong mong) {
        try {
            notificationClient.publishSleep(MqttReqDto.builder()
                    .accountId(mong.getAccountId())
                    .data(PublishSleepVo.builder()
                            .mongId(mong.getId())
                            .sleep(mong.getSleep())
                            .build())
                    .build());
        } catch (FeignException ignored) {}
    }

    public void publishHealthy(Mong mong) {
        try {
            notificationClient.publishHealthy(MqttReqDto.builder()
                    .accountId(mong.getAccountId())
                    .data(PublishHealthyVo.builder()
                            .mongId(mong.getId())
                            .health(mong.getHealthy())
                            .build())
                    .build());
        } catch (FeignException ignored) {}
    }

    public void publishSatiety(Mong mong) {
        try {
            notificationClient.publishSatiety(MqttReqDto.builder()
                    .accountId(mong.getAccountId())
                    .data(PublishSatietyVo.builder()
                            .mongId(mong.getId())
                            .satiety(mong.getSatiety())
                            .build())
                    .build());
        } catch (FeignException ignored) {}
    }

    public void publishStrength(Mong mong) {
        try {
            notificationClient.publishStrength(MqttReqDto.builder()
                    .accountId(mong.getAccountId())
                    .data(PublishStrengthVo.builder()
                            .mongId(mong.getId())
                            .strength(mong.getStrength())
                            .build())
                    .build());
        } catch (FeignException ignored) {}
    }

    public void publishWeight(Mong mong) {
        try {
            notificationClient.publishWeight(MqttReqDto.builder()
                    .accountId(mong.getAccountId())
                    .data(PublishWeightVo.builder()
                            .mongId(mong.getId())
                            .weight(mong.getWeight())
                            .build())
                    .build());
        } catch (FeignException ignored) {}
    }
}
