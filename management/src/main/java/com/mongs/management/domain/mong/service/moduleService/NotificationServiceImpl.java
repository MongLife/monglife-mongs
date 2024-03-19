package com.mongs.management.domain.mong.service.moduleService;

import com.mongs.core.vo.mqtt.*;
import com.mongs.management.domain.mong.client.NotificationClient;
import com.mongs.management.domain.mong.entity.Mong;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationClient notificationClient;

    @Override
    public void publishCreate(Mong mong) {
        try {
            notificationClient.publishCreate(MqttReqDto.builder()
                    .accountId(mong.getAccountId())
                    .data(PublishCreateVo.builder()
                            .mongId(mong.getId())
                            .name(mong.getName())
                            .mongCode(mong.getMongCode())
                            .weight(mong.getWeight())
                            .strength(mong.getStrength())
                            .satiety(mong.getSatiety())
                            .health(mong.getHealthy())
                            .sleep(mong.getSleep())
                            .poopCount(mong.getNumberOfPoop())
                            .stateCode(mong.getState().getCode())
                            .shiftCode(mong.getShift().getCode())
                            .payPoint(mong.getPayPoint())
                            .born(mong.getCreatedAt())
                            .build())
                    .build());
        } catch (FeignException ignored) {}
    }

    @Override
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

    @Override
    public void publishStroke(Mong mong) {
        try {
            notificationClient.publishStroke(MqttReqDto.builder()
                    .accountId(mong.getAccountId())
                    .data(PublishStrokeVo.builder()
                            .mongId(mong.getId())
                            .exp(mong.getExp())
                            .build())
                    .build());
        } catch (FeignException ignored) {}
    }

    @Override
    public void publishFeed(Mong mong) {
        try {
            notificationClient.publishFeed(MqttReqDto.builder()
                    .accountId(mong.getAccountId())
                    .data(PublishFeedVo.builder()
                            .mongId(mong.getId())
                            .weight(mong.getWeight())
                            .strength(mong.getStrength())
                            .satiety(mong.getSatiety())
                            .health(mong.getHealthy())
                            .sleep(mong.getSleep())
                            .exp(mong.getExp())
                            .build())
                    .build());
        } catch (FeignException ignored) {}
    }

    @Override
    public void publishSleeping(Mong mong) {
        try {
            notificationClient.publishSleep(MqttReqDto.builder()
                    .accountId(mong.getAccountId())
                    .data(PublishSleepingVo.builder()
                            .mongId(mong.getId())
                            .isSleeping(mong.getIsSleeping())
                            .build())
                    .build());
        } catch (FeignException ignored) {}
    }

    @Override
    public void publishPoop(Mong mong) {
        try {
            notificationClient.publishPoop(MqttReqDto.builder()
                    .data(PublishPoopVo.builder()
                            .mongId(mong.getId())
                            .poopCount(mong.getNumberOfPoop())
                            .exp(mong.getExp())
                            .build())
                    .build());
        } catch (FeignException ignored) {}
    }

    @Override
    public void publishTraining(Mong mong) {
        try {
            notificationClient.publishTraining(MqttReqDto.builder()
                    .accountId(mong.getAccountId())
                    .data(PublishTrainingVo.builder()
                            .mongId(mong.getId())
                            .payPoint(mong.getPayPoint())
                            .strength(mong.getStrength())
                            .exp(mong.getExp())
                            .build())
                    .build());
        } catch (FeignException ignored) {}
    }

    @Override
    public void publishGraduation(Mong mong) {
        try {
            notificationClient.publishGraduation(MqttReqDto.builder()
                    .accountId(mong.getAccountId())
                    .data(PublishGraduationVo.builder()
                            .mongId(mong.getId())
                            .isActive(mong.getIsActive())
                            .poopCount(mong.getNumberOfPoop())
                            .health(mong.getHealthy())
                            .satiety(mong.getSatiety())
                            .sleep(mong.getSleep())
                            .strength(mong.getStrength())
                            .weight(mong.getWeight())
                            .shiftCode(mong.getShift().getCode())
                            .stateCode(mong.getState().getCode())
                            .build())
                    .build());
        } catch (FeignException ignored) {}
    }

    @Override
    public void publishEvolution(Mong mong) {
        try {
            notificationClient.publishEvolution(MqttReqDto.builder()
                    .accountId(mong.getAccountId())
                    .data(PublishEvolutionVo.builder()
                            .mongId(mong.getId())
                            .mongCode(mong.getMongCode())
                            .shiftCode(mong.getShift().getCode())
                            .stateCode(mong.getState().getCode())
                            .exp(mong.getExp())
                            .build())
                    .build());
        } catch (FeignException ignored) {}
    }

    @Override
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
}
