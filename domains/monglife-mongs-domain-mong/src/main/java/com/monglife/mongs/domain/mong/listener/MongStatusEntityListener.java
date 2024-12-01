package com.monglife.mongs.domain.mong.listener;

import com.monglife.mongs.domain.mong.entity.MongStatusEntity;
import com.monglife.mongs.domain.mong.dto.event.MongObserveStatusEvent;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MongStatusEntityListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    @PostUpdate
    public void postUpdate(MongStatusEntity mongStatusEntity) {

        MongObserveStatusEvent mongObserveStatusEvent = MongObserveStatusEvent.builder()
                .mongId(mongStatusEntity.getMong().getMongId())
                .statusCode(mongStatusEntity.getCode())
                .weight(mongStatusEntity.getWeight())
                .expRatio(mongStatusEntity.getExp() / mongStatusEntity.getMaxStatus() * 100)
                .strengthRatio(mongStatusEntity.getStrengthRatio())
                .satietyRatio(mongStatusEntity.getSatietyRatio())
                .healthyRatio(mongStatusEntity.getHealthyRatio())
                .fatigueRatio(mongStatusEntity.getFatigueRatio())
                .poopCount(mongStatusEntity.getPoopCount())
                .build();

        applicationEventPublisher.publishEvent(mongObserveStatusEvent);
    }
}
