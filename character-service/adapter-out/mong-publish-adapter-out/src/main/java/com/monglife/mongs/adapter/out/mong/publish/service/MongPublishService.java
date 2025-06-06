package com.monglife.mongs.adapter.out.mong.publish.service;

import com.monglife.core.dto.event.SendNotificationDto;
import com.monglife.module.common.kafka.service.KafkaService;
import com.monglife.mongs.adapter.out.mong.publish.client.MongPublishClient;
import com.monglife.mongs.adapter.out.mong.publish.dto.response.MongPublishDto;
import com.monglife.mongs.application.mong.port.out.MongPublishPort;
import com.monglife.mongs.core.kafka.event.enums.EventTopic;
import com.monglife.mongs.domain.mong.model.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongPublishService implements MongPublishPort {

    private final MongPublishClient mongPublishClient;

    private final KafkaService kafkaService;

    @Value("${spring.config.activate.on-profile}")
    private String profile;

    /**
     * 몽 정보 비동기 응답
     * @param mong 몽 도메인 객체
     */
    @Override
    public void publishMongPort(Mong mong) {

        double expRatio      = mong.getExp()      / mong.getMaxStatus() * 100;
        double strengthRatio = mong.getStrength() / mong.getMaxStatus() * 100;
        double satietyRatio  = mong.getSatiety()  / mong.getMaxStatus() * 100;
        double healthyRatio  = mong.getHealthy()  / mong.getMaxStatus() * 100;
        double fatigueRatio  = mong.getFatigue()  / mong.getMaxStatus() * 100;

        MongPublishDto mongPublishDto = MongPublishDto.builder()
                .mongId(mong.getMongId())
                .name(mong.getName())
                .mongCode(mong.getMongCode())
                .payPoint(mong.getPayPoint())
                .stateCode(mong.getStateCode().name())
                .isSleep(mong.getIsSleep())
                .statusCode(mong.getStatusCode().name())
                .weight(mong.getWeight())
                .expRatio(expRatio)
                .strengthRatio(strengthRatio)
                .satietyRatio(satietyRatio)
                .healthyRatio(healthyRatio)
                .fatigueRatio(fatigueRatio)
                .createdAt(mong.getCreatedAt())
                .updatedAt(mong.getUpdatedAt())
                .build();

        // 비동기 응답 전송
        mongPublishClient.publishMong(mongPublishDto);
    }

    /**
     * 몽 변동 알림 응답
     * @param accountId 계정 ID
     * @param title 알림 제목
     * @param body 알림 본문
     */
    @Override
    public void publishNotificationPort(Long accountId, String title, String body) {

        kafkaService.generateEventWithProfile(EventTopic.NOTIFICATION, SendNotificationDto.builder()
                .accountId(accountId)
                .title(title)
                .body(body)
                .isAppForegroundMessage(false)
                .build());
    }
}
