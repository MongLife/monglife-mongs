package com.monglife.mongs.domain.mong.listener;

import com.monglife.mongs.domain.mong.dto.event.MongObserveStatusEvent;
import com.monglife.mongs.domain.mong.entity.MongStatusEntity;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongStatusEntityListener {

    private final ApplicationEventPublisher applicationEventPublisher;


    /**
     * 몽 지수 변경 트리거
     * @param mongStatusEntity 변경된 몽 지수 엔티티
     */
    @PostUpdate
    public void postUpdate(MongStatusEntity mongStatusEntity) {

        MongObserveStatusEvent mongObserveStatusEvent = MongObserveStatusEvent.builder()
                .mongId(mongStatusEntity.getMong().getMongId())
                .statusCode(mongStatusEntity.getCode())
                .weight(mongStatusEntity.getWeight())
                .expRatio(mongStatusEntity.getExpRatio())
                .strengthRatio(mongStatusEntity.getStrengthRatio())
                .satietyRatio(mongStatusEntity.getSatietyRatio())
                .healthyRatio(mongStatusEntity.getHealthyRatio())
                .fatigueRatio(mongStatusEntity.getFatigueRatio())
                .poopCount(mongStatusEntity.getPoopCount())
                .build();

        applicationEventPublisher.publishEvent(mongObserveStatusEvent);
    }
}
