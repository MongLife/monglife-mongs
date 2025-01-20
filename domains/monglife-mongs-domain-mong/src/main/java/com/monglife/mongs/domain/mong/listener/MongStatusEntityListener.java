package com.monglife.mongs.domain.mong.listener;

import com.monglife.mongs.domain.mong.dto.event.MongStatusObserveEvent;
import com.monglife.mongs.domain.mong.entity.MongStatusEntity;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongStatusEntityListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    @PostUpdate
    public void postUpdate(MongStatusEntity mongStatusEntity) {

        applicationEventPublisher.publishEvent(MongStatusObserveEvent.builder()
                .mongId(mongStatusEntity.getMong().getMongId())
                .statusCode(mongStatusEntity.getCode())
                .expRatio(mongStatusEntity.getExpRatio())
                .weight(mongStatusEntity.getWeight())
                .strengthRatio(mongStatusEntity.getStrengthRatio())
                .satietyRatio(mongStatusEntity.getSatietyRatio())
                .healthyRatio(mongStatusEntity.getHealthyRatio())
                .fatigueRatio(mongStatusEntity.getFatigueRatio())
                .poopCount(mongStatusEntity.getPoopCount())
                .updatedAt(mongStatusEntity.getUpdatedAt())
                .build());
    }
}
